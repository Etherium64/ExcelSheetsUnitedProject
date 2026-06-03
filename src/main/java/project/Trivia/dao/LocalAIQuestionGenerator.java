package project.Trivia.dao;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates trivia questions using a local Ollama model and stores them in the trivia database.
 * Ollama must be installed, running, and have at least one supported model downloaded.
 */
public class LocalAIQuestionGenerator {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    /**
     * Tries these local Ollama models in order.
     * To install one, run for example:
     * ollama pull llama3.2
     */
    private static final String[] MODEL_NAMES = {
            "llama3.2",
            "llama3.1",
            "llama3",
            "mistral",
            "gemma2:2b"
    };

    /**
     * Attempts to generate and insert AI trivia questions.
     *
     * @param amount number of questions to request from Ollama
     */
    public static void generateAndInsertQuestions(int amount) {
        try {
            createUsedAiQuestionsTable();

            Set<String> blockedQuestions = getBlockedQuestions();

            String response = requestQuestionsFromOllama(amount, blockedQuestions);
            List<GeneratedQuestion> questions = parseGeneratedQuestions(response, blockedQuestions);

            if (questions.isEmpty()) {
                System.out.println("AI trivia generation skipped: Ollama returned no new valid questions.");
                return;
            }

            int insertedCount = insertQuestions(questions);
            System.out.println("AI trivia questions generated: " + insertedCount);

        } catch (ConnectException e) {
            System.out.println("AI trivia generation skipped: Ollama is not running. Start Ollama first.");
        } catch (Exception e) {
            System.out.println("AI trivia generation skipped: " + safeMessage(e));
            e.printStackTrace();
        }
    }

    /**
     * Creates the table used to remember AI questions that have already appeared.
     */
    private static void createUsedAiQuestionsTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS used_ai_questions (
                    question TEXT PRIMARY KEY
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println("Could not create used AI question table: " + safeMessage(e));
        }
    }

    /**
     * Gets all questions that AI should avoid generating again.
     * This includes every question already in the database and every AI question already shown.
     *
     * @return set of blocked normalized question text
     */
    private static Set<String> getBlockedQuestions() {
        Set<String> blockedQuestions = new HashSet<>();

        String existingQuestionsSql = "SELECT question FROM questions";
        String usedQuestionsSql = "SELECT question FROM used_ai_questions";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return blockedQuestions;
            }

            try (PreparedStatement ps = conn.prepareStatement(existingQuestionsSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    blockedQuestions.add(normaliseQuestion(rs.getString("question")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(usedQuestionsSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    blockedQuestions.add(normaliseQuestion(rs.getString("question")));
                }
            }

        } catch (Exception e) {
            System.out.println("Could not load blocked AI questions: " + safeMessage(e));
        }

        return blockedQuestions;
    }

    /**
     * Tries each supported local Ollama model until one works.
     *
     * @param amount number of questions to generate
     * @param blockedQuestions questions the AI should avoid
     * @return raw model response text
     * @throws Exception if no model works
     */
    private static String requestQuestionsFromOllama(int amount, Set<String> blockedQuestions) throws Exception {
        Exception lastError = null;

        for (String modelName : MODEL_NAMES) {
            try {
                return requestQuestionsFromModel(amount, modelName, blockedQuestions);
            } catch (Exception e) {
                lastError = e;
                System.out.println("Ollama model skipped: " + modelName + " - " + safeMessage(e));
            }
        }

        throw new RuntimeException(
                "No supported Ollama model found. Install one with: ollama pull llama3.2",
                lastError
        );
    }

    /**
     * Sends a prompt to one local Ollama model asking for trivia questions.
     *
     * @param amount number of questions to generate
     * @param modelName Ollama model name
     * @param blockedQuestions questions the AI should avoid
     * @return raw model response text
     * @throws Exception if the HTTP request fails
     */
    private static String requestQuestionsFromModel(int amount, String modelName, Set<String> blockedQuestions) throws Exception {
        String avoidedQuestionsText = buildAvoidedQuestionsText(blockedQuestions);

        String prompt = """
                Generate %d new general knowledge trivia questions.

                Return ONLY this exact format, one question per line:
                question|option_a|option_b|option_c|option_d|correct_answer

                Rules:
                - no numbering
                - no markdown
                - no explanations
                - correct_answer must exactly match one of the four options
                - questions must be suitable for a simple trivia game
                - avoid duplicate questions
                - do not repeat the same question idea with slightly different wording
                - make the questions varied across history, science, geography, sport, music, film, technology, and culture
                - do not generate any question from the avoid list below

                Avoid these existing or previously used questions:
                %s
                """.formatted(amount, avoidedQuestionsText);

        String jsonBody = """
                {
                  "model": "%s",
                  "prompt": "%s",
                  "stream": false
                }
                """.formatted(modelName, escapeJson(prompt));

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .timeout(Duration.ofSeconds(120))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
            throw new RuntimeException("Ollama returned status code " + httpResponse.statusCode()
                    + ". Body: " + httpResponse.body());
        }

        System.out.println("Using Ollama model: " + modelName);
        return extractResponseField(httpResponse.body());
    }

    /**
     * Builds a compact avoid list for the model prompt.
     *
     * @param blockedQuestions normalized blocked questions
     * @return prompt text
     */
    private static String buildAvoidedQuestionsText(Set<String> blockedQuestions) {
        if (blockedQuestions.isEmpty()) {
            return "None";
        }

        StringBuilder builder = new StringBuilder();
        int count = 0;

        for (String question : blockedQuestions) {
            builder.append("- ").append(question).append("\n");
            count++;

            if (count >= 80) {
                break;
            }
        }

        return builder.toString();
    }

    /**
     * Extracts the response field from Ollama's JSON response without requiring an external JSON library.
     *
     * @param json raw Ollama JSON response
     * @return generated response text
     */
    private static String extractResponseField(String json) {
        String key = "\"response\":\"";
        int start = json.indexOf(key);

        if (start == -1) {
            throw new RuntimeException("No response field found in Ollama output. Full output: " + json);
        }

        start += key.length();

        StringBuilder result = new StringBuilder();
        boolean escaping = false;

        for (int i = start; i < json.length(); i++) {
            char current = json.charAt(i);

            if (escaping) {
                switch (current) {
                    case 'n' -> result.append('\n');
                    case 't' -> result.append('\t');
                    case 'r' -> result.append('\r');
                    case '"' -> result.append('"');
                    case '\\' -> result.append('\\');
                    default -> result.append(current);
                }

                escaping = false;
                continue;
            }

            if (current == '\\') {
                escaping = true;
                continue;
            }

            if (current == '"') {
                break;
            }

            result.append(current);
        }

        return result.toString();
    }

    /**
     * Parses pipe-separated generated question lines into GeneratedQuestion objects.
     * Questions already in the blocked set are rejected.
     *
     * @param response text returned by Ollama
     * @param blockedQuestions normalized questions that cannot be used
     * @return valid generated questions
     */
    private static List<GeneratedQuestion> parseGeneratedQuestions(String response, Set<String> blockedQuestions) {
        List<GeneratedQuestion> questions = new ArrayList<>();
        Set<String> questionsGeneratedThisBatch = new HashSet<>();

        String[] lines = response.split("\\R");

        for (String line : lines) {
            String cleanedLine = line.trim();

            if (cleanedLine.isEmpty()) {
                continue;
            }

            if (cleanedLine.matches("^\\d+\\.\\s+.*")) {
                cleanedLine = cleanedLine.replaceFirst("^\\d+\\.\\s+", "");
            }

            String[] parts = cleanedLine.split("\\|");

            if (parts.length != 6) {
                continue;
            }

            String question = parts[0].trim();
            String optionA = parts[1].trim();
            String optionB = parts[2].trim();
            String optionC = parts[3].trim();
            String optionD = parts[4].trim();
            String correctAnswer = parts[5].trim();

            String normalizedQuestion = normaliseQuestion(question);

            boolean correctMatchesOption =
                    correctAnswer.equals(optionA)
                            || correctAnswer.equals(optionB)
                            || correctAnswer.equals(optionC)
                            || correctAnswer.equals(optionD);

            if (question.isEmpty()
                    || optionA.isEmpty()
                    || optionB.isEmpty()
                    || optionC.isEmpty()
                    || optionD.isEmpty()
                    || correctAnswer.isEmpty()
                    || !correctMatchesOption
                    || blockedQuestions.contains(normalizedQuestion)
                    || questionsGeneratedThisBatch.contains(normalizedQuestion)) {
                continue;
            }

            questionsGeneratedThisBatch.add(normalizedQuestion);
            questions.add(new GeneratedQuestion(question, optionA, optionB, optionC, optionD, correctAnswer));
        }

        return questions;
    }

    /**
     * Inserts generated questions into the existing questions table.
     * INSERT OR IGNORE prevents exact duplicate questions from being added.
     *
     * @param questions generated trivia questions
     * @return number of questions inserted
     */
    private static int insertQuestions(List<GeneratedQuestion> questions) {
        String sql = """
                INSERT OR IGNORE INTO questions
                (question, option_a, option_b, option_c, option_d, correct_answer)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        int insertedCount = 0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Could not insert AI trivia questions: database connection is null.");
                return 0;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (GeneratedQuestion question : questions) {
                    ps.setString(1, question.question());
                    ps.setString(2, question.optionA());
                    ps.setString(3, question.optionB());
                    ps.setString(4, question.optionC());
                    ps.setString(5, question.optionD());
                    ps.setString(6, question.correctAnswer());

                    insertedCount += ps.executeUpdate();
                }
            }

        } catch (Exception e) {
            System.out.println("Could not insert AI trivia questions: " + safeMessage(e));
            e.printStackTrace();
        }

        return insertedCount;
    }

    /**
     * Normalizes question text so small casing and spacing differences are treated as the same question.
     *
     * @param question question text
     * @return normalized question text
     */
    private static String normaliseQuestion(String question) {
        if (question == null) {
            return "";
        }

        return question
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /**
     * Escapes text so it can be safely placed inside a JSON string.
     *
     * @param value raw text
     * @return escaped JSON text
     */
    private static String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Gets a readable exception message.
     *
     * @param e exception
     * @return readable message
     */
    private static String safeMessage(Exception e) {
        String message = e.getMessage();

        if (message == null || message.isBlank()) {
            return e.getClass().getSimpleName();
        }

        return message;
    }

    /**
     * Data holder for an AI-generated trivia question.
     */
    private record GeneratedQuestion(
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer
    ) {
    }
}
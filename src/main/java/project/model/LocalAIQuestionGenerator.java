package project.model;

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
 */
public class LocalAIQuestionGenerator {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";

    private static final String[] MODEL_NAMES = {
            "llama3.2",
            "llama3.1",
            "llama3",
            "mistral",
            "gemma2:2b"
    };

    private static final int BATCH_SIZE = 10;
    private static final int MAX_GENERATION_ATTEMPTS = 8;

    public static int generateUntilAiQuestionCount(int targetAiQuestionCount) {
        ensureAskedColumnExists();

        int insertedTotal = 0;
        int attempts = 0;

        System.out.println("Starting AI trivia generation. Target AI questions: " + targetAiQuestionCount);

        while (countAiQuestionsInDatabase() < targetAiQuestionCount && attempts < MAX_GENERATION_ATTEMPTS) {
            attempts++;

            int currentAiCount = countAiQuestionsInDatabase();
            int remaining = targetAiQuestionCount - currentAiCount;
            int batchAmount = Math.min(BATCH_SIZE, remaining);

            System.out.println("AI trivia generation attempt " + attempts + ". Current AI questions: " + currentAiCount);

            int insertedThisBatch = generateAndInsertQuestionBatch(batchAmount);
            insertedTotal += insertedThisBatch;

            if (insertedThisBatch == 0) {
                System.out.println("AI trivia generation stopped because this batch inserted 0 questions.");
                break;
            }
        }

        System.out.println("AI trivia generation finished. New questions inserted: " + insertedTotal);
        System.out.println("Total AI questions now stored: " + countAiQuestionsInDatabase());

        return insertedTotal;
    }

    public static int generateAndInsertQuestions(int amount) {
        ensureAskedColumnExists();

        int insertedTotal = 0;
        int attempts = 0;

        while (insertedTotal < amount && attempts < MAX_GENERATION_ATTEMPTS) {
            attempts++;

            int remaining = amount - insertedTotal;
            int batchAmount = Math.min(BATCH_SIZE, remaining);

            int insertedThisBatch = generateAndInsertQuestionBatch(batchAmount);
            insertedTotal += insertedThisBatch;

            if (insertedThisBatch == 0) {
                break;
            }
        }

        return insertedTotal;
    }

    private static int generateAndInsertQuestionBatch(int amount) {
        try {
            Set<String> existingQuestions = getExistingQuestions();

            String response = requestQuestionsFromOllama(amount);
            List<GeneratedQuestion> questions = parseGeneratedQuestions(response, existingQuestions);

            if (questions.isEmpty()) {
                System.out.println("AI trivia batch skipped: Ollama returned no valid questions.");
                System.out.println("Raw Ollama response:");
                System.out.println(response);
                return 0;
            }

            int insertedCount = insertQuestions(questions);

            System.out.println("AI trivia batch inserted: " + insertedCount);

            return insertedCount;

        } catch (ConnectException e) {
            System.out.println("AI trivia generation skipped: Ollama is not running. Start Ollama first.");
            return 0;
        } catch (Exception e) {
            System.out.println("AI trivia generation skipped: " + safeMessage(e));
            e.printStackTrace();
            return 0;
        }
    }

    public static int countAiQuestionsInDatabase() {
        ensureAskedColumnExists();

        int count = 0;
        String sql = "SELECT question FROM questions";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return 0;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String question = rs.getString("question");

                    if (!isHardcodedQuestion(question)) {
                        count++;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Could not count AI trivia questions: " + safeMessage(e));
        }

        return count;
    }

    public static void ensureAskedColumnExists() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "ALTER TABLE questions ADD COLUMN asked INTEGER NOT NULL DEFAULT 0"
             )) {

            ps.executeUpdate();

        } catch (Exception ignored) {
            // column already exists
        }
    }

    private static Set<String> getExistingQuestions() {
        Set<String> questions = new HashSet<>();

        String sql = "SELECT question FROM questions";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return questions;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    questions.add(normaliseQuestion(rs.getString("question")));
                }
            }

        } catch (Exception e) {
            System.out.println("Could not load existing trivia questions: " + safeMessage(e));
        }

        return questions;
    }

    private static String requestQuestionsFromOllama(int amount) throws Exception {
        Exception lastError = null;

        for (String modelName : MODEL_NAMES) {
            try {
                return requestQuestionsFromModel(amount, modelName);
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

    private static String requestQuestionsFromModel(int amount, String modelName) throws Exception {
        String prompt = """
                Create %d simple general knowledge multiple choice trivia questions.

                Return plain text only.

                Use this exact format for every question:
                question|option_a|option_b|option_c|option_d|correct_answer

                Important rules:
                - one question per line
                - no numbering
                - no bullet points
                - no markdown
                - no headings
                - no explanations
                - do not use extra text before or after the questions
                - correct_answer must be exactly the same text as one of the four options
                - questions should be varied across science, history, geography, animals, sport, music, film, technology, food, and culture

                Valid examples:
                What is the largest planet in our solar system?|Earth|Mars|Jupiter|Venus|Jupiter
                Which country is famous for the Eiffel Tower?|Italy|France|Spain|Germany|France
                Which animal is known as the king of the jungle?|Tiger|Lion|Elephant|Bear|Lion
                """.formatted(amount);

        String jsonBody = """
                {
                  "model": "%s",
                  "prompt": "%s",
                  "stream": false,
                  "options": {
                    "temperature": 0.8,
                    "num_predict": 2048
                  }
                }
                """.formatted(modelName, escapeJson(prompt));

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .timeout(Duration.ofSeconds(180))
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

    private static List<GeneratedQuestion> parseGeneratedQuestions(String response, Set<String> existingQuestions) {
        List<GeneratedQuestion> questions = new ArrayList<>();
        Set<String> questionsGeneratedThisBatch = new HashSet<>();

        String[] lines = response.split("\\R");

        for (String line : lines) {
            String cleanedLine = cleanGeneratedLine(line);

            if (cleanedLine.isEmpty()) {
                continue;
            }

            String[] parts = cleanedLine.split("\\|", -1);

            if (parts.length != 6) {
                continue;
            }

            String question = removeWrappingQuotes(parts[0].trim());
            String optionA = removeWrappingQuotes(parts[1].trim());
            String optionB = removeWrappingQuotes(parts[2].trim());
            String optionC = removeWrappingQuotes(parts[3].trim());
            String optionD = removeWrappingQuotes(parts[4].trim());
            String correctAnswer = removeWrappingQuotes(parts[5].trim());

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
                    || existingQuestions.contains(normalizedQuestion)
                    || questionsGeneratedThisBatch.contains(normalizedQuestion)) {
                continue;
            }

            questionsGeneratedThisBatch.add(normalizedQuestion);
            questions.add(new GeneratedQuestion(question, optionA, optionB, optionC, optionD, correctAnswer));
        }

        return questions;
    }

    private static String cleanGeneratedLine(String line) {
        if (line == null) {
            return "";
        }

        String cleanedLine = line.trim();

        cleanedLine = cleanedLine.replace("```", "").trim();

        if (cleanedLine.matches("^\\d+\\.\\s+.*")) {
            cleanedLine = cleanedLine.replaceFirst("^\\d+\\.\\s+", "");
        }

        if (cleanedLine.matches("^\\d+\\)\\s+.*")) {
            cleanedLine = cleanedLine.replaceFirst("^\\d+\\)\\s+", "");
        }

        if (cleanedLine.matches("^-\\s+.*")) {
            cleanedLine = cleanedLine.replaceFirst("^-\\s+", "");
        }

        return cleanedLine;
    }

    private static String removeWrappingQuotes(String value) {
        if (value == null) {
            return "";
        }

        String cleanedValue = value.trim();

        if (cleanedValue.length() >= 2
                && cleanedValue.startsWith("\"")
                && cleanedValue.endsWith("\"")) {
            return cleanedValue.substring(1, cleanedValue.length() - 1).trim();
        }

        return cleanedValue;
    }

    private static int insertQuestions(List<GeneratedQuestion> questions) {
        String sql = """
                INSERT OR IGNORE INTO questions
                (question, option_a, option_b, option_c, option_d, correct_answer, asked)
                VALUES (?, ?, ?, ?, ?, ?, 0)
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

    public static boolean isHardcodedQuestion(String question) {
        if (question == null) {
            return false;
        }

        return question.equals("What is the capital of France?")
                || question.equals("Which planet is known as the Red Planet?")
                || question.equals("What is the largest mammal?")
                || question.equals("Who painted the Mona Lisa?")
                || question.equals("What is the chemical symbol for water?")
                || question.equals("Which country is home to the kangaroo?")
                || question.equals("What is the tallest mountain in the world?")
                || question.equals("Which element has the atomic number 1?")
                || question.equals("Who wrote 'Romeo and Juliet'?")
                || question.equals("What is the largest ocean on Earth?")
                || question.equals("Which gas do plants absorb from the atmosphere?")
                || question.equals("What is the closest star to Earth?")
                || question.equals("Which planet has the most moons?")
                || question.equals("What is the main ingredient in guacamole?")
                || question.equals("Which country invented tea?")
                || question.equals("What is the smallest country in the world?");
    }

    public static String normaliseQuestion(String question) {
        if (question == null) {
            return "";
        }

        return question
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String safeMessage(Exception e) {
        String message = e.getMessage();

        if (message == null || message.isBlank()) {
            return e.getClass().getSimpleName();
        }

        return message;
    }

    public record GeneratedQuestion(
            String question,
            String optionA,
            String optionB,
            String optionC,
            String optionD,
            String correctAnswer
    ) {
    }
}
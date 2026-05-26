package project.Trivia.view;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.Trivia.dao.DatabaseConnection;
import project.Trivia.dao.LocalAIQuestionGenerator;
import project.Trivia.dao.ScoreDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for the main Trivia Game interface.
 * <p>
 * Manages user interaction with the quiz UI, including question display,
 * answer handling, score tracking, AI question generation, and saving user scores to the database.
 * Uses FXML injection to bind UI components and coordinates game flow.
 * </p>
 *
 * @author Ethan B
 */
public class MainController {

    @FXML private TextField usernameField;
    @FXML public Label scoreLabel;
    @FXML private Label statusLabel;
    @FXML private Label questionLabel;
    @FXML public Button buttonA, buttonB, buttonC, buttonD;
    @FXML public Button EndButton;

    @FXML private GridPane triviaSection;
    @FXML private VBox usernameSection;

    private int score = 0;
    public String correctAnswer;
    private int questionCount = 0;

    private static final int MAX_QUESTIONS = 5;
    private static final int AI_QUESTION_AMOUNT = 10;

    private final ScoreDAO dao = new ScoreDAO();

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    /**
     * Inner class representing a trivia question.
     */
    public class Question {

        String question;
        String a;
        String b;
        String c;
        String d;
        String correct;

        /**
         * True if this question came from the original hardcoded seeder.
         */
        boolean hardcoded;

        public Question(String q,
                        String a,
                        String b,
                        String c,
                        String d,
                        String correct,
                        boolean hardcoded) {

            this.question = q;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.correct = correct;
            this.hardcoded = hardcoded;
        }
    }

    /**
     * Initializes the controller after FXML injection.
     * Loads existing database questions immediately so the displayed question does not change
     * while Ollama generates extra questions in the background.
     */
    @FXML
    private void initialize() {
        loadQuestions();
        Collections.shuffle(questions);
        loadNextQuestion();

        generateAiQuestionsInBackground();
    }

    /**
     * Generates additional AI trivia questions using local Ollama.
     * This runs in the background so the Trivia window does not freeze while Ollama responds.
     * The current quiz is not reloaded afterward, preventing the first displayed question from changing.
     */
    private void generateAiQuestionsInBackground() {
        CompletableFuture.runAsync(() -> LocalAIQuestionGenerator.generateAndInsertQuestions(AI_QUESTION_AMOUNT))
                .exceptionally(error -> {
                    error.printStackTrace();
                    return null;
                });
    }

    /**
     * Handles answer button clicks.
     * <p>
     * Evaluates the selected answer, updates score and status, and loads the next question.
     * After MAX_QUESTIONS, disables buttons and transitions to username input.
     * </p>
     *
     * @param event the ActionEvent from the clicked button
     */
    @FXML
    private void onAnswerClick(javafx.event.ActionEvent event) {
        if (questionCount >= MAX_QUESTIONS) {
            return;
        }

        Button selected = (Button) event.getSource();

        if (selected.getText().equals(correctAnswer)) {
            score++;
            scoreLabel.setText(String.valueOf(score));
            statusLabel.setText("Correct!");
        } else {
            statusLabel.setText("Incorrect! Maybe next time");
        }

        questionCount++;

        if (questionCount >= MAX_QUESTIONS) {
            statusLabel.setText("Quiz complete! Final score: " + score);
            disableButtons();

            VBox parent = (VBox) EndButton.getParent();
            parent.getChildren().remove(EndButton);

            triviaSection.setVisible(false);
            triviaSection.setManaged(false);

            usernameSection.setVisible(true);
            usernameSection.setManaged(true);
        } else {
            loadNextQuestion();
        }
    }

    /**
     * Disables all answer buttons to prevent further input after quiz completion.
     */
    private void disableButtons() {
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);
        buttonD.setDisable(true);
        EndButton.setDisable(true);
    }

    /**
     * Loads all questions from the database into the questions list.
     * Questions from the original seeded set are marked with an asterisk.
     */
    private void loadQuestions() {
        String sql = "SELECT * FROM questions";

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn == null) {
                questionLabel.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String questionText = rs.getString("question");

                    boolean hardcoded = isHardcodedQuestion(questionText);

                    questions.add(new Question(
                            questionText,
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_answer"),
                            hardcoded
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Determines whether a question came from the original hardcoded seeder.
     *
     * @param question question text
     * @return true if it is one of the original seeded questions
     */
    private boolean isHardcodedQuestion(String question) {

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

    /**
     * Loads and displays the next question from the shuffled list.
     * Updates the UI with the question text and answer options.
     */
    private void loadNextQuestion() {

        if (questions.isEmpty()) {
            questionLabel.setText("No trivia questions available.");

            buttonA.setDisable(true);
            buttonB.setDisable(true);
            buttonC.setDisable(true);
            buttonD.setDisable(true);

            return;
        }

        if (currentQuestionIndex >= questions.size()
                || currentQuestionIndex >= MAX_QUESTIONS) {

            return;
        }

        Question q = questions.get(currentQuestionIndex++);

        // add an asterisk to hardcoded seeded questions
        if (q.hardcoded) {
            questionLabel.setText("* " + q.question);
        } else {
            questionLabel.setText(q.question);
        }

        buttonA.setText(q.a);
        buttonB.setText(q.b);
        buttonC.setText(q.c);
        buttonD.setText(q.d);

        correctAnswer = q.correct;
    }

    /**
     * Displays a popup showing the top 5 scores from the database.
     * The popup automatically closes after 10 seconds.
     */
    private void showTopScores() {

        VBox popup = new VBox(10);

        popup.setAlignment(Pos.CENTER);
        popup.setStyle("-fx-background-color: white; -fx-padding: 20;");

        Label title = new Label("Top 5 Scores");

        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        popup.getChildren().add(title);

        String sql =
                "SELECT u.username, s.score " +
                        "FROM scores s " +
                        "JOIN users u ON s.user_id = u.id " +
                        "ORDER BY s.score DESC " +
                        "LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn == null) {
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                int rank = 1;

                while (rs.next()) {

                    Label entry = new Label(
                            rank++ + ". "
                                    + rs.getString("username")
                                    + " - "
                                    + rs.getString("score")
                    );

                    popup.getChildren().add(entry);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scene popupScene = new Scene(popup, 300, 200);

        Stage popupStage = new Stage();

        popupStage.setScene(popupScene);
        popupStage.initOwner(usernameSection.getScene().getWindow());

        popupStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(10_000);
                Platform.runLater(popupStage::close);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Handles saving the user's score to the database.
     * <p>
     * Validates username input, saves the score via DAO, and displays top scores.
     * Shows an error message if username is empty.
     * </p>
     */
    @FXML
    private void onSaveClick() {

        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            statusLabel.setText("Enter username");
            return;
        }

        dao.saveScore(username, score);

        statusLabel.setText("Score saved!");

        showTopScores();

        PauseTransition delay = new PauseTransition(Duration.seconds(3));

        delay.setOnFinished(event -> {
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.close();
        });

        delay.play();
    }

    /**
     * Closes the trivia window without saving the score.
     */
    @FXML
    private void dontSave() {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Ends the trivia game and closes the trivia window.
     */
    @FXML
    private void endTrivia() {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }
}
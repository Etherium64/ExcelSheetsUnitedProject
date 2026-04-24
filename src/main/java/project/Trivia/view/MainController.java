package project.Trivia.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.Trivia.dao.DatabaseConnection;
import project.Trivia.dao.ScoreDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for the main Trivia Game interface.
 * <p>
 * Manages user interaction with the quiz UI, including question display,
 * answer handling, score tracking, and saving user scores to the database.
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

    private int score = 0;
    public String correctAnswer;
    private int questionCount = 0;
    private static final int MAX_QUESTIONS = 5;
    private final ScoreDAO dao = new ScoreDAO();

    @FXML private VBox triviaSection;
    @FXML private VBox usernameSection;

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
        if (questionCount >= MAX_QUESTIONS) return;

        Button selected = (Button) event.getSource();
        if (selected.getText().equals(correctAnswer)) {
            score++;
            scoreLabel.setText(String.valueOf(score));
            statusLabel.setText("Correct!");
        } else {
            statusLabel.setText("Wrong! Try again.");
        }

        questionCount++;
        if (questionCount >= MAX_QUESTIONS) {
            statusLabel.setText("Quiz complete! Final score: " + score);
            disableButtons();
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
    }
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    /**
     * Inner class representing a trivia question.
     */
    public class Question {
        String question, a, b, c, d, correct;
        public Question(String q, String a, String b, String c, String d, String correct) {
            this.question = q; this.a = a; this.b = b; this.c = c; this.d = d; this.correct = correct;
        }
    }

    /**
     * Initializes the controller after FXML injection.
     * <p>
     * Loads and shuffles the list of questions from the database.
     * Then displays the first question.
     * </p>
     */
    @FXML
    private void initialize() {
        loadQuestions();
        Collections.shuffle(questions);
        loadNextQuestion();
    }

    /**
     * Loads all questions from the database into the questions list.
     */
    private void loadQuestions() {
        String sql = "SELECT * FROM questions";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                questions.add(new Question(
                        rs.getString("question"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads and displays the next question from the shuffled list.
     * Updates the UI with the question text and answer options.
     */
    private void loadNextQuestion() {
        if (currentQuestionIndex >= questions.size() || currentQuestionIndex >= MAX_QUESTIONS) {
            // End quiz
            return;
        }
        Question q = questions.get(currentQuestionIndex++);
        questionLabel.setText(q.question);
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

        String sql = "SELECT u.username, s.score FROM scores s JOIN users u ON s.user_id = u.id ORDER BY s.score DESC LIMIT 5";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int rank = 1;
            while (rs.next()) {
                Label entry = new Label(rank++ + ". " + rs.getString("username") + " - " + rs.getString("score"));
                popup.getChildren().add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scene popupScene = new Scene(popup, 300, 200);
        Stage popupStage = new Stage();
        popupStage.setScene(popupScene);
        popupStage.initOwner(usernameSection.getScene().getWindow());
        popupStage.show();

        // Close after 10 seconds
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
    }
}
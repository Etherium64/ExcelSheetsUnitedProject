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


public class MainController {
    @FXML private TextField usernameField;
    @FXML private Label scoreLabel;
    @FXML private Label statusLabel;
    @FXML private Label questionLabel;
    @FXML private Button buttonA, buttonB, buttonC, buttonD;

    private int score = 0;
    private String correctAnswer;
    private int questionCount = 0;
    private static final int MAX_QUESTIONS = 5;
    private final ScoreDAO dao = new ScoreDAO();

    @FXML private VBox triviaSection;
    @FXML private VBox usernameSection;


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
    private void disableButtons() {
        buttonA.setDisable(true);
        buttonB.setDisable(true);
        buttonC.setDisable(true);
        buttonD.setDisable(true);
    }
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    public class Question {
        String question, a, b, c, d, correct;
        public Question(String q, String a, String b, String c, String d, String correct) {
            this.question = q; this.a = a; this.b = b; this.c = c; this.d = d; this.correct = correct;
        }
    }
    @FXML
    private void initialize() {
        loadQuestions();
        Collections.shuffle(questions);
        loadNextQuestion();
    }

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
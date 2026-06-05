package project.Trivia;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.authentication.UserSingleton;
import project.model.DatabaseConnection;
import project.model.LocalAIQuestionGenerator;
import project.model.ScoreDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static final int MAX_QUESTIONS = 5;
    private final ScoreDAO dao = new ScoreDAO();
    private final List<Question> questions = new ArrayList<>();
    @FXML
    public Label scoreLabel;
    @FXML
    public Button buttonA, buttonB, buttonC, buttonD;
    @FXML
    public Button EndButton;
    public String correctAnswer;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private GridPane triviaSection;
    @FXML
    private VBox usernameSection;
    private int score = 0;
    private int questionCount = 0;
    private int currentQuestionIndex = 0;
    private boolean usingHardcodedQuestions = false;

    @FXML
    private void initialize() {
        LocalAIQuestionGenerator.ensureAskedColumnExists();
        usernameLabel.setText(UserSingleton.getInstance().getUsername());
        resetTriviaState();

        if (LocalAIQuestionGenerator.countAiQuestionsInDatabase() >= MAX_QUESTIONS) {
            startTriviaWithAiQuestions();
        } else {
            startTriviaWithHardcodedQuestions();
        }
    }

    private void startTriviaWithHardcodedQuestions() {
        resetTriviaState();
        usingHardcodedQuestions = true;

        resetAskedFlagsIfAllQuestionsAsked(true);
        loadUnaskedHardcodedQuestionsFromDatabase();

        Collections.shuffle(questions);

        statusLabel.setText("");

        enableAnswerButtons();
        loadNextQuestion();
    }

    private void startTriviaWithAiQuestions() {
        resetTriviaState();
        usingHardcodedQuestions = false;

        resetAskedFlagsIfAllQuestionsAsked(false);
        loadUnaskedAiQuestionsFromDatabase();

        Collections.shuffle(questions);

        statusLabel.setText("");

        enableAnswerButtons();
        loadNextQuestion();
    }

    private void resetTriviaState() {
        questions.clear();
        currentQuestionIndex = 0;
        questionCount = 0;
        score = 0;

        if (scoreLabel != null) {
            scoreLabel.setText("0");
        }

        if (statusLabel != null) {
            statusLabel.setText("");
        }
    }

    private void enableAnswerButtons() {
        buttonA.setDisable(false);
        buttonB.setDisable(false);
        buttonC.setDisable(false);
        buttonD.setDisable(false);
        EndButton.setDisable(false);
    }

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

    private void resetAskedFlagsIfAllQuestionsAsked(boolean hardcodedMode) {
        if (countQuestionsByAskedState(hardcodedMode, false) > 0) {
            return;
        }

        String sql = "UPDATE questions SET asked = 0";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int countQuestionsByAskedState(boolean hardcodedMode, boolean asked) {
        int count = 0;
        String sql = "SELECT question, asked FROM questions";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return 0;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String questionText = rs.getString("question");
                    boolean isHardcoded = LocalAIQuestionGenerator.isHardcodedQuestion(questionText);
                    boolean isAsked = rs.getInt("asked") == 1;

                    if (hardcodedMode == isHardcoded && asked == isAsked) {
                        count++;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private void loadUnaskedHardcodedQuestionsFromDatabase() {
        String sql = """
                SELECT *
                FROM questions
                WHERE asked = 0
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                questionLabel.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String questionText = rs.getString("question");

                    if (!LocalAIQuestionGenerator.isHardcodedQuestion(questionText)) {
                        continue;
                    }

                    questions.add(new Question(
                            rs.getInt("question_id"),
                            questionText,
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_answer"),
                            true
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUnaskedAiQuestionsFromDatabase() {
        String sql = """
                SELECT *
                FROM questions
                WHERE asked = 0
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                questionLabel.setText("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String questionText = rs.getString("question");

                    if (LocalAIQuestionGenerator.isHardcodedQuestion(questionText)) {
                        continue;
                    }

                    questions.add(new Question(
                            rs.getInt("question_id"),
                            questionText,
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_answer"),
                            false
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void markQuestionAsAsked(int question_id) {
        String sql = """
                UPDATE questions
                SET asked = 1
                WHERE question_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, question_id);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNextQuestion() {
        if (questions.isEmpty()) {
            questionLabel.setText("No trivia questions available.");

            buttonA.setDisable(true);
            buttonB.setDisable(true);
            buttonC.setDisable(true);
            buttonD.setDisable(true);

            return;
        }

        if (currentQuestionIndex >= questions.size()) {
            if (usingHardcodedQuestions) {
                startTriviaWithHardcodedQuestions();
            } else {
                startTriviaWithAiQuestions();
            }

            return;
        }

        Question q = questions.get(currentQuestionIndex++);
        markQuestionAsAsked(q.question_id);

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

        /*
        String sql =
                "SELECT u.username, s.score " +
                        "FROM scores s " +
                        "JOIN users u ON s.user_id = u.id " +
                        "ORDER BY s.score DESC " +
                        "LIMIT 5";

         */
        String sql = "SELECT u.username, s.scoreValue FROM scores s JOIN users u ON s.user_id = u.user_id ORDER BY s.scoreValue DESC LIMIT 5";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int rank = 1;
            while (rs.next()) {
                Label entry = new Label(rank++ + ". " + rs.getString("username") + " - " + rs.getString("scoreValue"));
                popup.getChildren().add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*try (Connection conn = DatabaseConnection.getConnection()) {
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

         */

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
        String username = usernameLabel.getText().trim();

        if (username.isEmpty()) {
            statusLabel.setText("Enter username");
            return;
        }
        try {
            int user_id = UserSingleton.getInstance().getUser_id();
            int storedScore = dao.get(user_id);
            if (storedScore == -1) {
                dao.insert(user_id, score);
            } else {
                storedScore += score;
                dao.update(user_id, storedScore);
            }


            statusLabel.setText("Score saved!");
            showTopScores();

            // Close the trivia stage after 3 seconds
            PauseTransition delay = new PauseTransition(Duration.seconds(3));

            delay.setOnFinished(event -> {
                Stage stage = (Stage) statusLabel.getScene().getWindow();
                stage.close();
            });

            delay.play();
        } catch (Exception e) {
            statusLabel.setText("Saving error");
            e.printStackTrace();
        }

    }

    @FXML
    private void dontSave() {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void endTrivia() {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }

    public class Question {

        int question_id;
        String question;
        String a;
        String b;
        String c;
        String d;
        String correct;
        boolean hardcoded;

        public Question(int question_id,
                        String q,
                        String a,
                        String b,
                        String c,
                        String d,
                        String correct,
                        boolean hardcoded) {

            this.question_id = question_id;
            this.question = q;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.correct = correct;
            this.hardcoded = hardcoded;
        }
    }

}
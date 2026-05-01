package project.pomodoro.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import project.pomodoro.MainApplication;
import project.pomodoro.model.Session;
import project.pomodoro.model.SessionDAO;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for managing the timer logic in a Pomodoro application.
 * Handles starting, pausing, and resetting the timer, and updates the UI label
 * with the formatted remaining time. Uses a background {@link Timer} thread
 * and {@link Platform#runLater(Runnable)} to safely update JavaFX UI components.
 */

public class PomodoroController implements Initializable {
    /**
     * Singleton instance of the timer controller.
     */
    static private PomodoroController pomodoro;
    private ObservableList<Session> sessionsObservableList;
    private static SessionDAO sessionDAO;
    private Timestamp timestamp;
    private String sessionType;
    /**
     * Remaining time in seconds.
     */
    private static int timeElapsed;
    /**
     * Initial time set for the timer (used for reset).
     */
    private static int timeBegins;
    /**
     * The background timer task that drives the countdown.
     */
    private Timer timer;
    /**
     * The label displaying the timer value in the UI.
     */
    private Label timerLabel;
    /**
     * Indicates whether the timer is currently paused.
     */
    volatile boolean isPaused = true;
    /**
     * Flag to determine if this is the first time the timer is being started.
     */
    private boolean firstCount = true;
    private ProgressBar timerBar;
    private static double progress;
    private static double progressIncrements;
    private Button startPauseBtn;
    private MainApplication backScene = new MainApplication();

    @FXML
    private Button backBtn;

    @FXML
    private TableView<Session> tableView;

    @FXML
    private TableColumn idCol;

    @FXML
    private TableColumn timestampCol;

    @FXML
    private TableColumn typeCol;

    @FXML
    private TableColumn taskCol;

    @FXML
    private TableColumn timespentCol;

    @FXML
    private TableColumn completionCol;

    /**
     * Returns the singleton instance of the TimeController.
     * Creates a new instance if none exists.
     *
     * @return The single instance of TimeController
     */

    static public PomodoroController getPomodoro() {
        if (pomodoro == null) {
            pomodoro = new PomodoroController();
            sessionDAO = new SessionDAO();
            sessionDAO.createTable();
        }
        return pomodoro;
    }

    /**
     * Formats the remaining time into MM:SS string format.
     *
     * @return A string representation of the time in "mm:ss" format with leading zeros.
     */
    private String formatTimer(int timeValue) {
        int minutes = timeValue / 60;
        int seconds = timeValue % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


    public void setPomodoro(String sessionType, Label timerLabel, ProgressBar timerBar, Button startPauseBtn) {

        // Set up sessionDAO
        this.sessionType = sessionType;

        //Set up timer and timner-related fields
        if (Objects.equals(sessionType, "work")) {
            timeElapsed = 1500;
            timeBegins = timeElapsed;
        } else {
            timeElapsed = 300;
            timeBegins = timeElapsed;
        }
        this.timerLabel = timerLabel;
        isPaused = true;

        //Set up Progress bar
        this.timerBar = timerBar;
        progress = 1;
        timerBar.setProgress(progress);
        progressIncrements = (double) 1 / timeBegins;

        //Set up Button
        this.startPauseBtn = startPauseBtn;
    }

    public void unfinishedPomodoro() {
        int timeSpent = timeBegins - timeElapsed;
        String timeSpentReformat = formatTimer(timeSpent);
        Session session = sessionDAO.getByTimestamp(timestamp);
        session.setTimeSpent(timeSpentReformat);
        sessionDAO.update(session);
    }

    public void finishedPomodoro() {
        isPaused = true;
        timeElapsed = 0;
        startPauseBtn.setDisable(true);
        Session session = sessionDAO.getByTimestamp(timestamp);
        if (sessionType == "work") {
            session.setTimeSpent("25:00");
            startPauseBtn.setText("Please have a rest.");
        } else {
            session.setTimeSpent("05:00");
            startPauseBtn.setText("Time for work!");
        }
        session.Completed();
        sessionDAO.update(session);
    }

    public void recordSession(String sessionTask) {
        LocalDateTime now = LocalDateTime.now();
        timestamp = Timestamp.valueOf(now);
        sessionDAO.insert(new Session(timestamp, sessionType, sessionTask, "00:00", false));
    }


    /**
     * Starts or resumes the timer.
     * On first call, creates a new Timer and begins countdown.
     * Subsequent calls resume from paused state.
     */
    public void runPomodoro() {
        isPaused = !isPaused;
        if (firstCount) {
            timer = new Timer();
            firstCount = false;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        timeElapsed--;
                        progress -= progressIncrements;
                        Platform.runLater(() -> {
                            timerLabel.setText(formatTimer(timeElapsed));
                            timerBar.setProgress((progress));
                            if (timeElapsed <= 0) {
                                finishedPomodoro();
                            }
                        });
                    }
                }
            }, 1000, 1000);
        } else {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        Platform.runLater(() -> {
                            timerLabel.setText(formatTimer(timeElapsed));
                            timerBar.setProgress((progress));
                            if (timeElapsed <= 0) {
                                finishedPomodoro();
                            }
                        });
                    }
                }
            }, 1000, 1000);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sessionsObservableList = FXCollections.observableArrayList();

        idCol.setCellValueFactory((new PropertyValueFactory<Session, String>("id")));
        timestampCol.setCellValueFactory((new PropertyValueFactory<Session, String>("timestamp")));
        typeCol.setCellValueFactory((new PropertyValueFactory<Session, String>("sessionType")));
        taskCol.setCellValueFactory((new PropertyValueFactory<Session, String>("sessionTask")));
        timespentCol.setCellValueFactory((new PropertyValueFactory<Session, String>("timeSpent")));
        completionCol.setCellValueFactory((new PropertyValueFactory<Session, String>("completion")));

        sessionsObservableList.addAll(sessionDAO.getAll());
        tableView.setItems(sessionsObservableList);

    }

    @FXML
    public void backBtnClick() throws Exception {
        Stage backStage = (Stage) backBtn.getScene().getWindow();
        backScene.launch(backStage, "work-view.fxml");
    }

    @FXML
    public void deleteBtnClick() {
        Session selectedSession = tableView.getSelectionModel().getSelectedItem();
        sessionDAO.delete(selectedSession);
        tableView.getItems().clear();
        sessionsObservableList.addAll(sessionDAO.getAll());
        tableView.setItems(sessionsObservableList);
    }

}







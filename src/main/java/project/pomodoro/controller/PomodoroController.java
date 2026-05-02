package project.pomodoro.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
 * The Main Controller
 * Manages Timer, Progress bar, TableView, Session DAO and CRUD logic in the Pomodoro application.
 * Handles the start, pause and overall running of Timer / Progress Bar.
 * Uses a background {@link Timer} thread and {@link Platform#runLater(Runnable)} to safely update JavaFX UI components.
 * Handles the recording of finished and unfinished Pomodoro sessions to SessionDB.
 * Handles the CRUD operations via TableView FXML
 *
 *  @author Minhman Do
 */

public class PomodoroController implements Initializable {
    /**
     * Singleton instance of the Pomodoro controller.
     */
    static private PomodoroController pomodoro;

    /**
     * Data Access Object for Session Class.
     * Session is a Custom class for Pomodoro Session and interacted with via CRUD.
     *  */
    private static SessionDAO sessionDAO;
    /**
     * List of Session classes allowing for Listeners. Required to populate the TableView rows. .
     */
    private ObservableList<Session> sessionsObservableList;

    /**
     * Timestamp created the moment Pomodoro Timer starts. Stored as a private field heree to set to Session.
     */
    private Timestamp timestamp;
    /**
     * Type of Session: Work vs Rest.  Stored as a private field heree to set to Session.
     */
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
     * ProgressBar UI element synced to the Timer
     */
    private ProgressBar timerBar;
    /**
     * Progress value assigned to ProgressBar. Decrements in sync with Timer.
     */
    private static double progress;
    /**
     * 1 / Number of total seconds. Ensures progress is decremented accurately.
     */
    private static double progressIncrements;
    /**
     * Start Pause Button UI element. Switches from Start and Pause Text.
     */
    private Button startPauseBtn;
    /**
     * Indicates whether the timer is currently paused.
     */
    volatile boolean isPaused = true;
    /**
     * Flag to determine if this is the first time the timer is being started.
     */
    private boolean firstCount = true;
    /**
     * Instantiation of MainApplication for Scene transition
     */
    private MainApplication newScene = new MainApplication();
    /**
     * Returns to Work Button UI elemnent for TableView
     */
    @FXML
    private Button returnWorkBtn;
    /**
     * Returns to Rest Button UI elemnent for TableView
     */
    @FXML
    private Button returnRestBtn;
    /**
     * TableView FXML UI element
     */
    @FXML
    private TableView<Session> tableView;
    /**
     * TableColumn id for TableView
     */
    @FXML
    private TableColumn<Session, Integer> idCol;
    /**
     * TableColumn timestamp for TableView
     */
    @FXML
    private TableColumn<Session, Timestamp> timestampCol;
    /**
     * TableColumn type for TableView
     */
    @FXML
    private TableColumn<Session, String> typeCol;
    /**
     * TableColumn string task for TableView
     */
    @FXML
    private TableColumn<Session, String> taskCol;
    /**
     * TableColumn string timespent for TableView
     */
    @FXML
    private TableColumn<Session, String> timespentCol;
    /**
     * TableColumn completion for TableView
     */
    @FXML
    private TableColumn<Session, Boolean> completionCol;

    /**
     * Returns the singleton instance of the PomodoroController.
     * Creates a new instance if none exists.
     * Also creates the DAO and the Table.
     *
     * @return The single instance of PomodoroController
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

    /**
     * Sets up the PomodoroController
     * Assigns the required private fields for running Timer, Progress bar, Session, DAO, TableView and CRUD
     */
    public void setPomodoro(String sessionType, Label timerLabel, ProgressBar timerBar, Button startPauseBtn) {
        //Session type taken from MainApplication
        this.sessionType = sessionType;

        //Assign different timeElapsed values depending on Session type
        if (Objects.equals(sessionType, "work")) {
            timeElapsed = 1500;
            timeBegins = timeElapsed;
        } else {
            timeElapsed = 300;
            timeBegins = timeElapsed;
        }
        //Assign Label timerLabel to be dynamically updated by Timer
        this.timerLabel = timerLabel;

        //Set up ProgressBar
        this.timerBar = timerBar;
        progress = 1;
        timerBar.setProgress(progress);
        progressIncrements = (double) 1 / timeBegins;

        //Assign Start Pause Button. Needs to disable once Timer is finishe.
        this.startPauseBtn = startPauseBtn;
    }

    /**
     * If user switches to another scene or resets, record how much time was spent this session.
     *  Handles CRUD - Update operation.
     */
    public void unfinishedPomodoro() {
        int timespent = timeBegins - timeElapsed;
        String timeSpentReformat = formatTimer(timespent);
        Session session = sessionDAO.getByTimestamp(timestamp);
        session.setTimespent(timeSpentReformat);
        sessionDAO.update(session);
    }

    /**
     * If user successfuly finishes the session, record the full timespent and set completion as true.
     * Disables the Start Pause Button to remind user to have rest or do some work.
     * Handles CRUD - Update operation.
     */
    public void finishedPomodoro() {
        isPaused = true;
        timeElapsed = 0;
        startPauseBtn.setDisable(true);
        //Get Session by Timestamp rather than iD, as difficult for current instance of DAO to find latest Session id which auto increments between sessions
        Session session = sessionDAO.getByTimestamp(timestamp);
        if (sessionType == "work") {
            session.setTimespent("25:00");
            startPauseBtn.setText("Please have a rest.");
        } else {
            session.setTimespent("05:00");
            startPauseBtn.setText("Time for work!");
        }
        session.setCompletion(true);
        sessionDAO.update(session);
    }

    /**
     * Record the initial Pomodoro session.
     * Function is called the moment user first clicks the Start Button.
     * Insert session via DAO. CRUD - Create Operation
     */
    public void recordSession(String sessionTask) {
        //Session timestamp is created the moment Start button is clicked
        LocalDateTime now = LocalDateTime.now();
        timestamp = Timestamp.valueOf(now);
        sessionDAO.insert(new Session(timestamp, sessionType, sessionTask, "00:00", false));
    }

    /**
     * Starts or resumes the timer.
     * On first call, creates a new Timer and begins countdown.
     * Subsequent calls resume from paused state.
     * ProgressBar is synced with Timer.
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
                        //Update the timer label and progress bar on the JavaFx Application Thread
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
            //Else NOT the FirstCount, that is, first time User has clicked Start for this Session
            //Continue running the Timer and ProgressBar without invoking timeElapsed-- again to avoid an inaccurate Timer.
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

    /**
     * Initializes the editable TableView FXML
     * TableView is populated by user's recorded Session data stored in the SQLite database
     * Allows Users to directly update Session Data by selecting and editing cells
     * Read and Update CRUD operations are handled here.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialise the obserable list. Used later to populate the TableView cells.
        this.sessionsObservableList = FXCollections.observableArrayList();

        idCol.setCellValueFactory((new PropertyValueFactory<>("id")));
        timestampCol.setCellValueFactory((new PropertyValueFactory<>("timestamp")));

        //Bind the TableColumn typeCol to the sessionType field in the Session class (Data Model)
        typeCol.setCellValueFactory((new PropertyValueFactory<>("sessionType")));
        //Convert TableColumn typeCol cells into a ComboBox UI element
        //Session Type options are either work or rest so ComboBox fits
        typeCol.setCellFactory(ComboBoxTableCell.forTableColumn("work", "rest"));
        //To finalise the update, user must press the Enter Button (the default event)
        typeCol.setOnEditCommit(event -> {
            //Session to be updated is the Table row the user mouse click selects
            Session selectedSession = tableView.getSelectionModel().getSelectedItem();
            //Set session with the new session type field after pressing Enter
            selectedSession.setSessionType(event.getNewValue());
            //Update session data using DAO
            sessionDAO.update(selectedSession);
        });

        //Textfield UI element to allow users to customise their Session Task beyond the initial 4 options
        taskCol.setCellValueFactory((new PropertyValueFactory<>("sessionTask")));
        taskCol.setCellFactory(TextFieldTableCell.<Session>forTableColumn());
        taskCol.setOnEditCommit(event -> {
            Session selectedSession = tableView.getSelectionModel().getSelectedItem();
            selectedSession.setSessionTask(event.getNewValue());
            sessionDAO.update(selectedSession);
        });

        timespentCol.setCellValueFactory((new PropertyValueFactory<>("timespent")));
        timespentCol.setCellFactory(TextFieldTableCell.<Session>forTableColumn());
        timespentCol.setOnEditCommit(event -> {
            Session selectedSession = tableView.getSelectionModel().getSelectedItem();
            selectedSession.setTimespent(event.getNewValue());
            sessionDAO.update(selectedSession);
        });

        //For Completion TableColumn, use ComboBox with options limited to true and false booleans
        completionCol.setCellValueFactory((new PropertyValueFactory<>("completion")));
        completionCol.setCellFactory(ComboBoxTableCell.forTableColumn(true, false));
        completionCol.setOnEditCommit(event -> {
            Session selectedSession = tableView.getSelectionModel().getSelectedItem();
            selectedSession.setCompletion(event.getNewValue());
            sessionDAO.update(selectedSession);
        });

        // Clear the items from Tableview first for a refresh
        tableView.getItems().clear();
        //Add all session data to the ObservableList using DAO getAll method. CRUD - Read operation.
        sessionsObservableList.addAll(sessionDAO.getAll());
        //Populate the Tableview using this Observable List
        tableView.setItems(sessionsObservableList);
    }

    /**
     * Return to Pomodoro Work session upon Button click
     * Belongs to TableView FXML
     */
    @FXML
    public void returnWorkBtnClick() throws Exception {
        Stage newStage = (Stage) returnWorkBtn.getScene().getWindow();
        newScene.launch(newStage, "work-view.fxml");
    }

    /**
     * Return to Pomodoro Rest session upon Button click
     *  Belongs to TableView FXML
     */
    @FXML
    public void returnRestBtnClick() throws Exception {
        Stage newStage = (Stage) returnRestBtn.getScene().getWindow();
        newScene.launch(newStage, "rest-view.fxml");
    }

    /**
     * Create a blank session upon Button click
     *  Belongs to TableView FXML
     *  Handles CRUD create and read operations
     */
    @FXML
    public void createBtnClick() {
        LocalDateTime now = LocalDateTime.now();
        timestamp = Timestamp.valueOf(now);
        sessionDAO.insert(new Session(timestamp, "", "", "00:00", false));
        tableView.getItems().clear();
        sessionsObservableList.addAll(sessionDAO.getAll());
        tableView.setItems(sessionsObservableList);
    }

    /**
     * Deletes a session upon Button click
     *  Belongs to TableView FXML
     *  Handles CRUD delete and read operations
     */
    @FXML
    public void deleteBtnClick() {
        Session selectedSession = tableView.getSelectionModel().getSelectedItem();
        if (selectedSession != null)
        {
            sessionDAO.delete(selectedSession);
            tableView.getItems().clear();
            sessionsObservableList.addAll(sessionDAO.getAll());
            tableView.setItems(sessionsObservableList);
            tableView.getSelectionModel().selectNext();
        }
    }

}










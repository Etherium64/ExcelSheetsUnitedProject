package project.pomodoro.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import project.pomodoro.MainApplication;

/**
 * Controller class for handling button interactions in a Pomodoro timer application.
 * Manages user actions such as starting, pausing, and resetting the timer, as well as
 * switching between the Pomodoro and break views.
 */
public class WorkController {

    /**
     * Application instance used to launch the Pomodoro timer view.
     */
    private MainApplication workScene = new MainApplication();
    private MainApplication restScene = new MainApplication();
    private MainApplication tableScene = new MainApplication();
    private volatile boolean buttonPaused = false;
    private boolean taskIsSet = false;

    @FXML
    private ToggleGroup workGroup;
    @FXML
    private RadioButton rBtn1;
    @FXML
    private RadioButton rBtn2;
    @FXML
    private RadioButton rBtn3;
    @FXML
    private RadioButton rBtn4;
    @FXML
    private Button startPauseBtn;
    /**
     * Reference to the break button UI element, injected by FXML.
     */
    @FXML
    private Button restBtn;
    /**
     * Reference to the break button UI element, injected by FXML.
     */
    @FXML
    private Button resetBtn;
    /**
     * Reference to the break button UI element, injected by FXML.
     */
    @FXML
    private Button tableBtn;


    public void rBtnDisable() {
        rBtn1.setDisable(true);
        rBtn2.setDisable(true);
        rBtn3.setDisable(true);
        rBtn4.setDisable(true);
    }

    /**
     * Handles the click event for the start button.
     * Resumes the timer if it is paused.
     */

    /**
     * Handles the click event for the pause button.
     * Pauses the currently running timer.
     */
    @FXML
    public void startPauseBtnClick() {
        if (!buttonPaused) {
            if (!taskIsSet) {
                RadioButton radioButtonSelected = (RadioButton) workGroup.getSelectedToggle();
                String sessionTask = radioButtonSelected.getText();
                PomodoroController.getPomodoro().recordSession(sessionTask);
                rBtnDisable();
                taskIsSet = true;
            }
            startPauseBtn.setText("Pause");
            buttonPaused = true;
        } else {
            startPauseBtn.setText("Start");
            buttonPaused = false;
        }
        PomodoroController.getPomodoro().runPomodoro();
    }

    /**
     * Handles the click event for the break button.
     * Switches the scene to the break timer view with a 5-minute duration.
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */

    @FXML
    protected void restBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
        }
        Stage workStage = (Stage) restBtn.getScene().getWindow();
        workScene.launchWork(workStage);
    }


    @FXML
    public void resetBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
        }
        Stage restStage = (Stage) resetBtn.getScene().getWindow();
        restScene.launchRest(restStage);
    }

    @FXML
    public void tableBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
        }
        Stage tableStage = (Stage) tableBtn.getScene().getWindow();
        tableScene.launchTable(tableStage);
    }

}









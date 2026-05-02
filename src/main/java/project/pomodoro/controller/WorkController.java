package project.pomodoro.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import project.pomodoro.MainApplication;

/**
 * Controller class for handling button interactions in the Pomodoro Work Session UI.
 * Manages user actions such as starting, pausing, and resetting the timer, as well as
 * switching between the views.
 *
 * @author Minhman Do
 */
public class WorkController {

    /**
     * Application instance used for scene transitions
     */
    private MainApplication newScene = new MainApplication();
    /**
     * Boolean to determine if pause button is active
     */
    private volatile boolean buttonPaused = false;
    /**
     * Boolean to determine if Session task has been set
     */
    private boolean taskIsSet = false;

    /**
     * Radio Button Group for selecting the Session Task
     */
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
    /**
     * Reference to the Start Pause button UI element, injected by FXML.
     */
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
     * Handles the click event for both the start and pause button.
     * Resumes the timer if it is paused, changing button text to "Pause".
     * Else, pauses the currently running timer, changing button text to "Start"
     */
    @FXML
    public void startPauseBtnClick() {
        if (!buttonPaused) {
            //If session Task has not been set yet, extract session Task string from selected RadioButton
            //Record the session, creating a new instance of Session using the Session Task string
            // Disable the radiobuttons after and update TaskIsSet to true
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
     * Handles the click event for the Rest button.
     * Switches the scene to the Rest view  with a 5-minute duration.
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */

    @FXML
    protected void restBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
            buttonPaused = true;
        }
        Stage newStage = (Stage) restBtn.getScene().getWindow();
        newScene.launch(newStage, "rest-view.fxml");
    }
    /**
     * Handles the click event for the Reset button.
     * As resetting the timer directly "stacks" the decrementing Timer,
     * the same function can be achieved if instead a new Scene is created that uses the same FXML  and fields
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */

    @FXML
    public void resetBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
            buttonPaused = true;
        }
        Stage newStage = (Stage) resetBtn.getScene().getWindow();
        newScene.launch(newStage, "work-view.fxml");
    }

    /**
     * Handles the click event for the Session button.
     * Switches the scene to the Table view where CRUD operations take place.
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */
    @FXML
    public void tableBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
            buttonPaused = true;
        }
        Stage newStage = (Stage) tableBtn.getScene().getWindow();
        newScene.launch(newStage, "table-view.fxml");
    }

}









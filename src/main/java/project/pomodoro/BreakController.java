package project.pomodoro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller class for handling button interactions in the Pomodoro Work Session UI.
 * Manages user actions such as starting, pausing, and resetting the timer, as well as
 * switching between the views.
 *
 * @author Minhman Do
 */
public class BreakController {
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
    private ToggleGroup breakGroup;
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
    private Button workBtn;
    /**
     * Reference to the break button UI element, injected by FXML.
     */
    @FXML
    private Button resetBtn;
    /**
     * Reference to the break button UI element, injected by FXML.
     */
    @FXML
    private Button databBtn;
    @FXML
    private Button closeBtn;


    private void rBtnDisable() {
        rBtn1.setDisable(true);
        rBtn2.setDisable(true);
        rBtn3.setDisable(true);
        rBtn4.setDisable(true);
    }

    protected void transition(Button button, String FXMLstring) throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoroController().unfinishedPomodoro();
            taskIsSet = false;
            buttonPaused = true;
        }
        Stage newStage = (Stage) button.getScene().getWindow();
        Pomodoro newPomodoro = new Pomodoro();
        newPomodoro.launch(newStage, FXMLstring);
    }

    /**
     * Handles the click event for both the start and pause button.
     * Resumes the timer if it is paused, changing button text to "Pause".
     * Else, pauses the currently running timer, changing button text to "Start"
     */

    @FXML
    protected void startPauseBtnClick() {
        if (!buttonPaused) {
            if (!taskIsSet) {
                //If session Task has not been set yet, extract session Task string from selected RadioButton
                //Record the session, creating a new instance of Session using the Session Task string
                // Disable the radiobuttons after and update TaskisSet to true
                RadioButton radioButtonSelected = (RadioButton) breakGroup.getSelectedToggle();
                String sessionTask = radioButtonSelected.getText();
                PomodoroController.getPomodoroController().recordSession(sessionTask);
                rBtnDisable();
                taskIsSet = true;
            }
            startPauseBtn.setText("PAUSE");
            startPauseBtn.setBackground(Background.fill(Color.ORANGERED));
            buttonPaused = true;
        } else {
            startPauseBtn.setText("START");
            startPauseBtn.setBackground(Background.fill(Color.LIGHTGREEN));
            buttonPaused = false;
        }
        PomodoroController.getPomodoroController().runPomodoro();
    }

    /**
     * Handles the click event for the Work button.
     * Switches the scene to the Rest view  with a 25-minute duration.
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */
    @FXML
    protected void workBtnClick() throws Exception {
        transition(workBtn, "work-view.fxml");
    }

    /**
     * Handles the click event for the Reset button.
     * As resetting the timer directly "stacks" the decrementing Timer,
     * the same function can be achieved if instead a new Scene is created that uses the same FXML  and fields
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */

    @FXML
    protected void resetBtnClick() throws Exception {
        transition(resetBtn, "break-view.fxml");
    }

    /**
     * Handles the click event for the Session button.
     * Switches the scene to the Table view where CRUD operations take place.
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */
    @FXML
    protected void databBtnClick() throws Exception {
        transition(databBtn, "datab-view.fxml");
    }

    @FXML
    protected void closeBtnClick() {
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

}





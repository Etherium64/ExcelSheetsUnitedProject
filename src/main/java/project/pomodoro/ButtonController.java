package project.pomodoro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/**
 * Controller class for handling button interactions in a Pomodoro timer application.
 * Manages user actions such as starting, pausing, and resetting the timer, as well as
 * switching between the Pomodoro and break views.
 */
public class ButtonController {

    /** Application instance used to launch the Pomodoro timer view. */
    MainApplication pomodoroScene = new MainApplication();

    /** Application instance used to launch the break timer view. */
    MainApplication breakScene = new MainApplication();

    /**
     * Handles the click event for the start button.
     * Resumes the timer if it is paused.
     */
    @FXML
    public void startBtnClick() {
        TimeController.getTimer().startTimer();
    }


    /**
     * Handles the click event for the pause button.
     * Pauses the currently running timer.
     */
    @FXML
    public void pauseBtnClick() {
        TimeController.getTimer().pauseTimer();
    }

    /**
     * Handles the click event for the reset button.
     * Resets the timer to its initial state.
     */
    @FXML
    public void resetBtnClick() {
        TimeController.getTimer().resetTimer();
    }

    /** Reference to the break button UI element, injected by FXML. */
    @FXML
    private Button breakBtn;

    /** Reference to the Pomodoro button UI element, injected by FXML. */
    @FXML
    private Button pomoBtn;

    /**
     * Handles the click event for the Pomodoro button.
     * Switches the scene to the Pomodoro timer view with a 25-minute duration.
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */
    @FXML
    protected void pomoBtnClick() throws Exception {
        Stage pomoStage = (Stage) pomoBtn.getScene().getWindow();
        pomodoroScene.launchScene(pomoStage, "pomodoro-view.fxml", "25:00", 1500);
    }

    /**
     * Handles the click event for the break button.
     * Switches the scene to the break timer view with a 5-minute duration.
     *
     * @throws Exception if there is an error loading the FXML or switching scenes
     */
    @FXML
    protected void breakBtnClick() throws Exception {
        Stage breakStage = (Stage) breakBtn.getScene().getWindow();
        breakScene.launchScene(breakStage, "break-view.fxml", "05:00", 300);
    }
}





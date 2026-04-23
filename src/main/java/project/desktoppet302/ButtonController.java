package project.desktoppet302;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ButtonController {

    // Reference to the "Break" button from the FXML
    @FXML
    private Button breakBtn;

    // Reference to the "Pomodoro" button from the FXML
    @FXML
    private Button pomoBtn;

    // Starts the timer using the shared TimeController instance
    @FXML
    public void startBtnClick() {
        System.out.println("Start clicked");
        TimeController.getTimer().startTimer();
    }

    // Pauses the timer
    @FXML
    public void pauseBtnClick() {
        TimeController.getTimer().pauseTimer();
    }

    // Resets the timer back to its initial value
    @FXML
    public void resetBtnClick() {
        TimeController.getTimer().resetTimer();
    }

    // Triggered when the Pomodoro button is clicked
    @FXML
    protected void pomoBtnClick() {
        try {
            Stage pomoStage = (Stage) pomoBtn.getScene().getWindow();

            SceneManager.launchScene(
                    pomoStage,
                    "/project.desktoppet302/pomodoro-view.fxml",
                    "25:00",
                    1500
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Triggered when the Break button is clicked
    @FXML
    protected void breakBtnClick() {
        try {
            Stage breakStage = (Stage) breakBtn.getScene().getWindow();

            SceneManager.launchScene(
                    breakStage,
                    "/project.desktoppet302/break-view.fxml",
                    "05:00",
                    300
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
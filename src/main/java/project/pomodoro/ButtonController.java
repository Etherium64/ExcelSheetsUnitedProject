package project.pomodoro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ButtonController {

    MainApplication pomodoroScene = new MainApplication();
    MainApplication breakScene = new MainApplication();

    @FXML
    public void startBtnClick() {
        TimeController.getTimer().startTimer();
    }

    @FXML
    public void pauseBtnClick() {
        TimeController.getTimer().pauseTimer();
    }

    @FXML
    public void resetBtnClick() {
        TimeController.getTimer().resetTimer();
    }

    @FXML
    private Button breakBtn;

    @FXML
    private Button pomoBtn;

    @FXML
    protected void pomoBtnClick() throws Exception {
        Stage pomoStage = (Stage) pomoBtn.getScene().getWindow();
        pomodoroScene.launchScene(pomoStage, "pomodoro-view.fxml", "25:00", 1500);
    }

    @FXML
    protected void breakBtnClick() throws Exception {
        Stage breakStage = (Stage) breakBtn.getScene().getWindow();
        breakScene.launchScene(breakStage, "break-view.fxml", "05:00", 300);
    }
}





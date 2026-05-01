package project.pomodoro.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import project.pomodoro.MainApplication;

public class RestController {

    private MainApplication newScene = new MainApplication();
    private volatile boolean buttonPaused = false;
    private boolean taskIsSet = false;

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
    @FXML
    private Button startPauseBtn;
    @FXML
    private Button workBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button tableBtn;


    public void rBtnDisable() {
        rBtn1.setDisable(true);
        rBtn2.setDisable(true);
        rBtn3.setDisable(true);
        rBtn4.setDisable(true);
    }

    @FXML
    public void startPauseBtnClick() {
        if (!buttonPaused) {
            if (!taskIsSet) {
                RadioButton radioButtonSelected = (RadioButton) breakGroup.getSelectedToggle();
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

    @FXML
    protected void workBtnClick() throws Exception {
        ;
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
        }
        Stage workStage = (Stage) workBtn.getScene().getWindow();
        newScene.launch(workStage, "work-view.fxml");
    }

    @FXML
    public void resetBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
        }
        Stage restStage = (Stage) resetBtn.getScene().getWindow();
        newScene.launch(restStage, "rest-view.fxml");
    }

    @FXML
    public void tableBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
        }
        Stage newStage = (Stage) tableBtn.getScene().getWindow();
        newScene.launch(newStage, "table-view.fxml");
    }

}





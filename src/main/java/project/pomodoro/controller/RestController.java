package project.pomodoro.controller;

import AnimationStates.animStates;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.pomodoro.MainApplication;

import java.util.Random;

public class RestController {

    private MainApplication workScene = new MainApplication();
    private MainApplication restScene = new MainApplication();
    private MainApplication tableScene = new MainApplication();

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

    @FXML
    private VBox imagebox;

    @FXML
    private Rectangle2D bounds;

    @FXML
    private ImageView petImage;

    @FXML
    private TranslateTransition move;

    @FXML
    private AnimationTimer moving;

    @FXML
    private long then;

    @FXML
    private animStates petStates;

    // Initialise starting values.
    @FXML
    public void initialize() {
        //Set bounds of screen.
        bounds = Screen.getPrimary().getVisualBounds();
        move = new TranslateTransition();
        move.setNode(imagebox);
        petStates = new animStates();
        this.then = (System.currentTimeMillis());
        this.moving = new AnimationTimer() {
            @Override
            public void handle(long now) {
                now = System.currentTimeMillis();
                if (now - then > 8000) {
                    Random z = new Random();
                    double x = (double) z.nextInt(200) - 100;
                    double y = (double) z.nextInt(200) - 100;
                    move.setDuration(Duration.seconds(2));
                    move.setByX(x);
                    move.setByY(y);
                    if (x > 0) {
                        petStates.setState(animStates.PetState.WALKlEFT);
                        new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                petStates.update();
                                Image pet = petStates.getCurrentFrame();
                                petImage.setImage(pet);
                            }
                        }.start();
                    } else {
                        petStates.setState(animStates.PetState.WALKrIGHT);
                        new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                petStates.update();
                                Image pet = petStates.getCurrentFrame();
                                petImage.setImage(pet);
                            }
                        }.start();
                    }
                    move.play();
                    then = now;
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e -> idling()));
                    timeline.playFromStart();

                }

            }
        };
        Platform.runLater(this::idling);
    }

    @FXML
    protected void idling() {
        petStates.setState(animStates.PetState.IDLE);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                petStates.update();
                Image pet = petStates.getCurrentFrame();
                petImage.setImage(pet);
            }
        }.start();
        moving.start();
    }

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
        workScene.launch(workStage, "/project.pomodoro/work-view.fxml");
    }

    @FXML
    public void resetBtnClick() throws Exception {
        if (taskIsSet) {
            PomodoroController.getPomodoro().unfinishedPomodoro();
            taskIsSet = false;
        }
        Stage restStage = (Stage) resetBtn.getScene().getWindow();
        restScene.launch(restStage, "/project.pomodoro/rest-view.fxml");
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





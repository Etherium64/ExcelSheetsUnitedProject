package project.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project.pomodoro.controller.PomodoroController;

import java.util.Objects;

/**
 * JavaFX Application class responsible for launching and configuring scenes in the Pomodoro timer application.
 * Manages scene creation, stylesheet application, and initial timer setup.
 *
 * @author Minhman Do
 */
public class MainApplication extends Application {

    /**
     * Loads and displays a specified FXML view within a stage, applying styling and initializing the timer display.
     *
     * @param stage The primary stage to set the scene on
     * @throws Exception if the FXML file cannot be loaded or the timer label cannot be found
     */
    public void launch(Stage stage, String FXMLstring) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLstring)));

        // larger scene prevents the pomodoro and database layouts from being cut off
        Scene scene = new Scene(root, 760, 520, Color.TRANSPARENT);
        scene.getStylesheets().add("/styles.css");

        if (FXMLstring.equals("datab-view.fxml")) {
            stage.setTitle("Sessions Database");
        } else {
            stage.setTitle("Pomodoro");

            // set up all the necessary fields to run the Pomodoro Controller
            Label timerLabel = (Label) root.lookup("#timerLabel");
            ProgressBar timerBar = (ProgressBar) root.lookup("#timerBar");
            timerBar.setProgress(1);

            Button startPauseBtn = (Button) root.lookup("#startPauseBtn");
            startPauseBtn.setBackground(Background.fill(Color.LIGHTGREEN));

            String sessionType = FXMLstring.substring(0, 5).replace("-", "");
            PomodoroController.getPomodoro().setPomodoro(sessionType, timerLabel, timerBar, startPauseBtn);
        }

        stage.setMinWidth(760);
        stage.setMinHeight(520);
        stage.setScene(scene);
    }

    /**
     * The main entry point for the JavaFX application.
     *
     * @param stage The primary stage provided by the JavaFX runtime
     * @throws Exception if there is an error loading the FXML or initializing the scene
     */
    @Override
    public void start(Stage stage) throws Exception {
        launch(stage, "work-view.fxml");
        stage.show();
    }
}
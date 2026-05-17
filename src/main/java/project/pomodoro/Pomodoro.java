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
import javafx.stage.StageStyle;
import project.authentication.UserSingleton;

import java.util.Objects;

/**
 * JavaFX Application class responsible for launching and configuring scenes in the Pomodoro timer application.
 * Manages scene creation, stylesheet application, and initial timer setup.
 *
 * @author Minhman Do
 */
public class Pomodoro extends Application {
    /**
     * Loads and displays a specified FXML view within a stage, applying styling and initializing the timer display.
     *
     * @param stage The primary stage to set the scene on
     * @throws Exception if the FXML file cannot be loaded or the timer label cannot be found
     */

    public void launch(Stage stage, String FXMLstring) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLstring)));
        Scene scene = new Scene(root, 600, 300, Color.TRANSPARENT);
        scene.getStylesheets().add("/styles.css");
        root.setStyle("-fx-background-color: transparent;");
        if (FXMLstring == "datab-view.fxml")
        {
            stage.setTitle(UserSingleton.getInstance().getUsername() + "'s Sessions");
        }
        else{
            stage.setTitle("Pomodoro");
            //Set up all the necessary fields to run the Pomodoro Controller if running a Pomodoro Work / Rest Session
            Label timerLabel = (Label) root.lookup("#timerLabel");
            ProgressBar timerBar = (ProgressBar) root.lookup("#timerBar");
            timerBar.setProgress(1);
            Button startPauseBtn = (Button) root.lookup("#startPauseBtn");
            startPauseBtn.setBackground(Background.fill(Color.LIGHTGREEN));
            String sessionType = FXMLstring.substring(0, 5).replace("-", "");
            PomodoroController.getPomodoroController().setPomodoro(sessionType, timerLabel, timerBar, startPauseBtn);
        }
        stage.setScene(scene);
    }

    /**
     * The main entry point for the JavaFX application.
     * Initializes the primary stage with the Pomodoro view, sets the initial time to 25:00,
     * and displays the window.
     *
     * @param stage The primary stage provided by the JavaFX runtime
     * @throws Exception if there is an error loading the FXML or initializing the scene
     */
    @Override
    public void start(Stage stage) throws Exception{
        launch(stage, "work-view.fxml");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}


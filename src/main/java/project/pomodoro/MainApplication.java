package project.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.pomodoro.controller.PomodoroController;
import project.pomodoro.model.SessionDAO;

import java.util.Objects;

/**
 * JavaFX Application class responsible for launching and configuring scenes in the Pomodoro timer application.
 * Manages scene creation, stylesheet application, and initial timer setup.
 */
public class MainApplication extends Application {
    /**
     * Loads and displays a specified FXML view within a stage, applying styling and initializing the timer display.
     *
     * @param stage The primary stage to set the scene on
     * @throws Exception if the FXML file cannot be loaded or the timer label cannot be found
     */
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double sceneSizeX = bounds.getWidth();
    double sceneSizeY = bounds.getHeight();
    public void launch(Stage stage, String FXMLstring) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLstring)));
        if (FXMLstring == "table-view.fxml")
        {
            Scene scene = new Scene(root, sceneSizeX, sceneSizeY);
            scene.getStylesheets().add("/tablestyle.css");
            stage.setTitle("Sessions");
            stage.setScene(scene);
        }
        else{
            Scene scene = new Scene(root, 1080, 720);
            scene.getStylesheets().add("/pomodorostyle.css");
            stage.setTitle("Pomodoro");
            stage.setScene(scene);
            Label timerLabel = (Label) root.lookup("#timerLabel");
            ProgressBar timerBar = (ProgressBar) root.lookup("#timerBar");
            timerBar.setProgress(1);
            Button startPauseBtn = (Button) root.lookup("#startPauseBtn");
            String sessionType = FXMLstring.substring(0, 4);
            PomodoroController.getPomodoro().setPomodoro(sessionType, timerLabel, timerBar, startPauseBtn);
        }
    }

    /**
     * The main entry point for the JavaFX application.
     * Initializes the primary stage with the Pomodoro view, sets the initial time to 25:00,
     * and displays the window.
     *
     * @param primary The primary stage provided by the JavaFX runtime
     * @throws Exception if there is an error loading the FXML or initializing the scene
     */
    @Override
    public void start(Stage stage) throws Exception{
        launch(stage, "work-view.fxml");
        stage.show();
    }
}


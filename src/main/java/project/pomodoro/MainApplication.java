package project.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.pomodoro.controller.PomodoroController;
import project.pomodoro.model.SessionDAO;

import java.util.Objects;

/**
 * JavaFX Application class responsible for launching and configuring scenes in the Pomodoro timer application.
 * Manages scene creation, stylesheet application, and initial timer setup.
 */
public class MainApplication extends Application {
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double sceneSizeX = bounds.getWidth()/1.5;
    double sceneSizeY = bounds.getHeight()/1.5;

    /**
     * Loads and displays a specified FXML view within a stage, applying styling and initializing the timer display.
     *
     * @param stage The primary stage to set the scene on
     * @throws Exception if the FXML file cannot be loaded or the timer label cannot be found
     */
    public void launch(Stage stage, String FXMLstring) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(FXMLstring)));
        String stylesheet = MainApplication.class.getResource("stylesheet.css").toExternalForm();
        Scene scene = new Scene(parent, sceneSizeX, sceneSizeY);
        scene.getStylesheets().add(stylesheet);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Pomodoro");
        stage.setScene(scene);
        Label timerLabel = (Label) parent.lookup("#timerLabel");
        ProgressBar timerBar = (ProgressBar) parent.lookup("#timerBar");
        timerBar.setProgress(1);
        Button startPauseBtn = (Button) parent.lookup("#startPauseBtn");
        String sessionType = FXMLstring.substring(0, 4);
        PomodoroController.getPomodoro().setPomodoro(sessionType, timerLabel, timerBar, startPauseBtn);
    }
    public void launchTable(Stage stage) throws Exception    {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("table-view.fxml")));
        String stylesheet = MainApplication.class.getResource("tablestylesheet.css").toExternalForm();
        Scene scene = new Scene(parent, sceneSizeX, sceneSizeY);
        scene.getStylesheets().add(stylesheet);
        stage.setTitle("Table View");
        stage.setScene(scene);
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
    public void start(Stage primary) throws Exception{
        SessionDAO sessionDAO = new SessionDAO();
        sessionDAO.createTable();
        launch(primary, "work-view.fxml");
        primary.show();

    }
}


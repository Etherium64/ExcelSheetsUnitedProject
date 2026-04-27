package project.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
     * @param FXMLview The path to the FXML file to load (e.g., "pomodoro-view.fxml")
     * @param timeText The initial time string to display in the timer label (e.g., "25:00")
     * @param timeElapsed The initial timer duration in seconds (e.g., 1500 for 25 minutes)
     * @throws Exception if the FXML file cannot be loaded or the timer label cannot be found
     */
    public void launchScene(Stage stage, String FXMLview, String timeText, int timeElapsed ) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(FXMLview)));
        Scene scene = new Scene(parent, 1080, 720);
        stage.setTitle("Pomodoro");
        String stylesheet = MainApplication.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);

        Label timerLabel = (Label) parent.lookup("#timerLabel");
        timerLabel.setText(timeText);
        TimeController.getTimer().setupTimer(timerLabel, timeElapsed);
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
    public void start(Stage primary) throws Exception {
        launchScene(primary, "pomodoro-view.fxml", "25:00", 1500);
        primary.show();
    }
}


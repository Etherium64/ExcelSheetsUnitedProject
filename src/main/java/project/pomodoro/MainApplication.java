package project.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;
import javafx.geometry.Rectangle2D;

import java.util.Objects;

public class MainApplication extends Application {
    Rectangle2D fullScreen = Screen.getPrimary().getVisualBounds();

    public void launchScene(Stage stage, String FXMLview, String timeText, int timeElapsed ) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(FXMLview)));
        String stylesheet = MainApplication.class.getResource("stylesheet.css").toExternalForm();

        Scene scene = new Scene(parent, fullScreen.getWidth(), fullScreen.getHeight());
        scene.getStylesheets().add(stylesheet);
        stage.setTitle("Pomodoro");

        Label timerLabel = (Label) parent.lookup("#timerLabel");
        timerLabel.setText(timeText);
        ProgressBar timerBar = (ProgressBar) parent.lookup("#pB");
        Button switchBtn = (Button) parent.lookup("#switchBtn");
        TimeController.getTimer().setupTimer(timerLabel, timerBar, switchBtn, timeElapsed);

        stage.setScene(scene);
    }
    @Override
    public void start(Stage primary) throws Exception {
        launchScene(primary, "pomodoro-view.fxml", "25:00", 1500);
        primary.show();
    }
}

// Animated creature is idling somehwere... for Timer and Score screens
// Data base
// Score screen
// Database for Score
// Currency, link with Ethans?
// Documentation
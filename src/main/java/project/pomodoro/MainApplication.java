package project.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApplication extends Application {
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

    @Override
    public void start(Stage primary) throws Exception {
        launchScene(primary, "pomodoro-view.fxml", "25:00", 1500);
        primary.show();
    }
}


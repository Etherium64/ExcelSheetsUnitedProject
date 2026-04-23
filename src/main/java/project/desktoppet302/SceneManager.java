package project.desktoppet302;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class SceneManager {

    public static void launchScene(Stage stage, String fxmlView, String timeText, int timeElapsed) throws Exception {

        Parent parent = FXMLLoader.load(
                Objects.requireNonNull(SceneManager.class.getResource(fxmlView))
        );

        Scene scene = new Scene(parent, 1080, 720);
        stage.setTitle("Pomodoro");

        URL stylesheet = SceneManager.class.getResource("/project.desktoppet302/stylesheet.css");

        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        } else {
            System.out.println("stylesheet.css not found, continuing without CSS");
        }

        stage.setScene(scene);

        Label timerLabel = (Label) parent.lookup("#timerLabel");

        if (timerLabel == null) {
            throw new IllegalStateException("Could not find timerLabel in " + fxmlView);
        }

        timerLabel.setText(timeText);
        TimeController.getTimer().setupTimer(timerLabel, timeElapsed);
    }
}
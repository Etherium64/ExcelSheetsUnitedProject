package project.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SceneController extends Application {
    public static final String TITLE = "Pomodoro";
    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;
    private Label timerLabel;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("pomodoro-view.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle(TITLE);
        String stylesheet = SceneController.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
        stage.show();

        timerLabel = (Label) root.lookup("#timerLabel");
        timerLabel.setText("25:00");
        TimeController.getTimer().setLabel(timerLabel);
    }
}



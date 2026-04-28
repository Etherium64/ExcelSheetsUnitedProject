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
import project.pomodoro.controller.PomodoroController;
import project.pomodoro.model.SessionDAO;

import java.util.Objects;

public class MainApplication extends Application {
    Rectangle2D fullScreen = Screen.getPrimary().getVisualBounds();

    public void launch(Stage stage, String FXMLstring) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource(FXMLstring)));
        String stylesheet = MainApplication.class.getResource("stylesheet.css").toExternalForm();
        Scene scene = new Scene(parent, fullScreen.getWidth(), fullScreen.getHeight());
        scene.getStylesheets().add(stylesheet);
        stage.setTitle("Pomodoro");
        stage.setScene(scene);
        Label timerLabel = (Label) parent.lookup("#timerLabel");
        ProgressBar timerBar = (ProgressBar) parent.lookup("#timerBar");
        timerBar.setProgress(1);
        Button startPauseBtn = (Button) parent.lookup("#startPauseBtn");
        String sessionType = FXMLstring.substring(0, 4);
        PomodoroController.getPomodoro().setPomodoro(sessionType, timerLabel, timerBar, startPauseBtn);
    }

    public void launchTable(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("table-view.fxml")));
        String stylesheet = MainApplication.class.getResource("tablestylesheet.css").toExternalForm();
        Scene scene = new Scene(parent, fullScreen.getWidth(), fullScreen.getHeight());
        scene.getStylesheets().add(stylesheet);
        stage.setTitle("Table View");
        stage.setScene(scene);
    }

    @Override
    public void start(Stage primary) throws Exception{
        SessionDAO sessionDAO = new SessionDAO();
        sessionDAO.createTable();
        launch(primary, "work-view.fxml");
        primary.show();

    }
}

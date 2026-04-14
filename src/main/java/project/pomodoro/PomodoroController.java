package project.pomodoro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PomodoroController {

    @FXML
    private Button breakBtn;

    @FXML
    private Button pomoBtn;

    @FXML
    protected void pomoBtnClick() throws IOException {
        Stage stage = (Stage) pomoBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(PomodoroApplication.class.getResource("pomodoro-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), PomodoroApplication.WIDTH, PomodoroApplication.HEIGHT);
        String stylesheet = PomodoroApplication.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
    }

    @FXML
    protected void breakBtnClick() throws IOException {
        Stage stage = (Stage) breakBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(PomodoroApplication.class.getResource("break-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), PomodoroApplication.WIDTH, PomodoroApplication.HEIGHT);
        String stylesheet = PomodoroApplication.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
    }
}



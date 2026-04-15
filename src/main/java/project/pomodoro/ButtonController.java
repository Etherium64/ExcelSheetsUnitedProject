package project.pomodoro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;



public class ButtonController {
    @FXML
    public void startBtnClick() {
        TimeController.getTimer().startTimer();
    }

    @FXML
    private Button breakBtn;

    @FXML
    private Button pomoBtn;

    @FXML
    protected void pomoBtnClick() throws IOException {
        Stage stage = (Stage) pomoBtn.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("break-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), SceneController.WIDTH, SceneController.HEIGHT);
        String stylesheet = SceneController.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);

    }

    @FXML
    protected void breakBtnClick() throws IOException {
        Stage stage = (Stage) breakBtn.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("break-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), SceneController.WIDTH, SceneController.HEIGHT);
        String stylesheet = SceneController.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setScene(scene);
    }
}




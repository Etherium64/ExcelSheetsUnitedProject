package project.authentication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project.authentication/login-view.fxml")));
        Scene scene = new Scene(root, 640, 640);
        scene.getStylesheets().add("/styles.css");
        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}




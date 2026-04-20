package project.Trivia;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.Trivia.dao.DatabaseInitialiser;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Initialize database tables
        DatabaseInitialiser.init();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project.trivia/main.fxml")));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("/styles.css");
        stage.setTitle("Trivia Game");
        stage.setScene(scene);
        stage.show();
    }

}

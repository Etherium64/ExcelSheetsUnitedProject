package project.Trivia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project.Trivia.dao.DatabaseInitialiser;

import java.util.Objects;

/**
 * Entry point for the JavaFX Trivia Game application.
 * <p>
 * Initializes the database schema via {@link DatabaseInitialiser#init()}
 * and loads the main user interface from FXML. This class extends
 * {@link Application} and follows the standard JavaFX lifecycle.
 * </p>
 *
 * @author Ethan B
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     *
     * @param stage the primary stage for this application, provided by the JavaFX runtime
     * @throws Exception if the FXML or CSS resources cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitialiser.init();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project.trivia/main.fxml")));

        // larger scene prevents trivia content from being cut off
        Scene scene = new Scene(root, 520, 420, Color.TRANSPARENT);
        scene.getStylesheets().add("/styles.css");

        stage.setTitle("Trivia Game");
        stage.setMinWidth(520);
        stage.setMinHeight(420);
        stage.setScene(scene);
        stage.show();
    }
}
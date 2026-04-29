package project.Trivia;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
     * <p>
     * This method is called after the {@code init()} phase, is responsible for
     * setting up the primary stage, loading the UI from {@code main.fxml},
     * applying styles, and displaying the window.
     * </p>
     *
     * @param stage the primary stage for this application, provided by the JavaFX runtime
     * @throws Exception if the FXML or CSS resources cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitialiser.init();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project.trivia/main.fxml")));

        // Make the scene and stage transparent
        Scene scene = new Scene(root, 300, 200, Color.TRANSPARENT);
        scene.getStylesheets().add("/styles.css");

        // Ensure the root node doesn't have a default background
        root.setStyle("-fx-background-color: transparent;");

        stage.setTitle("Trivia Game");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

}

package project.Trivia;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
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
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double sceneSizeX = bounds.getWidth()/2;
        double sceneSizeY = bounds.getHeight()/2;

        // Initialize database tables
        DatabaseInitialiser.init();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project.trivia/main.fxml")));
        Scene scene = new Scene(root, sceneSizeX, sceneSizeY);
        scene.getStylesheets().add("/styles.css");
        stage.setTitle("Trivia Game");
        stage.setScene(scene);
        stage.show();
    }

}

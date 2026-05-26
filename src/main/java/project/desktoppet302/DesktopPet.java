package project.desktoppet302;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * A JavaFX application that displays a draggable desktop pet on the screen.
 * The pet appears as a transparent window containing an image, locked to the bottom
 * of the primary display. Users can drag the pet horizontally across the screen,
 * but its movement is constrained to stay within the screen bounds.
 */
public class DesktopPet extends Application {

    /**
     * Visual bounds of the screen.
     * getVisualBounds excludes the taskbar, so the pet window stays above it.
     */
    private final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    /**
     * Initializes and displays the desktop pet window when the application starts.
     *
     * @param stage The primary stage for this application, onto which the scene is set.
     * @throws IOException If the pet image resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {

        // load the desktop pet fxml
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/project.desktoppet302/pet-view.fxml")
        );

        // create a transparent scene
        Scene scene = new Scene(fxmlLoader.load(), Color.TRANSPARENT);

        // set the scene on the stage
        stage.setScene(scene);
        stage.setTitle("Desktop Pet");

        // make the transparent window full usable screen width
        stage.setWidth(bounds.getWidth());

        // scale the pet window based on usable screen height, but keep it within sensible limits
        double petWindowHeight = clamp(bounds.getHeight() * 0.25, 220, 320);
        stage.setHeight(petWindowHeight);

        // lock the window to the bottom of the usable screen, above the taskbar
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMaxY() - stage.getHeight());

        // remove normal window borders
        stage.initStyle(StageStyle.TRANSPARENT);

        // keep the pet above other windows
        stage.setAlwaysOnTop(true);

        // show the window
        stage.show();
    }

    /**
     * Keeps a value between a minimum and maximum amount.
     *
     * @param value the value to clamp
     * @param min the lowest allowed value
     * @param max the highest allowed value
     * @return the clamped value
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
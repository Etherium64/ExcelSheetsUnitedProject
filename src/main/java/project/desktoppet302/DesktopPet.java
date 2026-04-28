package project.desktoppet302;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import java.io.IOException;
import java.net.URL;

/**
 * A JavaFX application that displays a draggable desktop pet on the screen.
 * The pet appears as a transparent window containing an image, locked to the bottom
 * of the primary display. Users can drag the pet horizontally across the screen,
 * but its movement is constrained to stay within the screen bounds.
 */
public class DesktopPet extends Application {

    /**
     * Stores the x-coordinate offset between the pet's window and the point
     * where the user initially clicked to begin dragging. Used to ensure smooth dragging
     * and prevent the pet from jumping to the mouse position.
     */
    double dragOffsetX;

    // Sets size of window which holds the pet
    Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    double sceneSizeX = bounds.getWidth()/2;
    double sceneSizeY = bounds.getHeight()/2;


    /**
     * Initializes and displays the desktop pet window when the application starts.
     * Sets up the pet image from the resources, configures the transparent window,
     * positions it at the bottom of the screen, and enables horizontal dragging
     * with screen edge constraints.
     *
     * @param stage The primary stage for this application, onto which the scene is set.
     * @throws IOException If the pet image resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {

        //Load FXML page for desktop pet.
        FXMLLoader fxmlLoader = new FXMLLoader(DesktopPet.class.getResource("pet-view.fxml"));
        // Create window to hold canvas.
        Scene scene = new Scene(fxmlLoader.load(), sceneSizeX, sceneSizeY);
        stage.setScene(scene);
        stage.setTitle("Desktop Pet");
        stage.setAlwaysOnTop(true);
        // Show window.
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
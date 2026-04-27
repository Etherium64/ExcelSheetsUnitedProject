package project.desktoppet302;

import javafx.application.Application;
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

    /**
     * The width of the application window (scene) that contains the pet image.
     * Default value is 125 pixels.
     */
    double sceneSizeX = 125;

    /**
     * The height of the application window (scene) that contains the pet image.
     * Default value is 125 pixels.
     */
    double sceneSizeY = 125;

    /**
     * The displayed width of the pet image within the window. The height is scaled
     * proportionally to preserve the image's aspect ratio. Default value is 115 pixels.
     */
    double petSize = 115;

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

        // Find image URL inside resources folder
        URL imageFile = getClass().getResource("/pet.png");

        // Create and load image into JavaFx
        assert imageFile != null;
        Image image = new Image(imageFile.toExternalForm());
        ImageView petImage = new ImageView(image);

        // Set image size and keep aspect ratio
        petImage.setFitWidth(petSize);
        petImage.setPreserveRatio(true);

        // Create canvas to hold the image
        HBox canvas = new HBox(petImage);
        canvas.setStyle("-fx-background-color: transparent;");

        // Create transparent window to hold canvas
        Scene scene = new Scene(canvas, sceneSizeX, sceneSizeY, Color.TRANSPARENT);
        stage.setScene(scene);

        // Remove top bar/borders and keep window in front
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        // Show window so stage height is properly known
        stage.show();

        // get screen bounds and lock pet to bottom of screen
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setY(bounds.getMaxY() - stage.getHeight());

        // store where on the pet the mouse was pressed
        petImage.setOnMousePressed(mousePress -> {
            dragOffsetX = mousePress.getSceneX();
        });

        // Only move on x-axis
        petImage.setOnMouseDragged(mouseDrag -> {

            // Get the X position based on mouse movement
            double mouseX = mouseDrag.getScreenX() - dragOffsetX;

            // Find left edge of screen
            double leftScreenEdge = bounds.getMinX();

            // Find right edge of screen minus the pets width
            double rightScreenEdge = bounds.getMaxX() - stage.getWidth();

            // Clamp X-axis so pet cannot go off sides of screen
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }

            // Apply clamped X position to image
            stage.setX(mouseX);
        });
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
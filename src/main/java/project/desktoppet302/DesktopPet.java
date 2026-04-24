package org.example.desktoppet302;

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

public class DesktopPet extends Application {

    // Stores x coordinate of where the mouse first clicked on the pet
    double dragOffsetX;

    // Sets size of window which holds the pet
    double sceneSizeX = 125;
    double sceneSizeY = 125;

    // Sets size of pet image
    double petSize = 115;

    @Override
    public void start(Stage stage) throws IOException {

        // Find image URL inside resources folder
        URL imageFile = getClass().getResource("/pet.png");

        // Create and load image into JavaFx
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

            isDragging = true;

            double mouseX = mouseDrag.getScreenX() - dragOffsetX;

            // Screen boundaries
            double leftScreenEdge = bounds.getMinX();
            double rightScreenEdge = bounds.getMaxX() - petStage.getWidth();

            // Clamp movement within screen
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

    public static void main(String[] args) {
        launch(args);
    }
}
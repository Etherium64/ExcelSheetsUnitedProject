package org.example.desktoppet302;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class PetController {

    @FXML
    private double dragOffsetX;

    @FXML
    private HBox canvas;

    @FXML
    private Rectangle2D bounds;

    @FXML
    public Stage stage;

    @FXML
    private ImageView petImage;

    @FXML
    private Image pet;

    @FXML
    public double mouse;

    // Initialise starting values.
    @FXML
    public void initialize() {
        //Set bounds of screen.
        bounds = Screen.getPrimary().getVisualBounds();

    }

    // State change when animal is clicked.
    @FXML
    protected void onImageClick() {
        var timeline =
                new Timeline(
                        new KeyFrame(Duration.seconds(0), p -> petImage.setFitHeight(50)),
                        new KeyFrame(Duration.seconds(0.5), p -> petImage.setFitHeight(100)));
        timeline.playFromStart();
    }

    @FXML
    protected void onDragExit() {
        var timeline =
                new Timeline(
                        new KeyFrame(Duration.seconds(0), p -> petImage.setRotate(180)),
                        new KeyFrame(Duration.seconds(0.5), p -> petImage.setRotate(0)));
        timeline.playFromStart();

    }

    @FXML
    protected void onImageDrag() {

        petImage.setOnMousePressed(mousePress -> {
            dragOffsetX = mousePress.getSceneX();
        });
        petImage.setOnMouseDragged(mouseDrag -> {
            // Get the X position based on mouse movement
            double mouseX = mouseDrag.getScreenX() - dragOffsetX;
            // Find left edge of screen
            double leftScreenEdge = bounds.getMinX();
            // Find right edge of screen minus the pets width
            double rightScreenEdge = bounds.getMaxX() - petImage.getFitWidth();
            // Clamp X-axis so pet cannot go off sides of screen
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }


            // Apply clamped X position to image


            petImage.setTranslateX(mouseX);

            Parent root = petImage.getParent();

            //Creating a scene object
            Scene scene = new Scene(root, 125, 125);

            //Setting title to the Stage
            stage.setTitle("Multiple transformations");

            //Adding scene to the stage
            stage.setScene(scene);

            //Displaying the contents of the stage
            stage.show();

        });

    }
}

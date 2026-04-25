package org.example.desktoppet302;

import javafx.animation.KeyFrame;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    private double dragOffsetY;

    @FXML
    private HBox canvas;

    @FXML
    private Rectangle2D bounds;

    @FXML
    private double mouseX;

    @FXML
    private double mouseY;

    @FXML
    private Stage stage;

    @FXML
    private ImageView petImage;

    @FXML
    private Image pet;

    @FXML
    private TranslateTransition move;

    // Initialise starting values.
    @FXML
    public void initialize() {
        //Set bounds of screen.
        bounds = Screen.getPrimary().getVisualBounds();
        move = new TranslateTransition();
        move.setNode(petImage);
    }



    // State change when animal is clicked.
    @FXML
    protected void onImageClick() {

        move.setDuration(Duration.seconds(3));
        move.setByX(500);
        move.play();
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
    protected void onImageDrag() throws InterruptedException {
        // Get current stage on window.
        stage = (Stage) canvas.getScene().getWindow();
        petImage.setOnMouseDragged(mouseDrag -> {
            // Get the X position based on mouse movement
            mouseX = mouseDrag.getSceneX();
            // Get the Y position based on mouse movement
            mouseY = mouseDrag.getSceneY();
            // Find left edge of screen
            double leftScreenEdge = 0;
            // Find right edge of screen minus the pets width
            double rightScreenEdge = stage.getScene().getWidth() - petImage.getFitWidth();
            // Find left edge of screen
            double topScreenEdge = 0;
            // Find right edge of screen minus the pets width
            double bottomScreenEdge = stage.getScene().getHeight() - petImage.getFitHeight();
            // Clamp X-axis so pet cannot go off sides of screen
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }
            // Clamp X-axis so pet cannot go off sides of screen
            if (mouseY < topScreenEdge) {
                mouseY = topScreenEdge;
            }
            if (mouseY > bottomScreenEdge) {
                mouseY = bottomScreenEdge;
            }
            // Apply clamped X position to image
            petImage.setTranslateX(mouseX);
            petImage.setTranslateY(mouseY);
        });
    }




//    public Stage getStage() {
//        return stage;
//    }
//
//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }
}




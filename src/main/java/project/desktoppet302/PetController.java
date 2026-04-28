package project.desktoppet302;

import javafx.animation.KeyFrame;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

    @FXML
    private Timer timer;

    @FXML
    private TimerTask moving;

    // Initialise starting values.
    @FXML
    public void initialize() {
        //Set bounds of screen.
        bounds = Screen.getPrimary().getVisualBounds();
        move = new TranslateTransition();
        move.setNode(petImage);
        timer  = new Timer();

        Platform.runLater(this::idling);

    }



    @FXML
    protected void idling() {
        Random z = new Random();
        double x = z.nextDouble(-100, 100);
        double y = z.nextDouble(-100, 100);
        TimerTask moving = new TimerTask() {
            @Override
            public void run() {
                move = new TranslateTransition();
                move.setNode(petImage);
                move.setDuration(Duration.seconds(3));
                move.setByX(x);
                move.setByY(y);
                move.play();
                move.setDuration(Duration.seconds(3));
                move.play();
            }
        };
        timer.schedule(moving, 3000);


    }

    // State change when animal is clicked.
//    @FXML
//    protected void onImageClick() {
//        move = new TranslateTransition();
//        move.setNode(petImage);
//        move.setDuration(Duration.seconds(3));
//        move.setByX(100);
//        move.play();
//        var timeline =
//                new Timeline(
//                        new KeyFrame(Duration.seconds(0), p -> petImage.setRotate(180)),
//                        new KeyFrame(Duration.seconds(0.5), p -> petImage.setRotate(0)));
//        timeline.playFromStart();
//        idling();
//    }
//
//    @FXML
//    protected void onDragExit() {
//        var timeline =
//                new Timeline(
//                        new KeyFrame(Duration.seconds(0), p -> petImage.setRotate(180)),
//                        new KeyFrame(Duration.seconds(0.5), p -> petImage.setRotate(0)));
//        timeline.playFromStart();
//    }

    @FXML
    protected void onImageDrag() throws InterruptedException {
        move.pause();
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
        idling();
    }




//    public Stage getStage() {
//        return stage;
//    }
//
//    public void setStage(Stage stage) {
//        this.stage = stage;
//    }
}




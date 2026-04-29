package project.desktoppet302;

import AnimationStates.animStates;
import javafx.animation.KeyFrame;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Time;
import java.util.Random;
import java.util.Random.*;

public class PetController {

    @FXML
    private double dragOffsetX;

    @FXML
    private double dragOffsetY;

    @FXML
    private HBox box;

    @FXML
    private Text pettext;

    @FXML
    private Button yesbutton;

    @FXML
    private Button nobutton;

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
    private VBox imagebox;

    @FXML
    private TranslateTransition move;

    @FXML
    private AnimationTimer moving;

    @FXML
    private long then;

//    @FXML
//    private long now;

    @FXML
    private animStates petStates;

    // Initialise starting values.
    @FXML
    public void initialize() {
        //Set bounds of screen.
        bounds = Screen.getPrimary().getVisualBounds();
        move = new TranslateTransition();
        move.setNode(imagebox);
        petStates = new animStates();
        this.then = (System.currentTimeMillis());
        this.moving = new AnimationTimer() {
            @Override
            public void handle(long now) {
                now = System.currentTimeMillis();
                if (now - then > 8000) {
                    Random z = new Random();
                    double x = (double) z.nextInt(200) - 100;
                    double y = (double) z.nextInt(200) - 100;
                    move.setDuration(Duration.seconds(2));
                    move.setByX(x);
                    move.setByY(y);
                    move.play();
                    then = now;
                }

            }
        };

        Platform.runLater(this::idling);

    }



    @FXML
    protected void idling() {
        petStates.setState(animStates.PetState.IDLE);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                petStates.update();
                Image pet = petStates.getCurrentFrame();
                petImage.setImage(pet);
            }
        }.start();
        moving.start();

    }

    // State change when animal is clicked.
    @FXML
    protected void onImageClick() throws InterruptedException {
        moving.stop();
        petStates.setState(animStates.PetState.JUMP);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                petStates.update();
                Image pet = petStates.getCurrentFrame();
                petImage.setImage(pet);
            }
        }.start();
        then = System.currentTimeMillis();
        idling();
    }

    @FXML
    protected void onDragExit() {
        petStates.setState(animStates.PetState.JUMP);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                petStates.update();
                Image pet = petStates.getCurrentFrame();
                petImage.setImage(pet);
            }
        }.start();
        idling();
    }

    @FXML
    protected void onImageDrag() throws InterruptedException {
        moving.stop();
        // Get current stage on window.
        stage = (Stage) box.getScene().getWindow();
        petImage.setOnMouseDragged(mouseDrag -> {
            // Get the X position based on mouse movement
            mouseX = mouseDrag.getSceneX() - petImage.getFitWidth()/2;
            // Get the Y position based on mouse movement
            mouseY = mouseDrag.getSceneY() - petImage.getFitHeight()/2;
            // Find left edge of screen
            double leftScreenEdge = -75;
            // Find right edge of screen minus the pets width
            double rightScreenEdge = stage.getScene().getWidth() - petImage.getFitWidth() + 75;
            // Find left edge of screen
            double topScreenEdge = -50;
            // Find right edge of screen minus the pets width
            double bottomScreenEdge = stage.getScene().getHeight() - petImage.getFitHeight() + 75;
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
            imagebox.setTranslateX(mouseX);
            imagebox.setTranslateY(mouseY);
        });
        idling();
    }

    @FXML
    protected void timerButton() {
        pettext.setText("NEVER KYS");
        pettext.setVisible(true);
    }

    private boolean isTriviaPrompt = false;

    @FXML
    protected void triviaButton() {
        pettext.setText("Do you want to play trivia with me?");
        pettext.setVisible(true);
        yesbutton.setVisible(true);
        nobutton.setVisible(true);
        isTriviaPrompt = true; // Set the state
    }

    @FXML
    private void goButton() {
        if (isTriviaPrompt) {
            // Launch trivia game
            try {
                Stage triviaStage = new Stage();
                new project.Trivia.Main().start(triviaStage);
                triviaStage.setAlwaysOnTop(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Reset
            isTriviaPrompt = false;
            pettext.setVisible(false);
            yesbutton.setVisible(false);
            nobutton.setVisible(false);
        }
        // Add else-if blocks for other prompts if needed
    }

    @FXML
    protected void returnButton() {
        pettext.setText("KYS");
        pettext.setVisible(false);
        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }



}




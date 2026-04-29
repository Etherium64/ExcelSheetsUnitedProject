package project.desktoppet302;

import AnimationStates.animStates;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.Trivia.dao.DatabaseInitialiser;

import java.util.Objects;

/**
 * Main Desktop Pet application.
 *
 * Handles:
 * - Rendering animated pet
 * - Dragging behaviour
 * - Click interaction
 * - Feature menu (Pomodoro + Trivia)
 * - Positioning auxiliary windows relative to the pet
 */
public class DesktopPet extends Application {

    // Stores where the mouse was clicked relative to the pet (for dragging)
    private double dragOffsetX;

    // Used to distinguish between click and drag
    private boolean isDragging = false;


    // Window size for the pet container
    private double sceneSizeX = 500;
    private double sceneSizeY = 320;

    // Size of the pet image (scaled animation frames)
    private double petSize = 480;





    // Handles animation states (idle, walk, jump, etc.)
    private animStates petStates;

    // Separate stages for different features
    private Stage pomStage;
    private Stage triviaStage;
    private Stage featureMenuStage;

    @Override
    public void start(Stage petStage) {

        // Get screen bounds (used for positioning and clamping)
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        // Initialise animation system and set default state
        petStates = new animStates();
        petStates.setState(animStates.PetState.IDLE);

        // ImageView used to display animation frames
        ImageView petImage = new ImageView();
        petImage.setFitWidth(petSize);
        petImage.setPreserveRatio(true);

        // Transparent container for the pet
        HBox canvas = new HBox(petImage);
        canvas.setStyle("-fx-background-color: transparent;");

        // Transparent scene
        Scene scene = new Scene(canvas, sceneSizeX, sceneSizeY, Color.TRANSPARENT);
        petStage.setScene(scene);

        // Remove window decorations and keep on top
        petStage.initStyle(StageStyle.TRANSPARENT);
        petStage.setAlwaysOnTop(true);
        petStage.show();

        // Lock pet to bottom of screen
        petStage.setY(bounds.getMaxY() - petStage.getHeight());



        // Main animation loop (runs every frame)
        AnimationTimer animationLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {

                // Update animation state machine
                petStates.update();

                // Get current frame
                Image frame = petStates.getCurrentFrame();

                // Apply frame if valid
                if (frame != null) {
                    petImage.setImage(frame);
                }
            }
        };

        animationLoop.start();


        // When mouse is pressed, store offset and assume click initially
        petImage.setOnMousePressed(mousePress -> {
            dragOffsetX = mousePress.getSceneX();
            isDragging = false;
        });

        // Handle dragging (horizontal only)
        petImage.setOnMouseDragged(mouseDrag -> {

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


            // Change animation based on direction
            if (mouseX > petStage.getX()) {
                petStates.setState(animStates.PetState.WALKlEFT);
            } else if (mouseX < petStage.getX()) {
                petStates.setState(animStates.PetState.WALKrIGHT);
            }

            // Apply new position
            petStage.setX(mouseX);
            petStage.setY(bounds.getMaxY() - petStage.getHeight());

            // Keep all child windows aligned with the pet
            if (pomStage != null && pomStage.isShowing()) {
                positionStageAbovePet(petStage, pomStage, bounds);
            }

            if (triviaStage != null && triviaStage.isShowing()) {
                positionStageAbovePet(petStage, triviaStage, bounds);
            }

            if (featureMenuStage != null && featureMenuStage.isShowing()) {
                positionStageAbovePet(petStage, featureMenuStage, bounds);
            }
        });

        // Handle click vs drag
        petImage.setOnMouseReleased(mouseRelease -> {
            if (!isDragging) {
                onPetClicked(petStage);
            } else {
                // Return to idle after dragging
                petStates.setState(animStates.PetState.IDLE);
            }
        });
    }

    /**
     * Triggered when the pet is clicked (not dragged).
     * Opens the feature menu above the pet.
     */
    private void onPetClicked(Stage petStage) {

        System.out.println("Pet clicked!");

        // Play jump animation on click
        petStates.setState(animStates.PetState.JUMP);

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        // Create menu only once
        if (featureMenuStage == null) {
            featureMenuStage = new Stage();

            Button pomodoroButton = new Button("Pomodoro");
            Button triviaButton = new Button("Trivia");

            // Open Pomodoro feature
            pomodoroButton.setOnAction(event -> {
                openPomodoro(petStage);
                featureMenuStage.hide();
            });

            // Open Trivia feature
            triviaButton.setOnAction(event -> {
                openTrivia(petStage);
                featureMenuStage.hide();
            });

            // Vertical menu layout
            VBox menuBox = new VBox(10, pomodoroButton, triviaButton);

            // Basic styling
            menuBox.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-padding: 15;" +
                            "-fx-border-color: black;" +
                            "-fx-border-width: 1;"
            );

            Scene menuScene = new Scene(menuBox, 180, 120);

            featureMenuStage.setScene(menuScene);
            featureMenuStage.setTitle("Pet Menu");
            featureMenuStage.setAlwaysOnTop(true);
        }

        featureMenuStage.show();
        positionStageAbovePet(petStage, featureMenuStage, bounds);
    }

    /**
     * Opens the Pomodoro window.
     */

    private void openPomodoro(Stage petStage) {
        try {
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            if (pomStage == null) {
                pomStage = new Stage();
                SceneManager.launchScene(
                        pomStage,
                        "/project.desktoppet302/pomodoro-view.fxml",
                        "25:00",
                        1500
                );
                pomStage.setAlwaysOnTop(true);
                pomStage.show();
            } else if (!pomStage.isShowing()) {
                pomStage.show();
            }

            positionStageAbovePet(petStage, pomStage, bounds);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Positions a window directly above the pet.
     * Ensures it stays within screen bounds.
     */
    private void positionStageAbovePet(Stage petStage, Stage targetStage, Rectangle2D bounds) {

        double desiredX = petStage.getX() + (petStage.getWidth() / 2)
                - (targetStage.getWidth() / 2);

        double desiredY = petStage.getY() - targetStage.getHeight();

        double leftScreenEdge = bounds.getMinX();
        double rightScreenEdge = bounds.getMaxX() - targetStage.getWidth();

        // Clamp horizontally
        if (desiredX < leftScreenEdge) {
            desiredX = leftScreenEdge;
        }

        if (desiredX > rightScreenEdge) {
            desiredX = rightScreenEdge;
        }

        // Clamp vertically (top)
        if (desiredY < bounds.getMinY()) {
            desiredY = bounds.getMinY();
        }

        targetStage.setX(desiredX);
        targetStage.setY(desiredY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
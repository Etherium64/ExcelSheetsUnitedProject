package project.desktoppet302;

import AnimationStates.animStates;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller class for all interactions for the menu and the companion pet.
 * Includes clicking and dragging the pet, idle movement and animation changes for the pet,
 * and initiation of pomodoro and trivia applications.
 *
 * @author Jacob Farrell
 */
public class PetController {


    @FXML public Button trivia;
    @FXML public Button timer;
    /**
     * Image of the pet displayed in fxml.
     */
    @FXML public Image pet;
    /**
     * Box for the pet and menu in the application in fxml.
     */
    @FXML private HBox hbox;
    /**
     * Box for the pet and an associated text box and buttons in fxml.
     */
    @FXML private VBox imageBox;

    @FXML private HBox optionsBox;

    @FXML private HBox promptButtons;
    /**
     * Text displayed above the pet in fxml.
     */
    @FXML private Text petText;
    /**
     * Text field displayed above the pet in fxml.
     */
    @FXML private TextField textField;
    /**
     * Yes button in fxml for answering the pet.
     */
    @FXML private Button yesbutton;
    /**
     * No button in fxml for answering the pet.
     */
    @FXML private Button nobutton;
    /**
     * ImageView of the pet in fxml.
     */
    @FXML private ImageView petImage;
    /**
     * Value representing the bounds of the screen.
     */
    private final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
    /**
     * Translation transition for the pet for movement.
     */
    private final TranslateTransition movePet = new TranslateTransition();
    /**
     * Animation timer for pet when idle.
     */
    private AnimationTimer idlePet;
    /**
     * Long number for recording time between pet movement.
     */
    private long then;
    /**
     * Animation state for the pet.
     */
    private final animStates petStates = new animStates();
    /**
     * Pet object representing the pet in the application.
     */
    private Pet desktopPet;
    /**
     * Boolean to determine if the trivia button was clicked.
     */
    private boolean isTriviaPrompt = false;
    /**
     * Boolean to determine the pomodoro button was clicked.
     */
    private boolean isPomodoroPrompt = false;
    /**
     * Double representing the change in position from the start of a mouse drag to present.
     */
    private double mouseX;
    /**
     * Long number for recording time between a message for a break.
     */
    private long breakTimer;

    private double dragStartSceneX;
    private double dragStartTranslateX;
    private boolean wasDragged = false;

    private static final double DRAG_THRESHOLD = 5;

    /**
     * Handles initialisation actions for variables walkPet, desktopPet, then, breakTimer,
     * and idlePet. Calls for idling method after stage is set.
     */
    @FXML
    public void initialize() {
        // set the whole pet stack as the moving node
        movePet.setNode(imageBox);

        // create the pet object
        desktopPet = new Pet(petStates, petImage);
        desktopPet.startPet();
        // hide everything except the pet at startup
        hideAllPopups();

        // keep options managed so opening it does not shift the pet
//        optionsBox.setManaged(true);

        // record starting time
        then = System.currentTimeMillis();
        this.breakTimer = System.currentTimeMillis();
        // create automatic idle movement timer
        idlePet = new AnimationTimer() {
            @Override
            public void handle(long now) {
                now = System.currentTimeMillis();

                // move every 8 seconds
                if (now - then > 8000) {
                    Pet.movePet(desktopPet, movePet, bounds, imageBox);

                    then = now;

                    // return to idle after walking
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e -> idling())
                    );
                    timeline.playFromStart();
                }
            }
        };

        // click the pet to open or close the menu
        imageBox.setOnMouseClicked(mouseClick -> {
            // ignore button clicks
            if (mouseClick.getTarget() instanceof Button) {
                return;
            }

            // do not treat a drag as a click
            if (wasDragged) {
                wasDragged = false;
                return;
            }

            // stop movement while interacting
            idlePet.stop();
            movePet.stop();

//            // toggle the main options menu
//            boolean shouldShow = !optionsBox.isVisible();
//
//            optionsBox.setVisible(shouldShow);
//            optionsBox.setManaged(true);
//
//            // close confirmation prompt when toggling the main menu
//            petText.setVisible(false);
//            petText.setManaged(false);
//
//            promptButtons.setVisible(false);
//            promptButtons.setManaged(false);
//
//            yesbutton.setVisible(false);
//            nobutton.setVisible(false);

            // play click animation
            desktopPet.setShock();

            // reset timer without moving the pet
            then = System.currentTimeMillis();
        });

        // remember where the mouse started
        imageBox.setOnMousePressed(mousePress -> {
            // ignore button presses
            if (mousePress.getTarget() instanceof Button) {
                return;
            }

            dragStartSceneX = mousePress.getSceneX();
            dragStartTranslateX = imageBox.getTranslateX();
            wasDragged = false;
        });

        // drag the whole pet stack so the popup follows
        imageBox.setOnMouseDragged(mouseDrag -> {
            // ignore dragging from buttons
            if (mouseDrag.getTarget() instanceof Button) {
                return;
            }

            // calculate mouse movement from the original press point
            double dragDistance = mouseDrag.getSceneX() - dragStartSceneX;

            // ignore tiny movement from normal clicking
            if (Math.abs(dragDistance) < DRAG_THRESHOLD) {
                return;
            }

            wasDragged = true;

            // stop automatic movement while dragging
            idlePet.stop();
            movePet.stop();

            // calculate new pet position
            mouseX = dragStartTranslateX + dragDistance;

            // calculate screen bounds
            double leftScreenEdge = bounds.getMinX();
            double rightScreenEdge = bounds.getMaxX() - imageBox.getWidth() * 1.25;

            // clamp to left side
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }

            // clamp to right side
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }

            // move pet, options, and prompt together
            imageBox.setTranslateX(mouseX);
        });

        // only restart idle after an actual drag
        imageBox.setOnMouseReleased(mouseRelease -> {
            // ignore button releases
            if (mouseRelease.getTarget() instanceof Button) {
                return;
            }

            if (wasDragged) {
                then = System.currentTimeMillis();
                idling();
            }
        });

        // start idle after fxml finishes loading
        Platform.runLater(this::idling);
    }
    /**
     * Handles the actions of the pet when it is not being interacted with by the user.
     * Includes telling the user to take a break if a certain period of time has passed,
     * and the pet moving to the left or right on the screen randomly.
     */
    @FXML
    protected void idling() {
        // Get current time of system.
        long breakNow = System.currentTimeMillis();
        // Check that a certain period of time has passed and no other text is above the pet.
        if (breakNow - breakTimer > 30000 && !isTriviaPrompt && !isPomodoroPrompt) {
            // Set text to a break message and make it visible.
            textField.setText("Take a break to eat or drink!");
            textField.setVisible(true);
            // Reset timer so countdown starts again.
            breakTimer = breakNow;
            // After a while set the text invisible to finish the message delivery.
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(10), e -> textField.setVisible(false)));
            timeline.playFromStart();
        }
        // set pet to idle animation
        desktopPet.setIdle();

        // start automatic movement
        idlePet.start();
    }
    /**
     * Handles when the user selects the trivia button. Sets buttons and text visible
     * and text asks users to confirm intent to start trivia using the buttons.
     */
    @FXML
    protected void triviaButton() {
        // hide options but keep it managed to avoid layout shifting
//        optionsBox.setVisible(false);
//        optionsBox.setManaged(true);

        // show trivia prompt
        textField.setText("Do you want to play trivia with me?");
        textField.setVisible(true);
        textField.setManaged(true);

        promptButtons.setVisible(true);
        promptButtons.setManaged(true);

        yesbutton.setVisible(true);
        nobutton.setVisible(true);

        isTriviaPrompt = true;
        isPomodoroPrompt = false;
    }
    /**
     * Handles when the user selects the pomodoro button. Sets buttons and text visible
     * and text asks users to confirm intent to start pomodoro using the buttons.
     */
    @FXML
    protected void pomodoroButton() {
        // hide options but keep it managed to avoid layout shifting
//        optionsBox.setVisible(false);
//        optionsBox.setManaged(true);

        // show pomodoro prompt
        textField.setText("Want to start Pomodoro timer?");
        textField.setVisible(true);
        textField.setManaged(true);

        promptButtons.setVisible(true);
        promptButtons.setManaged(true);

        yesbutton.setVisible(true);
        nobutton.setVisible(true);

        isPomodoroPrompt = true;
        isTriviaPrompt = false;
    }
    /**
     * Handles when the user selects yes to launch the trivia or pomodoro application.
     * Determines whether the pomodoro or trivia button was selected and launches the
     * relevant application. Sets all text and buttons above the pet to invisible.
     */
    @FXML
    private void goButton() {
        if (isTriviaPrompt) {
            try {
                Stage primaryStage = (Stage) petText.getScene().getWindow();

                Stage triviaStage = new Stage();
                new project.Trivia.Main().start(triviaStage);

                triviaStage.setAlwaysOnTop(true);
                triviaStage.setX(primaryStage.getX() + 10);
                triviaStage.setY(primaryStage.getY() + primaryStage.getHeight() - triviaStage.getHeight() - 30);

            } catch (Exception e) {
                e.printStackTrace();
            }

            isTriviaPrompt = false;
            hideAllPopups();
        }

        else if (isPomodoroPrompt) {
            try {
                Stage primaryStage = (Stage) petText.getScene().getWindow();

                Stage pomodoroStage = new Stage();
                new project.pomodoro.MainApplication().start(pomodoroStage);

                pomodoroStage.setAlwaysOnTop(true);
                pomodoroStage.setX(primaryStage.getX() + 10);
                pomodoroStage.setY(primaryStage.getY() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10);

            } catch (Exception e) {
                e.printStackTrace();
            }

            isPomodoroPrompt = false;
            hideAllPopups();
        }
    }
    /**
     * Handles when the user selects not to start the trivia or pomodoro application after
     * being asked by the pet. Sets all text and buttons to be invisible.
     */
    @FXML
    protected void returnButton() {
        isTriviaPrompt = false;
        isPomodoroPrompt = false;

        hideAllPopups();

        idling();
    }

    private void hideAllPopups() {
        // hide option menu but keep it managed to stop layout jumps
//        optionsBox.setVisible(false);
//        optionsBox.setManaged(true);

        // hide prompt text
        textField.setVisible(false);
        textField.setManaged(true);

        // hide prompt buttons
        promptButtons.setVisible(false);
        promptButtons.setManaged(true);

        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }
}
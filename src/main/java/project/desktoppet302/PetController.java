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
     * Visual bounds of the screen.
     * getVisualBounds excludes the taskbar.
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
    private double lastDragTranslateX;
    private boolean wasDragged = false;

    private static final double DRAG_THRESHOLD = 5;
    private static final double SCREEN_PADDING = 10;

    /**
     * Handles initialisation actions for variables walkPet, desktopPet, then, breakTimer,
     * and idlePet. Calls for idling method after stage is set.
     */
    @FXML
    public void initialize() {
        movePet.setNode(imageBox);

        desktopPet = new Pet(petStates, petImage);
        desktopPet.startPet();

        hideAllPopups();

        then = System.currentTimeMillis();
        this.breakTimer = System.currentTimeMillis();

        idlePet = new AnimationTimer() {
            @Override
            public void handle(long now) {
                now = System.currentTimeMillis();

                if (now - then > 8000) {
                    Pet.movePet(desktopPet, movePet, bounds, imageBox);

                    then = now;

                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e -> idling())
                    );
                    timeline.playFromStart();
                }
            }
        };

        imageBox.setOnMouseClicked(mouseClick -> {
            if (mouseClick.getTarget() instanceof Button) {
                return;
            }

            if (wasDragged) {
                wasDragged = false;
                return;
            }

            idlePet.stop();
            movePet.stop();

            desktopPet.setShock();

            then = System.currentTimeMillis();
        });

        imageBox.setOnMousePressed(mousePress -> {
            if (mousePress.getTarget() instanceof Button) {
                return;
            }

            dragStartSceneX = mousePress.getSceneX();
            dragStartTranslateX = imageBox.getTranslateX();
            lastDragTranslateX = imageBox.getTranslateX();
            wasDragged = false;
        });

        imageBox.setOnMouseDragged(mouseDrag -> {
            if (mouseDrag.getTarget() instanceof Button) {
                return;
            }

            double dragDistance = mouseDrag.getSceneX() - dragStartSceneX;

            if (Math.abs(dragDistance) < DRAG_THRESHOLD) {
                return;
            }

            wasDragged = true;

            idlePet.stop();
            movePet.stop();

            mouseX = dragStartTranslateX + dragDistance;

            double leftScreenEdge = bounds.getMinX();
            double rightScreenEdge = bounds.getMaxX() - imageBox.getWidth() * 1.25;

            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }

            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }

            // animation directions are intentionally swapped here because the current
            // walk-left and walk-right animation sets face opposite to their names
            if (mouseX > lastDragTranslateX) {
                desktopPet.setWalkLeft();
            } else if (mouseX < lastDragTranslateX) {
                desktopPet.setWalkRight();
            }

            imageBox.setTranslateX(mouseX);
            lastDragTranslateX = mouseX;
        });

        imageBox.setOnMouseReleased(mouseRelease -> {
            if (mouseRelease.getTarget() instanceof Button) {
                return;
            }

            desktopPet.stopMoving();

            if (wasDragged) {
                then = System.currentTimeMillis();
                idling();
            }
        });

        Platform.runLater(this::idling);
    }

    /**
     * Handles the actions of the pet when it is not being interacted with by the user.
     */
    @FXML
    protected void idling() {
        long breakNow = System.currentTimeMillis();

        if (breakNow - breakTimer > 30000 && !isTriviaPrompt && !isPomodoroPrompt) {
            textField.setText("Take a break to eat or drink!");
            textField.setVisible(true);

            breakTimer = breakNow;

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(10), e -> textField.setVisible(false))
            );
            timeline.playFromStart();
        }

        desktopPet.setIdle();

        idlePet.start();
    }

    /**
     * Handles when the user selects the trivia button.
     */
    @FXML
    protected void triviaButton() {
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
     * Handles when the user selects the pomodoro button.
     */
    @FXML
    protected void pomodoroButton() {
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
     */
    @FXML
    private void goButton() {
        if (isTriviaPrompt) {
            try {
                Stage primaryStage = (Stage) textField.getScene().getWindow();

                Stage triviaStage = new Stage();
                new project.Trivia.Main().start(triviaStage);

                triviaStage.setAlwaysOnTop(true);
                clampChildStageToUsableScreen(primaryStage, triviaStage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            isTriviaPrompt = false;
            hideAllPopups();
        } else if (isPomodoroPrompt) {
            try {
                Stage primaryStage = (Stage) textField.getScene().getWindow();

                Stage pomodoroStage = new Stage();
                new project.pomodoro.MainApplication().start(pomodoroStage);

                pomodoroStage.setAlwaysOnTop(true);
                clampChildStageToUsableScreen(primaryStage, pomodoroStage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            isPomodoroPrompt = false;
            hideAllPopups();
        }
    }

    /**
     * Positions a launched trivia or pomodoro window inside the usable screen area.
     *
     * @param primaryStage the desktop pet stage
     * @param childStage the launched trivia or pomodoro stage
     */
    private void clampChildStageToUsableScreen(Stage primaryStage, Stage childStage) {
        Rectangle2D usableScreen = Screen.getPrimary().getVisualBounds();

        double childWidth = childStage.getWidth();
        double childHeight = childStage.getHeight();

        if (childWidth <= 0) {
            childWidth = childStage.getScene().getWidth();
        }

        if (childHeight <= 0) {
            childHeight = childStage.getScene().getHeight();
        }

        double preferredX = primaryStage.getX() + imageBox.getTranslateX() + 20;
        double preferredY = usableScreen.getMaxY() - childHeight - SCREEN_PADDING;

        double minX = usableScreen.getMinX() + SCREEN_PADDING;
        double maxX = usableScreen.getMaxX() - childWidth - SCREEN_PADDING;

        double minY = usableScreen.getMinY() + SCREEN_PADDING;
        double maxY = usableScreen.getMaxY() - childHeight - SCREEN_PADDING;

        double clampedX = clamp(preferredX, minX, maxX);
        double clampedY = clamp(preferredY, minY, maxY);

        childStage.setX(clampedX);
        childStage.setY(clampedY);
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

    /**
     * Handles when the user selects not to start the trivia or pomodoro application.
     */
    @FXML
    protected void returnButton() {
        isTriviaPrompt = false;
        isPomodoroPrompt = false;

        hideAllPopups();

        idling();
    }

    /**
     * Hides the prompt text and confirmation buttons above the pet.
     */
    private void hideAllPopups() {
        textField.setVisible(false);
        textField.setManaged(true);

        promptButtons.setVisible(false);
        promptButtons.setManaged(true);

        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }
}
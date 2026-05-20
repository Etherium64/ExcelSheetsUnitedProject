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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PetController {

    @FXML public Button trivia;
    @FXML public Button timer;
    @FXML public Image pet;

    @FXML private HBox hbox;
    @FXML private StackPane imagebox;
    @FXML private HBox optionsBox;
    @FXML private HBox promptButtons;
    @FXML private Text pettext;
    @FXML private Button yesbutton;
    @FXML private Button nobutton;
    @FXML private ImageView petImage;

    private final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    private final TranslateTransition move = new TranslateTransition();
    private AnimationTimer moving;
    private long then;

    private final animStates petStates = new animStates();
    private Pet desktopPet;

    private boolean isTriviaPrompt = false;
    private boolean ispomodoroPrompt = false;

    private double mouseX;
    private double dragStartSceneX;
    private double dragStartTranslateX;
    private boolean wasDragged = false;

    private static final double DRAG_THRESHOLD = 5;

    @FXML
    public void initialize() {
        // set the whole pet stack as the moving node
        move.setNode(imagebox);

        // create the pet object
        desktopPet = new Pet(petStates, petImage);

        // hide everything except the pet at startup
        hideAllPopups();

        // keep options managed so opening it does not shift the pet
        optionsBox.setManaged(true);

        // record starting time
        then = System.currentTimeMillis();

        // create automatic idle movement timer
        moving = new AnimationTimer() {
            @Override
            public void handle(long now) {
                now = System.currentTimeMillis();

                // move every 8 seconds
                if (now - then > 8000) {
                    Pet.movePet(desktopPet, move, then, now, bounds, imagebox);

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
        imagebox.setOnMouseClicked(mouseClick -> {
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
            moving.stop();
            move.stop();

            // toggle the main options menu
            boolean shouldShow = !optionsBox.isVisible();

            optionsBox.setVisible(shouldShow);
            optionsBox.setManaged(true);

            // close confirmation prompt when toggling the main menu
            pettext.setVisible(false);
            pettext.setManaged(false);

            promptButtons.setVisible(false);
            promptButtons.setManaged(false);

            yesbutton.setVisible(false);
            nobutton.setVisible(false);

            // play click animation
            desktopPet.setShock();

            // reset timer without moving the pet
            then = System.currentTimeMillis();
        });

        // remember where the mouse started
        imagebox.setOnMousePressed(mousePress -> {
            // ignore button presses
            if (mousePress.getTarget() instanceof Button) {
                return;
            }

            dragStartSceneX = mousePress.getSceneX();
            dragStartTranslateX = imagebox.getTranslateX();
            wasDragged = false;
        });

        // drag the whole pet stack so the popup follows
        imagebox.setOnMouseDragged(mouseDrag -> {
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
            moving.stop();
            move.stop();

            // calculate new pet position
            mouseX = dragStartTranslateX + dragDistance;

            // calculate screen bounds
            double leftScreenEdge = bounds.getMinX();
            double rightScreenEdge = bounds.getMaxX() - imagebox.getWidth() * 1.5;

            // clamp to left side
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }

            // clamp to right side
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }

            // move pet, options, and prompt together
            imagebox.setTranslateX(mouseX);
        });

        // only restart idle after an actual drag
        imagebox.setOnMouseReleased(mouseRelease -> {
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

    @FXML
    protected void idling() {
        // set pet to idle animation
        desktopPet.setIdle();

        // start automatic movement
        moving.start();
    }

    @FXML
    protected void triviaButton() {
        // hide options but keep it managed to avoid layout shifting
        optionsBox.setVisible(false);
        optionsBox.setManaged(true);

        // show trivia prompt
        pettext.setText("Do you want to play trivia with me?");
        pettext.setVisible(true);
        pettext.setManaged(true);

        promptButtons.setVisible(true);
        promptButtons.setManaged(true);

        yesbutton.setVisible(true);
        nobutton.setVisible(true);

        isTriviaPrompt = true;
        ispomodoroPrompt = false;
    }

    @FXML
    protected void pomodoroButton() {
        // hide options but keep it managed to avoid layout shifting
        optionsBox.setVisible(false);
        optionsBox.setManaged(true);

        // show pomodoro prompt
        pettext.setText("Want to start Pomodoro timer?");
        pettext.setVisible(true);
        pettext.setManaged(true);

        promptButtons.setVisible(true);
        promptButtons.setManaged(true);

        yesbutton.setVisible(true);
        nobutton.setVisible(true);

        ispomodoroPrompt = true;
        isTriviaPrompt = false;
    }

    @FXML
    private void goButton() {
        if (isTriviaPrompt) {
            try {
                Stage primaryStage = (Stage) pettext.getScene().getWindow();

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

        else if (ispomodoroPrompt) {
            try {
                Stage primaryStage = (Stage) pettext.getScene().getWindow();

                Stage pomodoroStage = new Stage();
                new project.pomodoro.MainApplication().start(pomodoroStage);

                pomodoroStage.setAlwaysOnTop(true);
                pomodoroStage.setX(primaryStage.getX() + 10);
                pomodoroStage.setY(primaryStage.getY() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10);

            } catch (Exception e) {
                e.printStackTrace();
            }

            ispomodoroPrompt = false;
            hideAllPopups();
        }
    }

    @FXML
    protected void returnButton() {
        isTriviaPrompt = false;
        ispomodoroPrompt = false;

        hideAllPopups();

        idling();
    }

    private void hideAllPopups() {
        // hide option menu but keep it managed to stop layout jumps
        optionsBox.setVisible(false);
        optionsBox.setManaged(true);

        // hide prompt text
        pettext.setVisible(false);
        pettext.setManaged(false);

        // hide prompt buttons
        promptButtons.setVisible(false);
        promptButtons.setManaged(false);

        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }
}
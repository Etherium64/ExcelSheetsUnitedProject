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

    @FXML
    public Button trivia;

    @FXML
    public Button timer;

    @FXML
    public Image pet;

    @FXML
    private HBox hbox;

    @FXML
    private StackPane imagebox;

    @FXML
    private HBox optionsBox;

    @FXML
    private HBox promptButtons;

    @FXML
    private Text pettext;

    @FXML
    private Button yesbutton;

    @FXML
    private Button nobutton;

    @FXML
    private Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

    @FXML
    private double mouseX;

    @FXML
    private Stage stage;

    @FXML
    private TranslateTransition move = new TranslateTransition();

    @FXML
    private AnimationTimer moving;

    @FXML
    private long then;

    @FXML
    private animStates petStates = new animStates();

    @FXML
    private ImageView petImage;

    @FXML
    private Pet desktopPet;

    private boolean isTriviaPrompt = false;
    private boolean ispomodoroPrompt = false;

    private double dragOffsetX;
    private boolean wasDragged = false;

    @FXML
    public void initialize() {
        // set the stackpane as the thing that moves around
        move.setNode(imagebox);

        // create the pet object
        desktopPet = new Pet(petStates, petImage);

        // hide the option buttons when the app starts
        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        // hide the prompt text when the app starts
        pettext.setVisible(false);
        pettext.setManaged(false);

        // hide the yes/no prompt buttons when the app starts
        promptButtons.setVisible(false);
        promptButtons.setManaged(false);

        // hide the actual yes/no buttons too
        yesbutton.setVisible(false);
        nobutton.setVisible(false);

        // record the starting time
        this.then = System.currentTimeMillis();

        // create the automatic movement timer
        this.moving = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // use normal system time for easier timing
                now = System.currentTimeMillis();

                // move every 8 seconds
                if (now - then > 8000) {
                    Pet.movePet(desktopPet, move, then, now, bounds, imagebox);

                    // reset the timer
                    then = now;

                    // go back to idle after walking
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e -> idling())
                    );
                    timeline.playFromStart();
                }
            }
        };

        // handle clicks on the whole pet stack
        imagebox.setOnMouseClicked(mouseClick -> {
            // ignore clicks on buttons
            if (mouseClick.getTarget() instanceof Button) {
                return;
            }

            // do not treat a drag as a click
            if (wasDragged) {
                wasDragged = false;
                return;
            }

            // stop automatic movement while the menu is open
            moving.stop();

            // toggle the main options menu
            boolean shouldShow = !optionsBox.isVisible();
            optionsBox.setVisible(shouldShow);
            optionsBox.setManaged(shouldShow);

            // hide the confirmation prompt when toggling the main menu
            pettext.setVisible(false);
            pettext.setManaged(false);

            promptButtons.setVisible(false);
            promptButtons.setManaged(false);

            yesbutton.setVisible(false);
            nobutton.setVisible(false);

            // play the click reaction animation
            Pet.setPet(desktopPet, animStates.PetState.SHOCK);

            // reset idle timer
            then = System.currentTimeMillis();
        });

        // remember where the mouse grabbed the pet
        imagebox.setOnMousePressed(mousePress -> {
            // ignore button presses
            if (mousePress.getTarget() instanceof Button) {
                return;
            }

            dragOffsetX = mousePress.getSceneX() - imagebox.getTranslateX();
        });

        // drag the whole pet stack so buttons follow
        imagebox.setOnMouseDragged(mouseDrag -> {
            // ignore dragging from buttons
            if (mouseDrag.getTarget() instanceof Button) {
                return;
            }

            // mark this as a drag so mouse release does not toggle the menu
            wasDragged = true;

            // stop automatic movement while dragging
            moving.stop();

            // calculate new x position
            mouseX = mouseDrag.getSceneX() - dragOffsetX;

            // get screen edges
            double leftScreenEdge = bounds.getMinX();
            double rightScreenEdge = bounds.getMaxX() - imagebox.getWidth() * 1.5;

            // clamp to left edge
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }

            // clamp to right edge
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }

            // move pet and buttons together
            imagebox.setTranslateX(mouseX);
        });

        // resume idle after dragging
        imagebox.setOnMouseReleased(mouseRelease -> {
            // ignore button releases
            if (mouseRelease.getTarget() instanceof Button) {
                return;
            }

            // reset idle timer
            then = System.currentTimeMillis();

            // go back to idle
            idling();
        });

        // start idling after fxml finishes loading
        Platform.runLater(this::idling);
    }

    @FXML
    protected void idling() {
        // set the pet to idle animation
        Pet.setPet(desktopPet, animStates.PetState.IDLE);

        // start the automatic movement timer
        moving.start();
    }

    @FXML
    protected void triviaButton() {
        // hide option buttons
        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        // show trivia prompt text
        pettext.setText("Do you want to play trivia with me?");
        pettext.setVisible(true);
        pettext.setManaged(true);

        // show yes and no buttons
        promptButtons.setVisible(true);
        promptButtons.setManaged(true);
        yesbutton.setVisible(true);
        nobutton.setVisible(true);

        // set current prompt state
        isTriviaPrompt = true;
        ispomodoroPrompt = false;
    }

    @FXML
    protected void pomodoroButton() {
        // hide option buttons
        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        // show pomodoro prompt text
        pettext.setText("Want to start Pomodoro timer?");
        pettext.setVisible(true);
        pettext.setManaged(true);

        // show yes and no buttons
        promptButtons.setVisible(true);
        promptButtons.setManaged(true);
        yesbutton.setVisible(true);
        nobutton.setVisible(true);

        // set current prompt state
        ispomodoroPrompt = true;
        isTriviaPrompt = false;
    }

    @FXML
    private void goButton() {
        if (isTriviaPrompt) {
            try {
                // get the desktop pet stage
                Stage primaryStage = (Stage) pettext.getScene().getWindow();

                // create trivia stage
                Stage triviaStage = new Stage();

                // start trivia
                new project.Trivia.Main().start(triviaStage);

                // keep trivia above other windows
                triviaStage.setAlwaysOnTop(true);

                // position trivia near the desktop pet window
                triviaStage.setX(primaryStage.getX() + 10);
                triviaStage.setY(primaryStage.getY() + primaryStage.getHeight() - triviaStage.getHeight() - 30);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // reset prompt state
            isTriviaPrompt = false;
            hideAllPopups();
        }

        else if (ispomodoroPrompt) {
            try {
                // get the desktop pet stage
                Stage primaryStage = (Stage) pettext.getScene().getWindow();

                // create pomodoro stage
                Stage pomodoroStage = new Stage();

                // start pomodoro
                new project.pomodoro.MainApplication().start(pomodoroStage);

                // keep pomodoro above other windows
                pomodoroStage.setAlwaysOnTop(true);

                // position pomodoro near the desktop pet window
                pomodoroStage.setX(primaryStage.getX() + 10);
                pomodoroStage.setY(primaryStage.getY() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // reset prompt state
            ispomodoroPrompt = false;
            hideAllPopups();
        }
    }

    @FXML
    protected void returnButton() {
        // reset all prompt states
        isTriviaPrompt = false;
        ispomodoroPrompt = false;

        // hide all menus and prompts
        hideAllPopups();

        // return to idle
        idling();
    }

    private void hideAllPopups() {
        // hide option buttons
        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        // hide prompt text
        pettext.setVisible(false);
        pettext.setManaged(false);

        // hide yes/no prompt buttons
        promptButtons.setVisible(false);
        promptButtons.setManaged(false);

        // hide the actual yes/no buttons too
        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }
}
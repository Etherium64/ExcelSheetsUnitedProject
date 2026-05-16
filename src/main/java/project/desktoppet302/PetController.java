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

    @FXML
    public void initialize() {
        // set the node that gets moved around
        move.setNode(imagebox);

        // create the pet object
        desktopPet = new Pet(petStates, petImage);

        // hide option buttons when the app starts
        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        // hide prompt text when the app starts
        pettext.setVisible(false);
        pettext.setManaged(false);

        // hide prompt buttons when the app starts
        promptButtons.setVisible(false);
        promptButtons.setManaged(false);

        // hide individual prompt buttons as well
        yesbutton.setVisible(false);
        nobutton.setVisible(false);

        // record the starting time
        this.then = System.currentTimeMillis();

        // create the automatic movement timer
        this.moving = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // get the current system time
                now = System.currentTimeMillis();

                // move every 8 seconds
                if (now - then > 8000) {
                    Pet.movePet(desktopPet, move, then, now, bounds, imagebox);

                    // reset the timer
                    then = now;

                    // return to idle after the walking animation
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e -> idling())
                    );
                    timeline.playFromStart();
                }
            }
        };

        // start idling after fxml finishes loading
        Platform.runLater(this::idling);
    }

    @FXML
    protected void idling() {
        // set pet to idle animation
        Pet.setPet(desktopPet, animStates.PetState.IDLE);

        // start the automatic movement timer
        moving.start();
    }

    @FXML
    protected void onImageClick() {
        // stop automatic movement while the menu is open
        moving.stop();

        // play the click reaction animation
        Pet.setPet(desktopPet, animStates.PetState.SHOCK);

        // show the horizontal option buttons above the pet
        optionsBox.setVisible(true);
        optionsBox.setManaged(true);

        // hide prompt text
        pettext.setVisible(false);
        pettext.setManaged(false);

        // hide prompt buttons
        promptButtons.setVisible(false);
        promptButtons.setManaged(false);
        yesbutton.setVisible(false);
        nobutton.setVisible(false);

        // reset idle timer
        then = System.currentTimeMillis();
    }

    @FXML
    protected void onImageDrag() {
        // stop automatic movement while dragging
        moving.stop();

        // get the current stage
        stage = (Stage) hbox.getScene().getWindow();

        // calculate the new x position
        mouseX = petImage.localToScene(petImage.getBoundsInLocal()).getMinX();

        // set the drag event
        petImage.setOnMouseDragged(mouseDrag -> {
            // get mouse x and center the pet under the cursor
            mouseX = mouseDrag.getSceneX() - petImage.getFitWidth() / 2;

            // get the left edge of the screen
            double leftScreenEdge = bounds.getMinX();

            // get the right edge of the screen minus the pet width
            double rightScreenEdge = bounds.getMaxX() - imagebox.getWidth() * 1.5;

            // clamp the x position to the left edge
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }

            // clamp the x position to the right edge
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }

            // move the pet and anything attached to it
            imagebox.setTranslateX(mouseX);
        });

        // return to idle after dragging setup
        idling();
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

                // make a new trivia stage
                Stage triviaStage = new Stage();

                // start trivia
                new project.Trivia.Main().start(triviaStage);

                // keep trivia above other windows
                triviaStage.setAlwaysOnTop(true);

                // keep trivia near the pet when the main stage moves
                primaryStage.xProperty().addListener((obs, old, val) ->
                        triviaStage.setX(val.doubleValue() + 10));

                primaryStage.yProperty().addListener((obs, old, val) ->
                        triviaStage.setY(val.doubleValue() + primaryStage.getHeight() - triviaStage.getHeight() + 10));

                primaryStage.widthProperty().addListener((obs, old, val) ->
                        triviaStage.sizeToScene());

                primaryStage.heightProperty().addListener((obs, old, val) ->
                        triviaStage.sizeToScene());

                primaryStage.iconifiedProperty().addListener((obs, old, val) ->
                        triviaStage.setIconified(val));

                // set starting trivia position
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

                // make a new pomodoro stage
                Stage pomodoroStage = new Stage();

                // start pomodoro
                new project.pomodoro.MainApplication().start(pomodoroStage);

                // keep pomodoro above other windows
                pomodoroStage.setAlwaysOnTop(true);

                // keep pomodoro near the pet when the main stage moves
                primaryStage.xProperty().addListener((obs, old, val) ->
                        pomodoroStage.setX(val.doubleValue() + 10));

                primaryStage.yProperty().addListener((obs, old, val) ->
                        pomodoroStage.setY(val.doubleValue() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10));

                primaryStage.widthProperty().addListener((obs, old, val) ->
                        pomodoroStage.sizeToScene());

                primaryStage.heightProperty().addListener((obs, old, val) ->
                        pomodoroStage.sizeToScene());

                primaryStage.iconifiedProperty().addListener((obs, old, val) ->
                        pomodoroStage.setIconified(val));

                // set starting pomodoro position
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

        // hide prompt buttons
        promptButtons.setVisible(false);
        promptButtons.setManaged(false);

        // hide individual prompt buttons
        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }
}
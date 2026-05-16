package project.desktoppet302;
import javafx.scene.layout.StackPane;
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
import java.util.Random.*;


public class PetController {

    @FXML
    private VBox optionsBox;

    @FXML
    public Button trivia;


    @FXML
    public Button timer;
    public Image pet;

    @FXML
    private HBox hbox;

    @FXML
    private StackPane imagebox;

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
    private double mouseY;

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

    // Initialise starting values.
    @FXML
    public void initialize() {
        // Set translate transition to allows for animation translations for movement.
        move.setNode(imagebox);
        // Set the animation state for the pet.
        desktopPet = new Pet(petStates, petImage);
        // Record current time of system when application starts.
        this.then = (System.currentTimeMillis());
        //Create animation timer for the movement when the pet is not interacted with.
        this.moving = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Record  current time of system.
                now = System.currentTimeMillis();
                if (now - then > 8000) {
                    Pet.movePet(desktopPet, move, then, now, bounds, imagebox);
                    // Set the old system time to the current time, allowing for repetition of animation timer.
                    then = now;
                    // Timeline that allows for walking animation to play before restarting the animation timer again.
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e -> idling()));
                    timeline.playFromStart();
                }
            }
        };
        // Run idling after the stage is set.
        Platform.runLater(this::idling);

        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        pettext.setVisible(false);
        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }


    @FXML
    protected void idling() {
        // Set the pet to the idle animation.
        Pet.setPet(desktopPet, animStates.PetState.IDLE);
        // Start the animation timer for movement when pet is not interacted with.
        moving.start();
    }

    @FXML
    protected void onImageClick() {
        // stop the pet moving while the menu is open
        moving.stop();

        // play the click reaction animation
        Pet.setPet(desktopPet, animStates.PetState.SHOCK);

        // show the options above the pet
        optionsBox.setVisible(true);
        optionsBox.setManaged(true);

        // hide the yes/no prompt
        pettext.setVisible(false);
        yesbutton.setVisible(false);
        nobutton.setVisible(false);

        // reset the idle timer
        then = System.currentTimeMillis();
    }


    @FXML
    protected void onImageDrag() throws InterruptedException {
        // Stop the idle animation countdown.
        moving.stop();
        // Get current stage on window.
        stage = (Stage) hbox.getScene().getWindow();
        //Apply translation to pet based on mouse position change.
        petImage.setOnMouseDragged(mouseDrag -> {
            // Get the X position based on mouse movement
            mouseX = mouseDrag.getSceneX() - petImage.getFitWidth() / 2;
            // Find left edge of screen
            double leftScreenEdge = bounds.getMinX();
            // Find right edge of screen minus the pets width
            double rightScreenEdge = bounds.getMaxX() - imagebox.getWidth() * 1.5;
            // Clamp X-axis so pet cannot go off sides of screen
            if (mouseX < leftScreenEdge) {
                mouseX = leftScreenEdge;
            }
            if (mouseX > rightScreenEdge) {
                mouseX = rightScreenEdge;
            }
            // Apply clamped X position to image
            imagebox.setTranslateX(mouseX);
        });
        // Go back to idling.
        idling();
    }

    private boolean isTriviaPrompt = false;
    private boolean ispomodoroPrompt = false;

    @FXML
    protected void triviaButton() {
        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        pettext.setText("Do you want to play trivia with me?");
        pettext.setVisible(true);
        yesbutton.setVisible(true);
        nobutton.setVisible(true);

        isTriviaPrompt = true;
        ispomodoroPrompt = false;
    }

    @FXML
    protected void pomodoroButton() {
        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        pettext.setText("Want to start Pomodoro timer?");
        pettext.setVisible(true);
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

                // Position relative to desktop pet
                primaryStage.xProperty().addListener((obs, old, val) ->
                        triviaStage.setX(val.doubleValue() + 10));
                primaryStage.yProperty().addListener((obs, old, val) ->
                        triviaStage.setY(val.doubleValue() + primaryStage.getHeight() - triviaStage.getHeight() + 10));
                primaryStage.widthProperty().addListener((obs, old, val) -> triviaStage.sizeToScene());
                primaryStage.heightProperty().addListener((obs, old, val) -> triviaStage.sizeToScene());
                primaryStage.iconifiedProperty().addListener((obs, old, val) -> triviaStage.setIconified(val));

                // Initial position in bottom-left of main window
                triviaStage.setX(primaryStage.getX() + 10);
                triviaStage.setY(primaryStage.getY() + primaryStage.getHeight() - triviaStage.getHeight() - 30);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Reset
            isTriviaPrompt = false;
            pettext.setVisible(false);
            yesbutton.setVisible(false);
            nobutton.setVisible(false);
        } else if (ispomodoroPrompt) { // Add this new block
            try {
                Stage primaryStage = (Stage) pettext.getScene().getWindow();
                Stage pomodoroStage = new Stage();
                new project.pomodoro.MainApplication().start(pomodoroStage); // Adjust package/class as needed
                pomodoroStage.setAlwaysOnTop(true);

                // Position relative to desktop pet (same as trivia)
                primaryStage.xProperty().addListener((obs, old, val) ->
                        pomodoroStage.setX(val.doubleValue() + 10));
                primaryStage.yProperty().addListener((obs, old, val) ->
                        pomodoroStage.setY(val.doubleValue() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10));
                primaryStage.widthProperty().addListener((obs, old, val) -> pomodoroStage.sizeToScene());
                primaryStage.heightProperty().addListener((obs, old, val) -> pomodoroStage.sizeToScene());
                primaryStage.iconifiedProperty().addListener((obs, old, val) -> pomodoroStage.setIconified(val));

                // Initial position in bottom-left of main window
                pomodoroStage.setX(primaryStage.getX() + 10);
                pomodoroStage.setY(primaryStage.getY() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Reset
            ispomodoroPrompt = false;
            pettext.setVisible(false);
            yesbutton.setVisible(false);
            nobutton.setVisible(false);
        }
    }
    @FXML
    protected void returnButton() {
        pettext.setVisible(false);
        yesbutton.setVisible(false);
        nobutton.setVisible(false);

        optionsBox.setVisible(false);
        optionsBox.setManaged(false);

        isTriviaPrompt = false;
        ispomodoroPrompt = false;
    }
}




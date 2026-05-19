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
import project.authentication.Login;
import project.authentication.UserSingleton;
import project.pomodoro.Pomodoro;
import project.pomodoro.PomodoroController;

import java.util.Random;

public class PetController {

    @FXML
    public Button trivia;

    @FXML
    public Button pomodoro;

    @FXML
    public Button logout;

    @FXML
    private HBox hbox;

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

    @FXML
    private animStates petStates;

    // Initialise starting values.
    @FXML
    public void initialize() {
        //Set bounds of screen.
        bounds = Screen.getPrimary().getVisualBounds();
        // Set translate transition to allows for animation translations for movement.
        move = new TranslateTransition();
        move.setNode(imagebox);
        // Set the animation state for the pet.
        petStates = new animStates();
        // Record current time of system when application starts.
        this.then = (System.currentTimeMillis());
        //Create animation timer for the movement when the pet is not interacted with.
        this.moving = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //Record  current time of system.
                now = System.currentTimeMillis();
                if (now - then > 8000) {
                    // Generate two random numbers between -100 and 100 after eight seconds from previous generation or System Start.
                    Random z = new Random();
                    double x = (double) z.nextInt(200) - 100;
                    double y = (double) z.nextInt(200) - 100;
                    // Set animation movement time.
                    move.setDuration(Duration.seconds(2));
                    // Set horizontal and vertical translation base on the random numbers generated.
                    move.setByX(x);
                    move.setByY(y);
                    // Based on whether the pet moves left or right, update animation state to show the pet walking in said direction.
                    if (x > 0) {
                        petStates.setState(animStates.PetState.WALKLEFT);
                        new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                petStates.update();
                                Image pet = petStates.getCurrentFrame();
                                petImage.setImage(pet);
                            }
                        }.start();
                    } else {
                        petStates.setState(animStates.PetState.WALKRIGHT);
                        new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                petStates.update();
                                Image pet = petStates.getCurrentFrame();
                                petImage.setImage(pet);
                            }
                        }.start();
                    }
                    // Play animation.
                    move.play();
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

    }


    @FXML
    protected void idling() {
        // Set the pet to the idle animation.
        petStates.setState(animStates.PetState.IDLE);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                petStates.update();
                Image pet = petStates.getCurrentFrame();
                petImage.setImage(pet);
            }
        }.start();
        // Start the animation timer for movement when pet is not interacted with.
        moving.start();

    }

    // State change when animal is clicked.
    @FXML
    protected void onImageClick() {
        // Stop the idle animation countdown.
        moving.stop();
        // Implement the jumping animation.
        petStates.setState(animStates.PetState.JUMP);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                petStates.update();
                Image pet = petStates.getCurrentFrame();
                petImage.setImage(pet);
            }
        }.start();
        // Reset the timer for the idling animation.
        then = System.currentTimeMillis();
        // Implement a timeline that allows jumping animation to play through and then start idling animation and events again.
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2.75), e -> idling()));
        timeline.playFromStart();
    }


    @FXML
    protected void onImageDrag() throws InterruptedException {
        // Stop the idle animation countdown.
        moving.stop();
        // Get current stage on window.
        stage = (Stage) hbox.getScene().getWindow();
        petImage.setOnMouseDragged(mouseDrag -> {
            // Get the X position based on mouse movement
            mouseX = mouseDrag.getSceneX() - petImage.getFitWidth() / 2 - 150;
            // Get the Y position based on mouse movement
            mouseY = mouseDrag.getSceneY() - petImage.getFitHeight() / 2;
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

    private boolean isTriviaPrompt = false;
    private boolean ispomodoroPrompt = false;
    private boolean islogoutPrompt = false;

    @FXML
    protected void triviaButton() {
        pettext.setText("Do you want to play trivia with me?");
        pettext.setVisible(true);
        yesbutton.setVisible(true);
        nobutton.setVisible(true);
        isTriviaPrompt = true; // Set the state
    }

    @FXML
    protected void pomodoroButton() {
        pettext.setText("Want to start Pomodoro timer");
        pettext.setVisible(true);
        yesbutton.setVisible(true);
        nobutton.setVisible(true);
        ispomodoroPrompt = true;
    }

    @FXML
    protected void logoutButton() {
        pettext.setText("Want to log out?");
        pettext.setVisible(true);
        yesbutton.setVisible(true);
        nobutton.setVisible(true);
        islogoutPrompt = true;
    }

    @FXML
    private void goButton() throws Exception {
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
                new Pomodoro().start(pomodoroStage); // Adjust package/class as needed
                pomodoroStage.setAlwaysOnTop(true);

//                // Position relative to desktop pet (same as trivia)
                primaryStage.xProperty().addListener((obs, old, val) ->
                        pomodoroStage.setX(val.doubleValue() + 10));
                primaryStage.yProperty().addListener((obs, old, val) ->
                        pomodoroStage.setY(val.doubleValue() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10));
                primaryStage.widthProperty().addListener((obs, old, val) -> pomodoroStage.sizeToScene());
                primaryStage.heightProperty().addListener((obs, old, val) -> pomodoroStage.sizeToScene());
                primaryStage.iconifiedProperty().addListener((obs, old, val) -> pomodoroStage.setIconified(val));

//                // Initial position in bottom-left of main window
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
        else if (islogoutPrompt)
        {
            move.stop();
            moving.stop();
            UserSingleton.getInstance().setup(0, ""); //Dummy information ensure User information doesn't remain
            Stage loginStage = (Stage) logout.getScene().getWindow();
            Login newLogin =  new Login();
            newLogin.start(loginStage);
        }
    }

    @FXML
    protected void returnButton() {
        pettext.setText("KYS");
        pettext.setVisible(false);
        yesbutton.setVisible(false);
        nobutton.setVisible(false);
    }
}




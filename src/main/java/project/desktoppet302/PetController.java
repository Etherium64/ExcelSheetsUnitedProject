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
    public Button trivia;

    @FXML
    public Button timer;

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
                    if (x > 0) {
                        petStates.setState(animStates.PetState.WALKlEFT);
                        new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                petStates.update();
                                Image pet = petStates.getCurrentFrame();
                                petImage.setImage(pet);
                            }
                        }.start();
                    }
                    else {
                        petStates.setState(animStates.PetState.WALKrIGHT);
                        new AnimationTimer() {
                            @Override
                            public void handle(long now) {
                                petStates.update();
                                Image pet = petStates.getCurrentFrame();
                                petImage.setImage(pet);
                            }
                        }.start();
                    }
                    move.play();
                    then = now;
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(2), e -> idling()));
                    timeline.playFromStart();
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
    protected void onImageClick() {
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
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> idling()));
        timeline.playFromStart();
    }


    @FXML
    protected void onImageDrag() throws InterruptedException {
        moving.stop();
        // Get current stage on window.
        stage = (Stage) box.getScene().getWindow();
        petImage.setOnMouseDragged(mouseDrag -> {
            // Get the X position based on mouse movement
            mouseX = mouseDrag.getSceneX() - petImage.getFitWidth()/2 - 125;
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
    private boolean ispomodoroPrompt = false;
    private boolean ishistoryPrompt = false;

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
                triviaStage.setY(primaryStage.getY() + primaryStage.getHeight() - triviaStage.getHeight() + 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Reset
            isTriviaPrompt = false;
            pettext.setVisible(false);
            yesbutton.setVisible(false);
            nobutton.setVisible(false);
//        } else if (pomodoroPrompt) { // Add this new block
//            try {
//                Stage primaryStage = (Stage) pettext.getScene().getWindow();
//                Stage pomodoroStage = new Stage();
//                new project.Pomodoro.Main().start(pomodoroStage); // Adjust package/class as needed
//                pomodoroStage.setAlwaysOnTop(true);
//
//                // Position relative to desktop pet (same as trivia)
//                primaryStage.xProperty().addListener((obs, old, val) ->
//                        pomodoroStage.setX(val.doubleValue() + 10));
//                primaryStage.yProperty().addListener((obs, old, val) ->
//                        pomodoroStage.setY(val.doubleValue() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10));
//                primaryStage.widthProperty().addListener((obs, old, val) -> pomodoroStage.sizeToScene());
//                primaryStage.heightProperty().addListener((obs, old, val) -> pomodoroStage.sizeToScene());
//                primaryStage.iconifiedProperty().addListener((obs, old, val) -> pomodoroStage.setIconified(val));
//
//                // Initial position in bottom-left of main window
//                pomodoroStage.setX(primaryStage.getX() + 10);
//                pomodoroStage.setY(primaryStage.getY() + primaryStage.getHeight() - pomodoroStage.getHeight() + 10);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            // Reset
//            pomodoroPrompt = false;
//            pettext.setVisible(false);
//            yesbutton.setVisible(false);
//            nobutton.setVisible(false);
//        }
//        else if (historyPrompt) { // Add this new block
//            try {
//                Stage primaryStage = (Stage) pettext.getScene().getWindow();
//                Stage historyStage = new Stage();
//                new project.History.Main().start(historyStage); // Adjust package/class as needed
//                historyStage.setAlwaysOnTop(true);
//
//                // Position relative to desktop pet (same as trivia and pomodoro)
//                primaryStage.xProperty().addListener((obs, old, val) ->
//                        historyStage.setX(val.doubleValue() + 10));
//                primaryStage.yProperty().addListener((obs, old, val) ->
//                        historyStage.setY(val.doubleValue() + primaryStage.getHeight() - historyStage.getHeight() + 10));
//                primaryStage.widthProperty().addListener((obs, old, val) -> historyStage.sizeToScene());
//                primaryStage.heightProperty().addListener((obs, old, val) -> historyStage.sizeToScene());
//                primaryStage.iconifiedProperty().addListener((obs, old, val) -> historyStage.setIconified(val));
//
//                // Initial position in bottom-left of main window
//                historyStage.setX(primaryStage.getX() + 10);
//                historyStage.setY(primaryStage.getY() + primaryStage.getHeight() - historyStage.getHeight() + 10);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            // Reset
//            historyPrompt = false;
//            pettext.setVisible(false);
//            yesbutton.setVisible(false);
//            nobutton.setVisible(false);
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




package AnimationStates;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AnimationTestWindow extends Application {

    private animStates petStates;
    private Canvas canvas;

    private double scale = 0.5; // scale down to 50%

    @Override
    public void start(Stage stage) {
        petStates = new animStates();

        petStates.setShock();

        canvas = new Canvas(600, 600);
        StackPane root = new StackPane(canvas);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("JavaFX Animation Test");
        stage.show();

        // JavaFX game loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {

                // NEW: update now takes a timestamp
                petStates.update(now);

                draw();
            }
        }.start();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Image frame = petStates.getCurrentFrame();
        if (frame == null) return;

        double scaledWidth = frame.getWidth() * scale;
        double scaledHeight = frame.getHeight() * scale;

        double x = (canvas.getWidth() - scaledWidth) / 2;
        double y = (canvas.getHeight() - scaledHeight) / 2;

        gc.drawImage(frame, x, y, scaledWidth, scaledHeight);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

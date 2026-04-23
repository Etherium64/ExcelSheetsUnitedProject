package project.desktoppet302;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimeController {

    // Singleton instance
    private static final TimeController timer = new TimeController();

    // Timeline updates the timer every second
    private Timeline timeline;

    // Label displaying the timer
    private Label timerLabel;

    // Current remaining time in seconds
    private int timeRemaining;

    // Original starting time, used for reset
    private int initialTime;

    private TimeController() {
    }

    public static TimeController getTimer() {
        return timer;
    }

    // Sets up the timer label and starting time
    public void setupTimer(Label timerLabel, int timeElapsed) {
        this.timerLabel = timerLabel;
        this.initialTime = timeElapsed;
        this.timeRemaining = timeElapsed;

        updateLabel();

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (timeRemaining > 0) {
                timeRemaining--;
                updateLabel();
            } else {
                timeline.stop();
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    // Starts or resumes the timer
    public void startTimer() {
        if (timeline != null) {
            timeline.play();
        }
    }

    // Pauses the timer
    public void pauseTimer() {
        if (timeline != null) {
            timeline.pause();
        }
    }

    // Resets the timer to original starting time
    public void resetTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeRemaining = initialTime;
        updateLabel();
    }

    // Updates the label in MM:SS format
    private void updateLabel() {
        if (timerLabel == null) {
            return;
        }

        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;

        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}
package project.pomodoro;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;


public class TimeController {

    static private TimeController thisTimeController;
    volatile boolean isPaused = true;
    private boolean firstCount = true;

    private Timer pomodoroTimer;
    private Label timerLabel;
    private static int timeElapsed;
    private static int timeBegins;

    private String formatTimer() {
        int minutes = timeElapsed / 60;
        int seconds = timeElapsed % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    static public TimeController getTimer() {
        if (thisTimeController == null) {
            thisTimeController = new TimeController();
        }
        return thisTimeController;
    }

    public void setupTimer(Label thisLabel, int thisTime) {
        timerLabel = thisLabel;
        timeElapsed = thisTime;
        timeBegins = thisTime;
        isPaused = true;
    }


    public void switchTimer() {
        isPaused = !isPaused;

        if (firstCount) {
            firstCount = false;
            pomodoroTimer = new Timer();
            pomodoroTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        timeElapsed--;
                        Platform.runLater(() -> timerLabel.setText(formatTimer()));
                    }
                }
            }, 1000, 1000);
        } else {
            pomodoroTimer = new Timer();
            pomodoroTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        Platform.runLater(() -> timerLabel.setText(formatTimer()));
                    }
                }
            }, 1000, 1000);
        }
    }

    public void resetTimer() {
        isPaused = true;
        timeElapsed = timeBegins;
        timerLabel.setText(formatTimer());
    }

}





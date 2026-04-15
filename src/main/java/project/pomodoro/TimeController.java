package project.pomodoro;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;


public class TimeController {

    static private TimeController thisTimeController;

    private Timer pomodoroTimer;
    private Label timerLabel;
    private static int timeElapsed = 1500;

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

    public void setLabel(Label thisLabel) {
        timerLabel = thisLabel;
    }

    public void startTimer() {
        if (pomodoroTimer == null) {
            pomodoroTimer = new Timer();
            pomodoroTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timeElapsed--;
                    Platform.runLater(() -> timerLabel.setText(formatTimer()));
                }
            }, 1000, 1000);
        }
    }


}


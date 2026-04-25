package project.pomodoro;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;


public class TimeController {

    static private TimeController thisTimeController;
    volatile boolean isPaused = true;
    private boolean firstCount = true;

    private Timer pomodoroTimer;
    private Label timerLabel;
    private ProgressBar timerBar;
    private Button switchBtn;
    private static int timeElapsed;
    private static int timeBegins;
    private static double progress;
    private static double progressIncrements;

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

    public void setupTimer(Label thisLabel, ProgressBar thisProgressBar, Button thisBtn, int thisTime) {
        timerLabel = thisLabel;
        timerBar = thisProgressBar;
        switchBtn = thisBtn;
        timeElapsed = thisTime;
        timeBegins = thisTime;
        progress = 1;
        timerBar.setProgress(progress);
        progressIncrements =  (double) 1 / thisTime;
        isPaused = true;
    }

    public void timerFinished()
    {
        isPaused=true;
        timeElapsed=0;
        switchBtn.setDisable(true);
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
                        progress-=progressIncrements;
                        Platform.runLater(() -> {
                            timerLabel.setText(formatTimer());
                            timerBar.setProgress((progress));
                            if (timeElapsed <=0)
                            {
                                timerFinished();
                            }
                        });
                    }
                }
            }, 1000, 1000);
        } else {
            pomodoroTimer = new Timer();
            pomodoroTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        Platform.runLater(() -> {
                            timerLabel.setText(formatTimer());
                            timerBar.setProgress((progress));
                            if (timeElapsed <=0)
                            {
                                timerFinished();
                            }
                        });
                    }
                }
            }, 1000, 1000);
        }
    }

    public void resetTimer() {
        isPaused = true;
        timeElapsed = timeBegins;
        timerLabel.setText(formatTimer());
        progress = 1;
        timerBar.setProgress(progress);
    }
}





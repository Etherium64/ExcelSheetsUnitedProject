package project.pomodoro;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for managing the timer logic in a Pomodoro application.
 * Handles starting, pausing, and resetting the timer, and updates the UI label
 * with the formatted remaining time. Uses a background {@link Timer} thread
 * and {@link Platform#runLater(Runnable)} to safely update JavaFX UI components.
 */
public class TimeController {

    /** Singleton instance of the timer controller. */
    static private TimeController thisTimeController;


    /** Indicates whether the timer is currently paused. */
    private volatile boolean isPaused = false;

    /** Flag to determine if this is the first time the timer is being started. */
    private boolean firstCount = true;


    /** The background timer task that drives the countdown. */
    private Timer pomodoroTimer;

    /** The label displaying the timer value in the UI. */
    private Label timerLabel;

    /** Remaining time in seconds. */
    private static int timeElapsed;

    /** Initial time set for the timer (used for reset).*/
    private static int timeBegins;


    /**
     * Formats the remaining time into MM:SS string format.
     * @return A string representation of the time in "mm:ss" format with leading zeros.
     */
    private String formatTimer() {
        int minutes = timeElapsed / 60;
        int seconds = timeElapsed % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


    /**
     * Returns the singleton instance of the TimeController.
     * Creates a new instance if none exists.
     *
     * @return The single instance of TimeController
     */
    static public TimeController getTimer() {
        if (thisTimeController == null) {
            thisTimeController = new TimeController();
        }
        return thisTimeController;
    }


    /**
     * Initializes the timer with a specific label and duration.
     *
     * @param thisLabel The UI label to display the timer
     * @param thisTime The initial time in seconds
     */
    public void setupTimer(Label thisLabel, int thisTime) {
        timerLabel = thisLabel;
        timeElapsed = thisTime;
        timeBegins = thisTime+1;

    }


    /**
     * Starts or resumes the timer.
     * On first call, creates a new Timer and begins countdown.
     * Subsequent calls resume from paused state.
     */
    public void startTimer() {
        isPaused = false;

        if (firstCount)
        {
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
        }
        else
        {
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

    /**
     * Pauses the timer without resetting it.
     * The timer can be resumed with {@link #startTimer()}.
     */
    public void pauseTimer() {
        isPaused = true;
    }


    /**
     * Resets the timer to its initial duration.
     * The timer remains paused after reset.
     */
    public void resetTimer() {
        timeElapsed = timeBegins;

    }

}





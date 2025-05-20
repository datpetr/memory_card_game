package main.cardgame.model;

import java.util.Observable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Represents a timer for the memory card game.
 * Supports both countdown and elapsed time tracking modes.
 */
public class Timer extends Observable {
    /**
     * Enumeration of possible timer states
     */
    private enum State {
        READY,
        RUNNING,
        PAUSED,
        STOPPED
    }
    private long startTime;
    private long pausedAt;
    private long totalPausedTime;
    private long countdownMillis;
    private boolean isCountdown;
    private State state;
    private Timeline countdownTimeline;

    /**
     * Creates a countdown timer with the specified time limit
     * @param countdownSeconds The time limit in seconds
     */
    public Timer(int countdownSeconds) {
        this.countdownMillis = countdownSeconds * 1000L; // Convert seconds to milliseconds
        this.isCountdown = true;
        this.state = State.READY;
        this.startTime = 0;
    }

    /**
     * Creates a timer for elapsed time tracking
     */
    public Timer() {
        this.isCountdown = false;
        this.state = State.READY;
        this.totalPausedTime = 0;
    }

    /**
     * Calculates the current time in milliseconds based on the timer's state
     * @return The current time in milliseconds
     */
    private long getCurrentTimeMillis() {
        if (state == State.READY) {
            return 0;
        } else if (state == State.PAUSED) {
            return pausedAt - startTime - totalPausedTime;
        } else {
            return System.currentTimeMillis() - startTime - totalPausedTime;
        }
    }

    /**
     * Gets the elapsed time in seconds
     * @return The elapsed time in seconds
     */
    public int getElapsedSeconds() {
        return (int) (getCurrentTimeMillis() / 1000);
    }

    /**
     * Notifies observers with the specified event
     * @param event The event to notify observers with
     */
    private void notifyObserversWithEvent(String event) {
        setChanged();
        notifyObservers(event);
    }

    /**
     * Starts the timer
     */
    public void startTimer() {
        if (state == State.READY || state == State.STOPPED) {
            this.startTime = System.currentTimeMillis();
            this.totalPausedTime = 0;
            this.state = State.RUNNING;
            notifyObserversWithEvent("TIMER_STARTED");

            // Only for countdown (timed) mode
            if (isCountdown) {
                if (countdownTimeline != null) {
                    countdownTimeline.stop();
                }
                countdownTimeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
                    if (getRemainingTime() <= 0 && state == State.RUNNING) {
                        stopTimer();
                    }
                }));
                countdownTimeline.setCycleCount(Timeline.INDEFINITE);
                countdownTimeline.play();
            }
        }
    }

    /**
     * Pauses the timer
     */
    public void pauseTimer() {
        if (state == State.RUNNING) {
            this.pausedAt = System.currentTimeMillis();
            this.state = State.PAUSED;
            notifyObserversWithEvent("TIMER_PAUSED");
        }
    }

    /**
     * Resumes the timer after it was paused
     */
    public void resumeTimer() {
        if (state == State.PAUSED) {
            this.totalPausedTime += (System.currentTimeMillis() - this.pausedAt);
            this.state = State.RUNNING;
            notifyObserversWithEvent("TIMER_RESUMED");
        }
    }

    /**
     * Stops the timer
     */
    public void stopTimer() {
        if (state == State.RUNNING || state == State.PAUSED) {
            this.state = State.STOPPED;

            // Only for countdown (timed) mode
            if (isCountdown && countdownTimeline != null) {
                countdownTimeline.stop();
            }
            notifyObserversWithEvent("TIMER_STOPPED");
        }
    }

    /**
     * Checks if the time is up (only for countdown timers)
     * @return True if the time is up, false otherwise
     * @throws UnsupportedOperationException If called on a non-countdown timer
     */
    public boolean isTimeUp() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support countdown.");
        }
        return state == State.RUNNING && getRemainingTime() <= 0;
    }

    /**
     * Gets the remaining time for countdown timers
     * @return The remaining time in milliseconds
     * @throws UnsupportedOperationException If called on a non-countdown timer
     */
    public long getRemainingTime() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support countdown.");
        }
        if (state == State.READY) {
            return countdownMillis;
        }
        return Math.max(countdownMillis - getCurrentTimeMillis(), 0);
    }

    /**
     * Gets the elapsed time
     * @return The elapsed time in milliseconds
     */
    public long getElapsedTime() {
        return getCurrentTimeMillis();
    }

    /**
     * Gets the formatted time string (MM:SS)
     * @return The time formatted as minutes and seconds
     */
    public String getFormatedTime() {
        long time = isCountdown ? getRemainingTime() : getElapsedTime();
        return formatTime(time);
    }

    /**
     * Formats the time in milliseconds as a string (MM:SS)
     * @param timeMillis The time in milliseconds
     * @return The formatted time string
     */
    private String formatTime(long timeMillis) {
        long totalSeconds = timeMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Checks if this is a countdown timer
     * @return True if this is a countdown timer, false otherwise
     */
    public boolean isCountdown() {
        return isCountdown;
    }

    /**
     * Checks if the timer is currently running
     * @return True if the timer is running, false otherwise
     */
    public boolean isRunning() {
        return state == State.RUNNING;
    }

    /**
     * Sets the time limit for a countdown timer
     * @param countdownSeconds The new time limit in seconds
     * @throws UnsupportedOperationException If called on a non-countdown timer
     */
    public void setTimeLimit(int countdownSeconds) {
        if (!isCountdown) {
            throw new UnsupportedOperationException("Cannot set time limit for elapsed-time timer.");
        }
        this.countdownMillis = countdownSeconds * 1000L;
        notifyObserversWithEvent("TIME_LIMIT_CHANGED");
    }

    /**
     * Gets the maximum time for countdown timers
     * @return The maximum time in milliseconds
     * @throws UnsupportedOperationException If called on a non-countdown timer
     */
    public long getMaxTime() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support maximum time.");
        }
        return countdownMillis;
    }

    /**
     * Resets the timer to its initial state
     */
    public void resetTimer() {
        this.state = State.READY;
        this.startTime = 0;
        notifyObserversWithEvent("TIMER_RESET");
    }
}

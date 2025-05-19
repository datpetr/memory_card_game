package main.cardgame.model;

import java.util.Observable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Timer extends Observable {
    // timer states to prevent incorrect triggering
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

    // Constructor for countdown timer
    public Timer(int countdownSeconds) {
        this.countdownMillis = countdownSeconds * 1000L; // Convert seconds to milliseconds
        this.isCountdown = true;
        this.state = State.READY;
        this.startTime = 0;
    }

    // Constructor for elapsed time tracking
    public Timer() {
        this.isCountdown = false;
        this.state = State.READY;
        this.totalPausedTime = 0;
    }

    public void startTimer() {
        if (state == State.READY || state == State.STOPPED) {
            this.startTime = System.currentTimeMillis();
            this.totalPausedTime = 0;
            this.state = State.RUNNING;

            setChanged();
            notifyObservers("TIMER_STARTED");

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

    public void pauseTimer() {
        if (state == State.RUNNING) {
            this.pausedAt = System.currentTimeMillis();
            this.state = State.PAUSED;

            setChanged();
            notifyObservers("TIMER_PAUSED");
        }
    }

    public void resumeTimer() {
        if (state == State.PAUSED) {
            this.totalPausedTime += (System.currentTimeMillis() - this.pausedAt);
            this.state = State.RUNNING;

            setChanged();
            notifyObservers("TIMER_RESUMED");
        }
    }

    public void stopTimer() {
        if (state == State.RUNNING || state == State.PAUSED) {
            this.state = State.STOPPED;

            // Only for countdown (timed) mode
            if (isCountdown && countdownTimeline != null) {
                countdownTimeline.stop();
            }

            setChanged();
            notifyObservers("TIMER_STOPPED");
        }
    }

    public boolean isTimeUp() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support countdown.");
        }
        return state == State.RUNNING && getRemainingTime() <= 0;
    }

    private long getCurrentTime() {
        if (state == State.READY) {
            return 0;
        } else if (state == State.PAUSED) {
            return pausedAt - startTime - totalPausedTime;
        } else {
            return System.currentTimeMillis() - startTime - totalPausedTime;
        }
    }

    public long getRemainingTime() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support countdown.");
        }
        if (state == State.READY) {
            return countdownMillis;
        }
        return Math.max(countdownMillis - getCurrentTime(), 0);
    }

    public long getElapsedTime() {
        if (isCountdown) {
            throw new UnsupportedOperationException("This timer only supports countdown.");
        }
        return getCurrentTime();
    }

    public String getFormatedTime() {
        long time = isCountdown ? getCurrentTime() : getElapsedTime();
        return formatTime(time);
    }

    private String formatTime(long timeMillis) {
        long totalSeconds = timeMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public boolean isCountdown() {
        return isCountdown;
    }

    public boolean isRunning() {
        return state == State.RUNNING;
    }

    public void setTimeLimit(int countdownSeconds) {
        if (!isCountdown) {
            throw new UnsupportedOperationException("Cannot set time limit for elapsed-time timer.");
        }
        this.countdownMillis = countdownSeconds * 1000L;

        setChanged();
        notifyObservers("TIME_LIMIT_CHANGED");
    }

    // Method to get the maximum time for countdown timer
    public long getMaxTime() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support maximum time.");
        }
        return countdownMillis;
    }

    public void resetTimer() {
        this.state = State.READY;
        this.startTime = 0;

        setChanged();
        notifyObservers("TIMER_RESET");
    }
}
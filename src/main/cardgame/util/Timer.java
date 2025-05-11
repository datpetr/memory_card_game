package main.cardgame.util;

public class Timer {
    private long startTime;
    private long countdownMillis;
    private boolean isCountdown;

    // Constructor for countdown timer
    public Timer(int countdownSeconds) {
        this.countdownMillis = countdownSeconds * 1000L; // Convert seconds to milliseconds
        this.isCountdown = true;
    }

    // Constructor for elapsed time tracking
    public Timer() {
        this.isCountdown = false;
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        this.startTime = 0; // Reset the start time
    }

    public boolean isTimeUp() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support countdown.");
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime >= countdownMillis;
    }

    public long getRemainingTime() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support countdown.");
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        return Math.max(countdownMillis - elapsedTime, 0);
    }

    public long getElapsedTime() {
        if (isCountdown) {
            throw new UnsupportedOperationException("This timer only supports countdown.");
        }
        return System.currentTimeMillis() - startTime;
    }

    public boolean isCountdown() {
        return isCountdown;
    }

    public void setTimeLimit(int countdownSeconds) {
        if (!isCountdown) {
            throw new UnsupportedOperationException("Cannot set time limit for elapsed-time timer.");
        }
        this.countdownMillis = countdownSeconds * 1000L;
    }

    // Method to get the maximum time for countdown timer
    public long getMaxTime() {
        if (!isCountdown) {
            throw new UnsupportedOperationException("This timer does not support maximum time.");
        }
        return countdownMillis;
    }

}
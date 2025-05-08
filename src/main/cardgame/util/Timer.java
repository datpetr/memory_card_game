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
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime;
    }

    public void setTimeLimit(int timeLimit) {
    }
}
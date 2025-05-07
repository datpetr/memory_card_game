package main.cardgame.util;

public class Timer {
    private long startTime;
    private long endTime;

    public Timer() {
        // Initialize timer
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        // will be added more code later
    }

    public void stopTimer() {
        endTime = System.currentTimeMillis();
        // will be added more code later
    }

    public long getElapsedTime() {
        return endTime - startTime;
        // will be added more code later
    }

    public boolean isTimeUp() {
        // Check if the time is up
        // will be added more code later
        return false;
    }

    public double getRemainingTime() {
        // Calculate remaining time
        // will be added more code later
        return 0;
    }
}
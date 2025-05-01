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
}
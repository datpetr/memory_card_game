package main.cardgame.util;

public class Timer {
    private long startTime;
    private long endTime;

    public Timer() {
        // Initialize timer
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }
}
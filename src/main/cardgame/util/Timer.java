package main.cardgame.util;

public class Timer {
    private long startTime;
    private long countdownMillis;

    public Timer(int countdownSeconds) {
        this.countdownMillis = countdownSeconds * 1000L; // Convert seconds to milliseconds
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean isTimeUp() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return elapsedTime >= countdownMillis;
    }

    public long getRemainingTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return Math.max(countdownMillis - elapsedTime, 0);
    }
}
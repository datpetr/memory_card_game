package main.cardgame.util;

public class ScoreManager {
    private int currentScore;

    public ScoreManager() {
        this.currentScore = 0;
    }

    public void incrementScore() {
        currentScore++;
    }

    public void resetScore() {
        currentScore = 0;
    }

    public int getScore() {
        return currentScore;
    }
}
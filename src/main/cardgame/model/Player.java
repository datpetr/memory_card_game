package main.cardgame.model;

public class Player {
    private String name;
    private int score;
    private int moves;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.moves = 0;
    }

    public Player() {
        this.name = "Unknown Player";
        this.score = 0;
        this.moves = 0;
    }

    public String getName() {
        // will be added more code later
        return name;
    }

    public int getScore() {
        // will be added more code later
        return score;
    }

    public void setScore(int newScore) {
        // will be added more code later
        this.score = newScore;
    }

    public void incrementScore(int points) {
        this.score += points;
    }

    public int getMoves() {
        // will be added more code later
        return moves;
    }

    public void incrementMoves() {
        // will be added more code later
        moves++;
    }
}

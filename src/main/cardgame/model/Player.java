package main.cardgame.model;

public class Player {
    private String name;
    private int score;
    private int moves;


    // default constructor
    public Player() {
        this.name = "Unknown Player";
        this.score = 0;
        this.moves = 0;
    }

    // constructor with name
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.moves = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void incrementScore(int points) {
        this.score++;
    }

    public int getMoves() {
        return moves;
    }

    public void incrementMoves() {
        this.moves++;
    }

    // since there is no sence in decreasing the number of movements, we don't need a decrement method
}

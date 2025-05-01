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

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public int getMoves() {
        return moves;
    }

    public void incrementMoves() {
        moves++;
    }
}

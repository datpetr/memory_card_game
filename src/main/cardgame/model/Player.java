package main.cardgame.model;

import java.util.Observable;

public class Player extends Observable {
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

    // Helper method to notify observers with an event
    private void notifyWithEvent(String eventType) {
        setChanged();
        notifyObservers(eventType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty() && !name.equals(this.name)) {
            this.name = name;
            notifyWithEvent("NAME_CHANGED");
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        if (newScore >= 0 && newScore != this.score) {
            this.score = newScore;
            notifyWithEvent("SCORE_CHANGED");
        }
    }

    public void incrementScore(int points) {
        if (points > 0) {
            this.score += points;
            notifyWithEvent("SCORE_INCREASED");
        }
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        if (moves >= 0 && moves != this.moves) {
            this.moves = moves;
            notifyWithEvent("MOVES_CHANGED");
        }
    }

    public void incrementMoves() {
        this.moves++;
        notifyWithEvent("MOVES_MADE");
    }

    /**
     * reset player score for a new game
     */
    public void resetStats() {
        this.score = 0;
        this.moves = 0;
        notifyWithEvent("PLAYER_RESET");
    }
}

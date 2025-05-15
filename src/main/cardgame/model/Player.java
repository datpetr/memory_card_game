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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            String oldName = this.name;
            this.name = name;

            if (!oldName.equals(this.name)) {
                setChanged();
                notifyObservers("NAME_CHANGED");
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        if (newScore >= 0) {
            int oldScore = score;
            this.score = newScore;

            if (oldScore != this.score) {
                setChanged();
                notifyObservers("SCORE_CHANGED");
            }
        }
    }

    public void incrementScore(int points) {
        if (points > 0) {
            this.score += points;
            setChanged();
            notifyObservers("SCORE_INCREASED");
        }
    }

    public int getMoves() {
        return moves;
    }

    public void incrementMoves() {
        this.moves++;
        setChanged();
        notifyObservers("MOVES_MADE");
    }

    /**
     * reset player score for a new game
     */
    public void resetStats() {
        this.score = 0;
        this.moves = 0;

        setChanged();
        notifyObservers("PLAYER_RESET");
    }
}

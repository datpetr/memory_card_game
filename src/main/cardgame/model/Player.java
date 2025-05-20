package main.cardgame.model;

import java.util.Observable;

/**
 * Represents a player in the memory card game.
 * Tracks player's name, score, and number of moves made during gameplay.
 */
public class Player extends Observable {
    private String name;
    private int score;
    private int moves;

    /**
     * Creates a new player with default name and reset statistics
     */
    public Player() {
        this.name = "Unknown Player";
        this.score = 0;
        this.moves = 0;
    }

    /**
     * Creates a new player with the specified name
     * @param name The player's name
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.moves = 0;
    }

    /**
     * Notifies observers with the specified event type
     * @param eventType The type of event that occurred
     */
    private void notifyWithEvent(String eventType) {
        setChanged();
        notifyObservers(eventType);
    }

    /**
     * Gets the player's name
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name
     * @param name The new name for the player
     */
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty() && !name.equals(this.name)) {
            this.name = name;
            notifyWithEvent("NAME_CHANGED");
        }
    }

    /**
     * Gets the player's current score
     * @return The player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the player's score to a new value
     * @param newScore The new score
     */
    public void setScore(int newScore) {
        if (newScore >= 0 && newScore != this.score) {
            this.score = newScore;
            notifyWithEvent("SCORE_CHANGED");
        }
    }

    /**
     * Increases the player's score by the specified number of points
     * @param points The number of points to add to the score
     */
    public void incrementScore(int points) {
        if (points > 0) {
            this.score += points;
            notifyWithEvent("SCORE_INCREASED");
        }
    }

    /**
     * Gets the number of moves the player has made
     * @return The number of moves
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Sets the number of moves the player has made
     * @param moves The new number of moves
     */
    public void setMoves(int moves) {
        if (moves >= 0 && moves != this.moves) {
            this.moves = moves;
            notifyWithEvent("MOVES_CHANGED");
        }
    }

    /**
     * Increments the number of moves the player has made by one
     */
    public void incrementMoves() {
        this.moves++;
        notifyWithEvent("MOVES_MADE");
    }

    /**
     * Resets player statistics (score and moves) for a new game
     */
    public void resetStats() {
        this.score = 0;
        this.moves = 0;
        notifyWithEvent("PLAYER_RESET");
    }
}

package main.cardgame.model;

/**
 * Interface defining the essential behavior of a card in the memory game.
 * Provides methods for accessing and manipulating card state.
 */

public interface CardBehavior {
    /**
     * Gets the path to the card's image
     * @return The path to the card's image
     */
    String getImagePath();

    /**
     * Checks if the card has been matched
     * @return True if the card has been matched, false otherwise
     */
    boolean isMatched();

    /**
     * Flips the card, changing its face up status
     */
    void flip();

    /**
     * Sets the matched status of the card
     * @param matched The new matched status
     */
    void setMatched(boolean matched);
}

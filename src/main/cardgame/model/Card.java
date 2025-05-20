package main.cardgame.model;

import java.util.Observable;

/**
 * Represents a card in the memory game.
 * Each card has an image, a matched state, and can be flipped.
 */

public class Card extends Observable implements CardBehavior{
    private int id;
    private String imagePath;
    private boolean isMatched;
    private boolean isFaceUp;
    private static String backImagePath;

    /**
     * Creates a new card with the specified id and image path
     * @param id The unique identifier for the card
     * @param imagePath The path to the card's image file
     */
    public Card(int id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
        this.isMatched = false;
        this.isFaceUp = false;
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
     * Gets the path to the card back image
     * @return The path to the back image
     */
    public static String getBackImagePath() {
        return backImagePath;
    }

    /**
     * Sets the path to the card back image
     * @param backImagePath The path to the back image
     */
    public static void setBackImagePath(String backImagePath) {
        Card.backImagePath = backImagePath;
    }

    /**
     * Gets the path to the card's image
     * @return The path to the card's image
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Sets the path to the card's image
     * @param imagePath The new path to the card's image
     */
    public void setImagePath(String imagePath) {
        if (imagePath != null && !imagePath.equals(this.imagePath)) {
            this.imagePath = imagePath;
            notifyWithEvent("IMAGE_CHANGED");
        }
    }

    /**
     * Checks if the card has been matched
     * @return True if the card has been matched, false otherwise
     */
    public boolean isMatched() {
        return isMatched;
    }

    /**
     * Sets the matched status of the card
     * @param matched The new matched status
     */
    public void setMatched(boolean matched) {
        if (this.isMatched != matched) {
            this.isMatched = matched;
            notifyWithEvent("MATCHED_STATUS_CHANGED");
        }
    }

    /**
     * Checks if the card is face up
     * @return True if the card is face up, false otherwise
     */
    public boolean isFaceUp() {
        return isFaceUp;
    }

    /**
     * Flips the card, changing its face up status
     */
    public void flip() {
        this.isFaceUp = !this.isFaceUp;
        notifyWithEvent("CARD_FLIPPED");
    }

    /**
     * Gets the card's unique identifier
     * @return The card's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the card's unique identifier
     * @param id The new ID for the card
     */
    public void setId(int id) {
        if (id != this.id) {
            this.id = id;
            notifyWithEvent("ID_CHANGED");
        }
    }

    /**
     * Resets the card to its initial state (face down, not matched)
     */
    public void reset() {
        boolean changed = false;
        if (isFaceUp) {
            isFaceUp = false;
            changed = true;
        }
        if (isMatched) {
            isMatched = false;
            changed = true;
        }
        if (changed) {
            notifyWithEvent("CARD_RESET");
        }
    }

    /**
     * Check if this card matches another card
     * @param otherCard card to compare with
     * @return true if the cards have the same image path
     */
    public boolean matches(Card otherCard) {
        if (otherCard == null) return false;
        return this.imagePath != null && this.imagePath.equals(otherCard.getImagePath());
    }
}

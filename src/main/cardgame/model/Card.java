package main.cardgame.model;

import java.util.Observable;

public class Card extends Observable implements CardBehavior{
    private int id;
    private String imagePath;
    private boolean isMatched;
    private boolean isFaceUp;
    private static String backImagePath;

    public Card(int id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
        this.isMatched = false;
        this.isFaceUp = false;
    }

    // Helper method to notify observers with an event
    private void notifyWithEvent(String eventType) {
        setChanged();
        notifyObservers(eventType);
    }

    public static String getBackImagePath() {
        return backImagePath;
    }

    public static void setBackImagePath(String backImagePath) {
        Card.backImagePath = backImagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        if (imagePath != null && !imagePath.equals(this.imagePath)) {
            this.imagePath = imagePath;
            notifyWithEvent("IMAGE_CHANGED");
        }
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        if (this.isMatched != matched) {
            this.isMatched = matched;
            notifyWithEvent("MATCHED_STATUS_CHANGED");
        }
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void flip() {
        this.isFaceUp = !this.isFaceUp;
        notifyWithEvent("CARD_FLIPPED");
    }

    public int getId() {
        return id;
    }

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

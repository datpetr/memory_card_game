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
        this.imagePath = imagePath; // Corrected from 'imagePath' to 'value'
        this.isMatched = false;
        this.isFaceUp = false;
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
        this.imagePath = imagePath;

        setChanged();
        notifyObservers("IMAGE_CHANGED");
    }


    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        boolean oldValue = this.isMatched;
        this.isMatched = matched;

        if (oldValue != this.isMatched) {
            setChanged();
            notifyObservers("MATCHED_STATUS_CHANGED");
        }
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void flip() {
        this.isFaceUp = !this.isFaceUp;

        setChanged();
        notifyObservers("CARD_FLIPPED");
    }

    public int getId() {
        return id;
    }

    /**
     * Gets the current display image path based on card state
     * @return path to image that should be displayed
     */
    public String getCurrentImagePath() {
        return isFaceUp ? imagePath : backImagePath;
    }

    /**
     * Resets the card to its initial state (face down, not matched)
     */
    public void reset() {
        if (isFaceUp) {
            isFaceUp = false;
        }
        if (isMatched) {
            isMatched = false;
        }
        setChanged();
        notifyObservers("CARD_RESET");
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

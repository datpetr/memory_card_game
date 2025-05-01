package main.cardgame.model;

public class Card {
    private int id;
    private String value;
    private boolean isMatched;
    private boolean isFaceUp;

    public Card(int id, String value) {
        this.id = id;
        this.value = value;
        this.isMatched = false;
        this.isFaceUp = false;
    }

    public String getValue() {
        return value;
        // will be added more code later
    }

    public boolean isMatched() {
        return isMatched;
        // will be added more code later
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
        // will be added more code later
    }

    public boolean isFaceUp() {
        return isFaceUp;
        // will be added more code later
    }

    public void flip() {
        isFaceUp = !isFaceUp;
        // will be added more code later
    }
}

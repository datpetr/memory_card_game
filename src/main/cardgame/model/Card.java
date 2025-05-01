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
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void flip() {
        isFaceUp = !isFaceUp;
    }
}

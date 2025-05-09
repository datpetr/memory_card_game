package main.cardgame.model;

public interface CardBehavior {
    String getImagePath();
    boolean isMatched();
    void flip();
    void setMatched(boolean matched);
}

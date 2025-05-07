package main.cardgame.model;

public interface CardBehavior {
    String getValue();
    boolean isMatched();
    void flip();
    void setMatched(boolean matched);
}

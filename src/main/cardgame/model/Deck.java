package main.cardgame.model;

import java.util.List;
import java.util.ArrayList;

public class Deck {
    private List<Card> cards;

    public Deck(String[] values) {
        // Create a deck of cards with pairs of values
        cards = new ArrayList<>();
        // Initialize cards
    }

    public void shuffle() {
        // Shuffle the deck
    }

    public List<Card> getCards() {
        return cards;
    }
}

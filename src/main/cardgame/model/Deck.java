package main.cardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck(String[] values) {
        // Create a deck of cards with pairs of values
        cards = new ArrayList<>();
        for(int i = 0; i < values.length; i++){
            cards.add(new Card(i, values[i])); //adding each card their unique ID and value
        }
        // Initialize cards
    }

    public void shuffle() {
        Collections.shuffle(cards); 
        // Shuffle the deck
    }

    public List<Card> getCards() {
        return cards;
        // will be added more code later
    }
}

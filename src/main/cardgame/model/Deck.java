package main.cardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards); // Create a copy of the input list
        this.cards.sort((card1, card2) -> Integer.compare(card1.getId(), card2.getId())); // Sort by id
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // Return a copy to preserve immutability
    }

    public List<Card> shuffle() {
        Collections.shuffle(cards); // Shuffle the original list in place
        return new ArrayList<>(cards); // Return a copy of the shuffled list
    }

    public Card findCardById(int id) {
        for (Card card : cards) {
            if (card.getId() == id) {
                return card; // Return the card if the id matches
            }
        }
        return null;
    }
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card); // Add the card to the deck
            cards.sort((card1, card2) -> Integer.compare(card1.getId(), card2.getId())); // Keep the deck sorted by ID
        }
    }

    public Deck createDeck(int numberOfPairs) {
        Deck deck = new Deck();
        int uniqueId = 1; // Start with a unique ID for each card
        for (int i = 1; i <= numberOfPairs; i++) {
            String imagePath = "file:src/main/resources/images/card" + i + ".png";
            deck.addCard(new Card(uniqueId++, imagePath)); // First card of the pair
            deck.addCard(new Card(uniqueId++, imagePath)); // Second card of the pair
        }
        deck.shuffle();
        return deck;
    }

}

package main.cardgame.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards); // Create a copy of the input list
        this.cards.sort((card1, card2) -> Integer.compare(card1.getId(), card2.getId())); // Sort by id
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // Return a copy to preserve immutability
    }

    public List<Card> shuffle() {
        List<Card> shuffledDeck = new ArrayList<>(cards); // Create a copy of the original deck
        for (int i = shuffledDeck.size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1)); // Random index between 0 and i
            // Swap shuffledDeck[i] with shuffledDeck[j]
            Card temp = shuffledDeck.get(i);
            shuffledDeck.set(i, shuffledDeck.get(j));
            shuffledDeck.set(j, temp);
        }
        return shuffledDeck; // Return the shuffled copy
    }

    public Card findCardById(int id) {
        for (Card card : cards) {
            if (card.getId() == id) {
                return card; // Return the card if the id matches
            }
        }
        return null;
    }
}

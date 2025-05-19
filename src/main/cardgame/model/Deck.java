package main.cardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class Deck extends Observable{
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards); // Create a copy of the input list
        sortById();
        }

    private void sortById() {
        cards.sort((card1, card2) -> Integer.compare(card1.getId(), card2.getId())); // Sort by id
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // Return a copy to preserve immutability
    }

    public List<Card> shuffle() {
        Collections.shuffle(cards);

        setChanged();
        notifyObservers("DECK_SHUFFLED");

        return new ArrayList<>(cards); // Return a copy of the shuffled list
    }

    public Card findCardById(int id) {
        return cards.stream()
                .filter(card -> card.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addCard(Card card) {
        if (card != null) {
            cards.add(card); // Add the card to the deck
            sortById();

            setChanged();
            notifyObservers("CARD_ADDED");
        }
    }

    /**
     * Creates a deck of cards with the specified number of pairs.
     */
    public static Deck createDeck(int numberOfPairs) {
        if (numberOfPairs <= 0) {
            throw new IllegalArgumentException("Number of pairs must be positive");
        }

        Deck deck = new Deck();
        int uniqueId = 1;

        for (int i = 1; i <= numberOfPairs; i++) {
            // Use modulo to cycle through available images if more pairs than images
            int imageNumber = ((i - 1) % 32) + 1;
            String imagePath = "file:src/main/resources/images/card" + imageNumber + ".png";

            // Create two cards with the same image path (they form a pair)
            deck.addCard(new Card(uniqueId++, imagePath));
            deck.addCard(new Card(uniqueId++, imagePath));
        }

        return deck;
    }

    public static Deck createDeckForLevel(String level) {
        int rows, cols;

        switch (level.toLowerCase()) {
            case "easy":
                rows = 4;
                cols = 3;
                break;
            case "medium":
                rows = 5;
                cols = 4;
                break;
            case "hard":
                rows = 6;
                cols = 5;
                break;
            default:
                rows = 4;
                cols = 3;
                break;
        }

        int totalCards = rows * cols;
        int pairs = totalCards / 2;

        Deck deck = createDeck(pairs);
        deck.shuffle();
        return deck;
    }

    /**
     * Find cards that match a given card (share the same image)
     * @param card the card to find matches for
     * @return list of cards with the same image path
     */
    public List<Card> findMatchingCards(Card card) {
        if (card == null) return Collections.emptyList();
        
        List<Card> matches = new ArrayList<>();
        for (Card c : cards) {
            if (c != card && card.matches(c)) {
                matches.add(c);
            }
        }
        return matches;
    }

    /**
     * Resets the deck to its initial state (all cards face down and unmatched)
     */

    public void reset() {
        for (Card card : cards) {
            card.reset(); // Reset each card
        }

        setChanged();
        notifyObservers("DECK_RESET");
    }


    /**
     * Get Number of cards in the deck
     */

    public int getPairsCount() {
        return cards.size() / 2; // Each pair consists of 2 cards
    }

    /**
     * Get Number of cards in the deck
     */
    public int getCardsCount() {
        return cards.size(); // Total number of cards
    }
}

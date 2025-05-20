package main.cardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

/**
 * Represents a deck of cards in the memory game.
 * Contains methods for creating, shuffling, and managing cards.
 */
public class Deck extends Observable {
    private List<Card> cards;

    /**
     * Creates an empty deck of cards
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Creates a deck with the given list of cards
     * @param cards The initial list of cards for the deck
     */
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards); // Create a copy of the input list
        sortById();
    }

    /**
     * Notifies observers with the specified event type
     * @param eventType The type of event that occurred
     */
    private void notifyWithEvent(String eventType) {
        setChanged();
        notifyObservers(eventType);
    }

    /**
     * Sorts the deck of cards by their ID
     */
    private void sortById() {
        cards.sort((card1, card2) -> Integer.compare(card1.getId(), card2.getId())); // Sort by id
    }

    /**
     * Gets the list of cards in the deck
     * @return A copy of the list of cards
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards); // Return a copy to preserve immutability
    }

    /**
     * Shuffles the cards in the deck
     * @return A copy of the shuffled list of cards
     */
    public List<Card> shuffle() {
        Collections.shuffle(cards);
        notifyWithEvent("DECK_SHUFFLED");
        return new ArrayList<>(cards); // Return a copy of the shuffled list
    }

    /**
     * Adds a card to the deck
     * @param card The card to add to the deck
     */
    public void addCard(Card card) {
        if (card != null) {
            cards.add(card); // Add the card to the deck
            sortById();
            notifyWithEvent("CARD_ADDED");
        }
    }

    /**
     * Creates a deck for the specified difficulty level
     * @param level The difficulty level ("easy", "medium", or "hard")
     * @return A new deck with the appropriate number of card pairs
     * @throws IllegalArgumentException If the specified level is invalid
     */
    public static Deck createDeckForLevel(String level) {
        int pairs;
        String folder;

        switch (level.toLowerCase()) {
            case "easy":
                pairs = 6;
                folder = "easy";
                break;
            case "medium":
                pairs = 10;
                folder = "medium";
                break;
            case "hard":
                pairs = 15;
                folder = "hard";
                break;
            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + level);
        }

        Deck deck = new Deck();
        int uniqueId = 1;
        for (int i = 1; i <= pairs; i++) {
            String imagePath = "file:src/main/resources/images/" + folder + "/card" + i + ".png";
            deck.addCard(new Card(uniqueId++, imagePath));
            deck.addCard(new Card(uniqueId++, imagePath));
        }
        deck.shuffle();
        return deck;
    }
}

package main.cardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class Deck extends Observable {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards); // Create a copy of the input list
        sortById();
    }

    // Helper method to notify observers with an event
    private void notifyWithEvent(String eventType) {
        setChanged();
        notifyObservers(eventType);
    }

    private void sortById() {
        cards.sort((card1, card2) -> Integer.compare(card1.getId(), card2.getId())); // Sort by id
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // Return a copy to preserve immutability
    }

    public List<Card> shuffle() {
        Collections.shuffle(cards);
        notifyWithEvent("DECK_SHUFFLED");
        return new ArrayList<>(cards); // Return a copy of the shuffled list
    }

    public void addCard(Card card) {
        if (card != null) {
            cards.add(card); // Add the card to the deck
            sortById();
            notifyWithEvent("CARD_ADDED");
        }
    }

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

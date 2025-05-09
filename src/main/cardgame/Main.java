package main.cardgame;

import main.cardgame.model.Card;
import main.cardgame.model.Deck;

public class Main {
    public static void main(String[] args) {
        // Set the common back image for all cards
        Deck deck = new Deck();
        deck = deck.createDeck(4); // Create a deck with 4 pairs of cards

        // Print the back image path
        System.out.println(deck.getCards().get(0).getBackImagePath());

        // Print the image paths of the cards in the deck
        deck.getCards().forEach(card -> System.out.println("Card ID: " + card.getId() + ", Image Path: " + card.getImagePath()));
    }
}
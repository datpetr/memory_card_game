package main.cardgame;

import main.cardgame.model.Card;

public class Main {
    public static void main(String[] args) {
        // Set the common back image for all cards
        Card.setBackImagePath("C:/Users/polya/Downloads/back.png");

        // Create individual cards with unique front images
        Card card1 = new Card(1, "images/card1.png");
        Card card2 = new Card(2, "images/card1.png");
        Card card3 = new Card(3, "images/card2.png");
        Card card4 = new Card(4, "images/card2.png");
        Card card5 = new Card(5, "images/card3.png");
        Card card6 = new Card(6, "images/card3.png");

        System.out.println(Card.getBackImagePath());

    }
}
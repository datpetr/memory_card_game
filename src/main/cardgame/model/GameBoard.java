package main.cardgame.model;

import java.util.List;

public class GameBoard {
    private Card[][] board;
    private int size;
    private Deck deck;

    public GameBoard(int size, List<Card> cards) {
        this.size = size;
        this.board = new Card[size][size];
        // Initialize the board with cards
        // will be added more code later
    }

    public Card getCard(int row, int col) {
        return board[row][col];
        // will be added more code later
    }

    public void displayBoard() {
        // Display the current state of the board
        // will be added more code later
    }

    public boolean allCardsMatched(Deck deck) {
        for (Card card : deck.getCards()) {
            if (!card.isMatched()) {
                return false; // If any card is not matched, return false
            }
        }
        return true; // All cards are matched
    }

    public void flipCard(int row, int col) {
        Card card = getCard(row, col);
        if (card != null) {
            card.flip(); // Flip the card
        }
    }

}

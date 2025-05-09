package main.cardgame.model;

import java.util.List;

public class GameBoard {
    private Card[][] board;
    private int size;
    private Deck deck;

    public GameBoard(String level, List<Card> cards) {
        this.size = determineSize(level);
        this.board = new Card[size][size];
        // Initialize the board with cards
        // will be added more code later
    }

    private int determineSize(String level) {
        switch (level.toLowerCase()) {
            case "easy":
                return 4;
            case "medium":
                return 6;
            case "hard":
                return 8;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    public Card getCard(int row, int col) {
        return board[row][col];
        // will be added more code later
    }

    public void displayBoard() {
        // Display the current state of the board
        // will be added more code later
    }

    public boolean allCardsMatched() {
        for (Card[] row : board) {
            for (Card card : row) {
                if (card != null && !card.isMatched()) {
                    return false; // If any card is not matched, return false
                }
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

    public void setGridSize(int gridSize) {
        this.size = gridSize;
        this.board = new Card[size][size]; // Reinitialize the board with the new size
        // will be added more code later
    }

    public int getGridSize() {
        return size;
    }

    public boolean checkMatch(Card card1, Card card2) {
        return card1.getImagePath().equals(card2.getImagePath());
    }

    public void initializeBoard(List<Card> cards) {
        int index = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = cards.get(index++);
            }
        }
    }
}

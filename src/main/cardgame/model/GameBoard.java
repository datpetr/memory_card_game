package main.cardgame.model;

import java.util.List;

public class GameBoard {
    private Card[][] board;
    private int size;

    public GameBoard(int size, List<Card> cards) {
        this.size = size;
        this.board = new Card[size][size];
        // Initialize the board with cards
    }

    public Card getCard(int row, int col) {
        return board[row][col];
    }

    public void displayBoard() {
        // Display the current state of the board
    }

    public boolean allCardsMatched() {
        // Check if all cards on the board are matched
        return false;
    }
}

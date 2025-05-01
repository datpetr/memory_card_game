package main.cardgame.model;

import java.util.List;

public class GameBoard {
    private Card[][] board;
    private int size;

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

    public boolean allCardsMatched() {
        // Check if all cards on the board are matched
        // will be added more code later
        return false;
    }
}

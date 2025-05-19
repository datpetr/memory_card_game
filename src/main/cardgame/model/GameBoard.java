package main.cardgame.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GameBoard extends Observable {
    private Card[][] board;
    private int rows;
    private int cols;
    private int matchedPairsCount = 0;
    private final int totalPairs;
    private Observer mainObserver; // Track the main observer for the board

    public GameBoard(String level, List<Card> cards) {
        this.rows = determineRows(level);
        this.cols = determineCols(level);
        this.board = new Card[rows][cols];
        this.totalPairs = (rows * cols) / 2;

        initializeBoard(cards);
    }

    public int determineRows(String level) {
        switch (level.toLowerCase()) {
            case "easy":
                return 3;
            case "medium":
                return 4;
            case "hard":
                return 5;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    public int determineCols(String level) {
        switch (level.toLowerCase()) {
            case "easy":
                return 4;
            case "medium":
                return 5;
            case "hard":
                return 6;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    public Card getCard(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean allCardsMatched() {
        return matchedPairsCount == totalPairs;
    }

    public void flipCard(int row, int col) {
        Card card = getCard(row, col);
        if (card != null && !card.isMatched()) {
            card.flip(); // Flip the card

            setChanged();
            notifyObservers("CARD_FLIPPED");
        }
    }

    public boolean checkMatch(Card card1, Card card2) {
        if (card1 == null || card2 == null) {
            return false;
        }

        boolean isMatch = card1.getImagePath().equals(card2.getImagePath());

        if (isMatch && !card1.isMatched() && !card2.isMatched()) {
            card1.setMatched(true);
            card2.setMatched(true);
            matchedPairsCount++;

            setChanged();
            notifyObservers("MATCH_FOUND");
        }

        return isMatch;
    }

    public void initializeBoard(List<Card> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Cards list cannot be null");
        }

        if (cards.size() != rows * cols) {
            throw new IllegalArgumentException(
                    "Invalid number of cards. Expected " + (rows * cols) +
                            " but got " + cards.size()
            );
        }

        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                board[row][col] = cards.get(index++);
            }
        }

        // Reset matched pairs when initializing a new board
        matchedPairsCount = 0;

        setChanged();
        notifyObservers("BOARD_INITIALIZED");
    }

    public void resetBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = board[row][col];
                if (card != null) {
                    card.flip(); // Flip the card back
                }
                if (card.isMatched()) {
                    card.setMatched(false);
                }
            }
        }
        matchedPairsCount = 0;

        setChanged();
        notifyObservers("BOARD_RESET");
    }

    public int getMatchedPairsCount() {
        return matchedPairsCount;
    }

    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    // For backward compatibility
    public int getGridSize() {
        return Math.max(rows, cols);
    }
    
    // Store the observer for retrieval later
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        this.mainObserver = o;
    }
    
    public Observer getObserver() {
        return mainObserver;
    }
}

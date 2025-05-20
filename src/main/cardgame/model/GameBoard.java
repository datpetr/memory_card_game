package main.cardgame.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents the game board for the memory card game.
 * Contains a grid of cards and tracks matched pairs.
 */
public class GameBoard extends Observable {
    private Card[][] board;
    private int rows;
    private int cols;
    private int matchedPairsCount = 0;
    private final int totalPairs;
    private Observer mainObserver; // Track the main observer for the board
    private Object observer;

    /**
     * Creates a new game board for the specified difficulty level with the given cards
     * @param level The difficulty level ("easy", "medium", or "hard")
     * @param cards The list of cards to place on the board
     */
    public GameBoard(String level, List<Card> cards) {
        this.rows = determineRows(level);
        this.cols = determineCols(level);
        this.board = new Card[rows][cols];
        this.totalPairs = (rows * cols) / 2;

        initializeBoard(cards);
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
     * Determines the number of rows based on the difficulty level
     * @param level The difficulty level ("easy", "medium", or "hard")
     * @return The number of rows for the game board
     * @throws IllegalArgumentException If the level is not valid
     */
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

    /**
     * Determines the number of columns based on the difficulty level
     * @param level The difficulty level ("easy", "medium", or "hard")
     * @return The number of columns for the game board
     * @throws IllegalArgumentException If the level is not valid
     */
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

    /**
     * Gets the card at the specified position
     * @param row The row index
     * @param col The column index
     * @return The card at the position, or null if the position is invalid
     */
    public Card getCard(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }

    /**
     * Checks if the position is valid on the game board
     * @param row The row index to check
     * @param col The column index to check
     * @return True if the position is valid, false otherwise
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Checks if all cards have been matched
     * @return True if all cards have been matched, false otherwise
     */
    public boolean allCardsMatched() {
        return matchedPairsCount == totalPairs;
    }

    /**
     * Checks if two cards match and updates their matched status if they do
     * @param card1 The first card to check
     * @param card2 The second card to check
     * @return True if the cards match, false otherwise
     */
    public boolean checkMatch(Card card1, Card card2) {
        if (card1 == null || card2 == null) {
            return false;
        }

        boolean isMatch = card1.getImagePath().equals(card2.getImagePath());

        if (isMatch && !card1.isMatched() && !card2.isMatched() && card1 != card2) {
            card1.setMatched(true);
            card2.setMatched(true);
            matchedPairsCount++;
            notifyWithEvent("MATCH_FOUND");
        }

        return isMatch;
    }

    /**
     * Initializes the game board with the given list of cards
     * @param cards The list of cards to place on the board
     * @throws IllegalArgumentException If the cards list is null or has an incorrect size
     */
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
        notifyWithEvent("BOARD_INITIALIZED");
    }

    /**
     * Gets the number of rows in the game board
     * @return The number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Sets the number of rows in the game board
     * @param rows The new number of rows
     */
    public void setRows(int rows) {
        if (rows > 0 && rows != this.rows) {
            this.rows = rows;
            notifyWithEvent("ROWS_CHANGED");
        }
    }

    /**
     * Gets the number of columns in the game board
     * @return The number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Sets the number of columns in the game board
     * @param cols The new number of columns
     */
    public void setCols(int cols) {
        if (cols > 0 && cols != this.cols) {
            this.cols = cols;
            notifyWithEvent("COLS_CHANGED");
        }
    }

    /**
     * Gets the count of matched pairs
     * @return The number of matched pairs
     */
    public int getMatchedPairsCount() {
        return matchedPairsCount;
    }

    /**
     * Sets the count of matched pairs
     * @param matchedPairsCount The new count of matched pairs
     */
    public void setMatchedPairsCount(int matchedPairsCount) {
        if (matchedPairsCount >= 0 && matchedPairsCount <= totalPairs && matchedPairsCount != this.matchedPairsCount) {
            this.matchedPairsCount = matchedPairsCount;
            notifyWithEvent("MATCHED_PAIRS_CHANGED");
        }
    }

    /**
     * Gets the total number of pairs in the game
     * @return The total number of pairs
     */
    public int getTotalPairs() {
        return totalPairs;
    }

    /**
     * Store the observer for retrieval later
     * @param o The observer to add
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        this.mainObserver = o;
    }

    /**
     * Gets the observer
     * @return The observer object
     */
    public Object getObserver() {
        return observer;
    }

    /**
     * Sets the observer
     * @param observer The new observer
     */
    public void setObserver(Object observer) {
        this.observer = observer;
    }
}

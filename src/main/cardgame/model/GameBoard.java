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
    private Object observer;

    public GameBoard(String level, List<Card> cards) {
        this.rows = determineRows(level);
        this.cols = determineCols(level);
        this.board = new Card[rows][cols];
        this.totalPairs = (rows * cols) / 2;

        initializeBoard(cards);
    }

    // Helper method to notify observers with an event
    private void notifyWithEvent(String eventType) {
        setChanged();
        notifyObservers(eventType);
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

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows > 0 && rows != this.rows) {
            this.rows = rows;
            notifyWithEvent("ROWS_CHANGED");
        }
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        if (cols > 0 && cols != this.cols) {
            this.cols = cols;
            notifyWithEvent("COLS_CHANGED");
        }
    }

    public int getMatchedPairsCount() {
        return matchedPairsCount;
    }

    public void setMatchedPairsCount(int matchedPairsCount) {
        if (matchedPairsCount >= 0 && matchedPairsCount <= totalPairs && matchedPairsCount != this.matchedPairsCount) {
            this.matchedPairsCount = matchedPairsCount;
            notifyWithEvent("MATCHED_PAIRS_CHANGED");
        }
    }

    public int getTotalPairs() {
        return totalPairs;
    }

    // Store the observer for retrieval later
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
        this.mainObserver = o;
    }

    public Object getObserver() {
        return observer;
    }

    public void setObserver(Object observer) {
        this.observer = observer;
    }
}

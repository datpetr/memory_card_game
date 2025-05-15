package main.cardgame.model;

import java.util.List;
import java.util.Observable;

public class GameBoard extends Observable {
    private Card[][] board;
    private int size;
    private int matchedPairsCount = 0;
    private final int totalPairs;



    public GameBoard(String level, List<Card> cards) {
        this.size = determineSize(level);
        this.board = new Card[size][size];
        this.totalPairs = (size * size) / 2;

        initializeBoard(cards);
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
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
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

    public void setGridSize(int gridSize) {
        this.size = gridSize;
        this.board = new Card[size][size]; // Reinitialize the board with the new size
        // will be added more code later
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

        if (cards.size() != size * size) {
            throw new IllegalArgumentException(
                    "Invalid number of cards. Expected " + (size * size) +
                            " but got " + cards.size()
            );
        }

        int index = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = cards.get(index++);
            }
        }

        // Reset matched pairs when initializing a new board
        matchedPairsCount = 0;

        setChanged();
        notifyObservers("BOARD_INITIALIZED");
    }

    public void resetBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
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

    public int getGridSize() {
        return size;
    }

}

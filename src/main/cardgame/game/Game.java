package main.cardgame.game;

import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.util.Logger;
import main.cardgame.util.ScoreManager;
import main.cardgame.util.Timer;


public abstract class Game {
    private GameBoard board;
    private Player player;
    private ScoreManager scoreManager;
    protected Timer timer;
    private Card firstSelection;
    private Card secondSelection;

    public Game(GameBoard board, Player player) {
        if (board == null || player == null) {
            throw new IllegalArgumentException("GameBoard and Player cannot be null.");
        }
        this.board = board;
        this.player = player;
        this.scoreManager = new ScoreManager(player);
        this.timer = new Timer();
    }

    public void play() {
        // Main game loop logic
    }

    public void processTurn(int row1, int col1, int row2, int col2) {
        Card firstCard = getBoard().getCard(row1, col1);
        Card secondCard = getBoard().getCard(row2, col2);

        if (firstCard != secondCard) {
            firstCard.flip();
            secondCard.flip();

            if (firstCard.getValue().equals(secondCard.getValue())) {
                firstCard.setMatched(true);
                secondCard.setMatched(true);
            } else {
                // Flip them back after a delay (simulate delay if needed)
                firstCard.flip();
                secondCard.flip();
            }
        }
    }

    public void pickCard(int row, int col) {
        getBoard().flipCard(row, col); // Flip the card at the specified position
    }

    public abstract boolean isGameOver();

    // Getters
    public GameBoard getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public Timer getTimer() {
        return timer;
    }

    public Card getFirstSelection() {
        return firstSelection;
    }

    public Card getSecondSelection() {
        return secondSelection;
    }

    // Setters
    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setFirstSelection(Card firstSelection) {
        this.firstSelection = firstSelection;
    }

    public void setSecondSelection(Card secondSelection) {
        this.secondSelection = secondSelection;
    }
}
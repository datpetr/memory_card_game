package main.cardgame.game;

import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.util.ScoreManager;
import main.cardgame.util.Timer;

/**
 * Abstract class representing a game of cards.
 * This class provides the basic structure and functionality for a card game.
 */

public abstract class Game {
    private GameBoard board;
    private Player player;
    private ScoreManager scoreManager;
    protected Timer timer;
    private Card firstSelection;
    private Card secondSelection;

    // Constructor for elapsed time-based games
    public Game(GameBoard board, Player player) {
        if (board == null || player == null) {
            throw new IllegalArgumentException("GameBoard and Player cannot be null.");
        }
        this.board = board;
        this.player = player;
        this.scoreManager = new ScoreManager(player);
        this.timer = new Timer(); // Default: elapsed time tracking
    }

    // Constructor for countdown-based games
    public Game(GameBoard board, Player player, int countdownSeconds) {
        if (board == null || player == null) {
            throw new IllegalArgumentException("GameBoard and Player cannot be null.");
        }
        this.board = board;
        this.player = player;
        this.scoreManager = new ScoreManager(player);
        this.timer = new Timer(countdownSeconds); // Countdown timer
    }

    public void play() {
        timer.startTimer();
        // Main game loop logic
    }

    public boolean processTurn(Card card1, Card card2) {
        boolean isMatch = getBoard().checkMatch(card1, card2);

        if (isMatch) {
            card1.setMatched(true);
            card2.setMatched(true);
            player.incrementScore(10);
        }

        player.incrementMoves();

        if (timer.isCountdown() && timer.isTimeUp()) {
            endGame();
        }

        return isMatch;
    }



    public void pickCard(int row, int col) {
        getBoard().flipCard(row, col); // Flip the card at the specified position
    }

    public abstract boolean isGameOver();

    public void endGame() {
        timer.stopTimer(); // Stop the timer
        System.out.println("main.cardgame.game.Game Over! Final score: " + player.getScore());
    }

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


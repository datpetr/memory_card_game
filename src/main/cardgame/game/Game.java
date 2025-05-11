package main.cardgame.game;

import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.util.ScoreManager;
import main.cardgame.util.Timer;

import java.util.Observable;

/**
 * Abstract class representing a card matching game.
 * Built with JavaFX integration in mind using Observable pattern for UI updates.
 */

public abstract class Game extends Observable {
    private GameBoard board;
    private Player player;
    private ScoreManager scoreManager;
    protected Timer timer;

    // Card selection for tracking
    private Card firstSelection;
    private Card secondSelection;

    // Game state flags
    private boolean isActive;
    private boolean isPaused;

    /**
     * Common initialization logic for both constructors
     */
    private void initializeGame(GameBoard board, Player player) {
        if (board == null || player == null) {
            throw new IllegalArgumentException("GameBoard and Player cannot be null.");
        }
        this.board = board;
        this.player = player;
        this.scoreManager = new ScoreManager(player);
        this.isActive = false;
        this.isPaused = false;
    }

    // Constructor for elapsed time-based games
    public Game(GameBoard board, Player player) {
        if (board == null || player == null) {
            throw new IllegalArgumentException("GameBoard and Player cannot be null.");
        }
        initializeGame(board, player);
        this.timer = new Timer();
    }

    // Constructor for countdown-based games
    public Game(GameBoard board, Player player, int countdownSeconds) {
        initializeGame(board, player);
        this.timer = new Timer(countdownSeconds);
    }

    // start the game and initialize the timer

    public void startGame() {
        this.isActive = true;
        this.timer.startTimer();

        // Notify observers that game has started
        setChanged();
        notifyObservers("GAME_STARTED");
    }

    // Pause the game
    public void pauseGame() {
        this.isPaused = true;

        // notify observers that game is paused
        setChanged();
        notifyObservers("GAME_PAUSED");
    }

    // Resume the game

    public void resumeGame() {
        this.isPaused = false;

        // notify observers that game is resumed
        setChanged();
        notifyObservers("GAME_RESUMED");
    }


    public void play() {
        timer.startTimer();
        // Main game loop logic
    }

    public boolean processTurn(Card card1, Card card2) {

        if (!isActive || isPaused) {
            return false; // Game is not active or is paused
        }

        boolean isMatch = getBoard().checkMatch(card1, card2);

        // Update the player stats
        player.incrementMoves();
        if (isMatch) {
//            card1.setMatched(true);
//            card2.setMatched(true);
            player.incrementScore(10);

            // Notify observers about the match
            setChanged();
            notifyObservers("MATCH_FOUND");
        } else {
            // Notify observers about the mismatch
            setChanged();
            notifyObservers("NO_MATCH");
        }

        if ((timer.isCountdown() && timer.isTimeUp()) || (getBoard().allCardsMatched())) {
            endGame();
        }

        return isMatch;
    }



    public void pickCard(int row, int col) {
        getBoard().flipCard(row, col); // Flip the card at the specified position
    }


    /**
     * Select a card at the given position
     * @return true if this completes a turn (second card selected)
     */

    public boolean selectCard(int row, int col) {
        if (!isActive || isPaused) return false;

        Card selectedCard = board.getCard(row, col);

        // Ignore if card is already matched or face-up
        if (selectedCard.isMatched() || selectedCard.isFaceUp()) {
            return false;
        }

        // Flip the card
        board.flipCard(row, col);

        // First selection of the turn
        if (firstSelection == null) {
            firstSelection = selectedCard;
            setChanged();
            notifyObservers("FIRST_CARD_SELECTED");
            return false;
        }
        // Second selection of the turn
        else {
            secondSelection = selectedCard;
            setChanged();
            notifyObservers("SECOND_CARD_SELECTED");

            // Process the turn
            boolean isMatch = processTurn(firstSelection, secondSelection);

            // Reset selections for next turn
            resetSelections();

            return true;
        }
    }
    /**
     * Check if the game is over
     */
    public abstract boolean isGameOver();

    /**
     * End the game and calculate final score
     */
    public void endGame() {
        this.isActive = false;
        timer.stopTimer();

        // Notify observers about game ending
        setChanged();
        notifyObservers("GAME_OVER");
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

    /**
     /**
     * Reset card selections for next turn
     */
    public void resetSelections() {
        // If cards don't match, they'll be flipped back in the UI
        firstSelection = null;
        secondSelection = null;
    }


    public boolean isActive() {
        return isActive;
    }

    public boolean isPaused() {
        return isPaused;
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
}


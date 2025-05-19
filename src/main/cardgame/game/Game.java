package main.cardgame.game;

import main.cardgame.stats.GameStatistics;

import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.model.Timer;

import java.util.Observable;

public abstract class Game extends Observable {
    private GameBoard board;
    private Player player;
    private Timer timer;
    private boolean isActive;
    private boolean isPaused;
    private Card firstCard;
    private Card secondCard;
    protected GameStatistics statistics = GameStatistics.loadFromDisk();

    /**
     * Constructor for standard game without countdown
     */
    public Game(GameBoard board, Player player) {
        this.board = board;
        this.player = player;
        this.timer = new Timer();  // Create a default timer that tracks elapsed time
        this.isActive = false;
        this.isPaused = false;
        this.firstCard = null;
        this.secondCard = null;
    }
    
    /**
     * Constructor for game with countdown timer
     */
    public Game(GameBoard board, Player player, int countdownSeconds) {
        this.board = board;
        this.player = player;
        this.timer = new Timer(countdownSeconds);  // Create a countdown timer
        this.isActive = false;
        this.isPaused = false;
        this.firstCard = null;
        this.secondCard = null;
    }

    private long startTime; // Add this field

    public void play() {
        this.isActive = true;
        this.isPaused = false;
        this.startTime = System.currentTimeMillis(); // Start timing
        this.timer.startTimer();
        setChanged();
        notifyObservers("GAME_STARTED");
    }
    // This method is commented out to avoid confusion with the new play() method
    /*public void play() {
        this.isActive = true;
        this.isPaused = false;  // Make sure game is not paused when started
        this.timer.startTimer();

        setChanged();
        notifyObservers("GAME_STARTED");
    }*/

    public void pause() {
        if (isActive) {
            this.isPaused = true;  // Set paused state
            this.timer.pauseTimer();
            setChanged();
            notifyObservers("GAME_PAUSED");
        }
    }

    public void resume() {
        if (isActive && isPaused) {  // Only resume if both active and paused
            this.isPaused = false;   // Clear paused state
            this.timer.resumeTimer();
            setChanged();
            notifyObservers("GAME_RESUMED");
        }
    }
    
    /**
     * Alias method for pause() - used by UI controls
     */
    public void pauseGame() {
        pause();
    }
    
    /**
     * Alias method for resume() - used by UI controls
     */
    public void resumeGame() {
        resume();
    }
    
    /**
     * Check if the game is currently paused
     * @return true if the game is paused
     */
    public boolean isPaused() {
        return isPaused;
    }

    public void endGame() {
        this.isActive = false;
        this.timer.stopTimer();

        long duration = System.currentTimeMillis() - startTime;
        int matches = board.getMatchedPairsCount();  // or count how many matches were made
        int moves = player.getMoves();

        statistics.updateGameStats(matches, moves, duration);

        setChanged();
        notifyObservers("GAME_OVER");
    }
    // This method is commented out to avoid confusion with the new endGame() method
    /*public void endGame() {
        this.isActive = false;
        this.timer.stopTimer();
        setChanged();
        notifyObservers("GAME_OVER");
    }*/

    public boolean processTurn(Card card1, Card card2) {
        if (card1 == null || card2 == null || !isActive) {
            return false;
        }

        // Increment moves counter
        player.incrementMoves();

        // Check if the cards match
        boolean isMatch = board.checkMatch(card1, card2);
        
        // Award points if it's a match
        if (isMatch) {
            player.incrementScore(10);  // Basic score for a match
        }

        // Check if game is over after this turn
        if (isGameOver()) {
            endGame();
        }

        return isMatch;
    }

    public void resetGame() {
        // Reset the player's stats
        player.resetStats();
        
        // Reset the board
        board.resetBoard();
        
        // Reset the timer
        timer.resetTimer();
        
        // Reset game state
        this.isActive = false;
        this.isPaused = false;  // Reset paused state
        this.firstCard = null;
        this.secondCard = null;
        
        setChanged();
        notifyObservers("GAME_RESET");
        
        // Start the game again
        play();
    }

    public abstract boolean isGameOver();

    public boolean isActive() {
        return isActive;
    }

    public GameBoard getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public Timer getTimer() {
        return timer;
    }
    
    public void setFirstCard(Card card) {
        this.firstCard = card;
    }
    
    public void setSecondCard(Card card) {
        this.secondCard = card;
    }
    
    public Card getFirstCard() {
        return firstCard;
    }

    public int getMoves() {
        return player.getMoves();
    }

    public int getMatches() {
        // Replace 'matches' with the actual field or logic that tracks matches
        return board.getMatchedPairsCount();
    }

    public Card getSecondCard() {
        return secondCard;
    }
    
    /**
     * Handles card selection at a specific position
     * @param row Row of the selected card
     * @param col Column of the selected card
     * @return True if the card was successfully picked, false otherwise
     */
    public boolean pickCard(int row, int col) {
        if (!isActive || isPaused) {
            return false;
        }
        
        Card card = board.getCard(row, col);
        if (card == null || card.isFaceUp() || card.isMatched()) {
            return false;
        }
        
        // Flip the selected card
        board.flipCard(row, col);
        
        // Handle card selection logic
        if (firstCard == null) {
            // This is the first card selected
            firstCard = card;
            setChanged();
            notifyObservers("FIRST_CARD_SELECTED");
            return true;
        } else if (secondCard == null) {
            // This is the second card selected
            secondCard = card;
            
            // Process the turn with the two cards
            boolean isMatch = processTurn(firstCard, secondCard);
            
            // If not a match, schedule cards to be flipped back
            if (!isMatch) {
                // In a real application, you would use a timer here,
                // but we'll leave that to the UI layer as it's already implemented there
                setChanged();
                notifyObservers("CARDS_DONT_MATCH");
            } else {
                setChanged();
                notifyObservers("CARDS_MATCH");
            }
            
            // Reset selected cards for next turn
            firstCard = null;
            secondCard = null;
            
            return true;
        }
        
        return false;
    }

    public GameStatistics getStatistics() {
        return statistics;
    }

}

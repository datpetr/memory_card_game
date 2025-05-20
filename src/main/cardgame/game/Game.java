package main.cardgame.game;

import main.cardgame.stats.GameStatistics;

import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.model.Timer;
import main.cardgame.profile.GlobalProfileContext;
import main.cardgame.profile.UserProfile;

import java.util.Observable;

public abstract class Game extends Observable {
    private GameBoard board;
    private Player player;
    private Timer timer;
    private boolean isActive;
    private boolean isPaused;
    private Card firstCard;
    private Card secondCard;
    protected GameStatistics statistics;
    private long startTime;

    // Helper method to notify observers with an event
    private void notifyWithEvent(String eventType) {
        setChanged();
        notifyObservers(eventType);
    }

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

    public void initializeStatistics() {
        UserProfile profile = GlobalProfileContext.getActiveProfile();
        this.statistics = (profile != null) ? profile.getStatistics() : new GameStatistics();
    }

    public void play() {
        initializeStatistics();
        this.isActive = true;
        this.isPaused = false;
        this.startTime = System.currentTimeMillis(); // Start timing
        this.timer.startTimer();
        notifyWithEvent("GAME_STARTED");
    }

    /**
     * Pauses the game and the timer
     */
    public void pause() {
        if (isActive && !isPaused) {
            this.isPaused = true;  // Set paused state
            this.timer.pauseTimer();
            notifyWithEvent("GAME_PAUSED");
        }
    }

    /**
     * Resumes the game after being paused
     */
    public void resume() {
        if (isActive && isPaused) {  // Only resume if both active and paused
            this.isPaused = false;   // Clear paused state
            this.timer.resumeTimer();
            notifyWithEvent("GAME_RESUMED");
        }
    }
    
    /**
     * Check if the game is currently paused
     * @return true if the game is paused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Ends the current game and updates statistics
     */
    public void endGame() {
        if (!isActive) return;

        this.isActive = false;
        this.timer.stopTimer();

        // Final stats
        int matches = board.getMatchedPairsCount();
        int moves = player.getMoves();
        int score = player.getScore(); // Get the actual player score
        long duration;
        if (timer.isCountdown()) {
            duration = timer.getMaxTime() - timer.getRemainingTime();
        } else {
            duration = timer.getElapsedTime();
        } // Use consistent timing method

        // Update and save statistics
        if (statistics != null) {
            // Determine if this is a timed game based on timer type
            boolean isTimedGame = timer.isCountdown();

            // Use the new method that accepts score and game type
            statistics.updateGameStats(matches, moves, duration, score, isTimedGame);

            UserProfile profile = GlobalProfileContext.getActiveProfile();
            if (profile != null) {
                try {
                    main.cardgame.profile.ProfileManager.saveProfile(profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Notify UI
        notifyWithEvent("GAME_OVER");
    }

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

    public int getMoves() {
        return player.getMoves();
    }

    public int getMatches() {
        return board.getMatchedPairsCount();
    }

    public GameStatistics getStatistics() {
        return statistics;
    }
}

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
    //protected GameStatistics statistics = GameStatistics.loadFromDisk();
    protected GameStatistics statistics;
    private long startTime;

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
        setChanged();
        notifyObservers("GAME_STARTED");
    }


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


    /**
     * Check if the game is currently paused
     * return true if the game is paused
     */
    public void endGame() {
        this.isActive = false;
        this.timer.stopTimer();

        // Final stats
        int matches = board.getMatchedPairsCount(); // or player.getMatchedPairs()
        int moves = player.getMoves();
        long duration;
        if (timer.isCountdown()) {
            duration = timer.getMaxTime() - timer.getRemainingTime();
        } else {
            duration = timer.getElapsedTime();
        } // Use consistent timing method

        // Update and save statistics
        if (statistics != null) {
            statistics.updateGameStats(matches, moves, duration);

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
        setChanged();
        notifyObservers("GAME_OVER");
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
        // Replace 'matches' with the actual field or logic that tracks matches
        return board.getMatchedPairsCount();
    }

    public GameStatistics getStatistics() {
        return statistics;
    }

}

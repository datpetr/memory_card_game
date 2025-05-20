package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.model.Card;

/**
 * Implementation of a timed game mode with countdown timer.
 * Players must find all matches before time runs out.
 * Includes time-based bonus points and unique scoring mechanisms.
 */
public class TimedGame extends Game {
    /** Time limit in seconds for easy difficulty */
    public static final int EASY_TIME = 60;   // 1 minute
    /** Time limit in seconds for medium difficulty */
    public static final int MEDIUM_TIME = 2 * 60; // 2 minutes
    /** Time limit in seconds for hard difficulty */
    public static final int HARD_TIME = 3 * 60; // 3 minute

    private boolean isStopped;

    /**
     * Creates a new timed game with the specified countdown
     * @param board The game board
     * @param player The player
     * @param countdownSeconds The time limit in seconds
     */
    public TimedGame(GameBoard board, Player player, int countdownSeconds) {
        super(board, player, countdownSeconds); // Pass GameBoard and playerName to the Game constructor
    }

    /**
     * Determines if the game is over based on:
     * 1. Game is no longer active
     * 2. All cards are matched
     * 3. Time has run out
     * @return True if the game is over, false otherwise
     */
    @Override
    public boolean isGameOver() {
        return !isActive() || getBoard().allCardsMatched() || getTimer().isTimeUp();
    }

    /**
     * Ends the game and applies time-based bonus points if the game
     * is won before the timer runs out
     */
    @Override
    public void endGame() {
        // Only apply time bonus if the game is still active and not timed out
        if (isActive() && !getTimer().isTimeUp()) {
            int remainingSeconds = (int) getTimer().getRemainingTime() / 1000;
            int timeBonus = remainingSeconds / 2;

            if (timeBonus > 0) {
                getPlayer().incrementScore(timeBonus);

                // Notify observers about the bonus
                setChanged();
                notifyObservers("TIME_BONUS_ADDED");
            }
        }

        // Always stop the timer
        getTimer().stopTimer();

        // Call parent end game method after bonuses are applied
        if (isActive()) {
            super.endGame();
        }
    }

    /**
     * Processes a turn when two cards are selected, adds time-pressure
     * bonus points for matches (more points when less time remains)
     * @param card1 The first selected card
     * @param card2 The second selected card
     * @return True if the cards match, false otherwise
     */
    @Override
    public boolean processTurn(Card card1, Card card2) {
        boolean result = super.processTurn(card1, card2);

        // Add time-based bonus points for matches
        if (result) {
            // Calculate bonus based on time pressure - more points when less time remains
            long maxTime = getTimer().getMaxTime();
            long remainingTime = getTimer().getRemainingTime();
            double timeRatio = (double)remainingTime / maxTime;

            // Time pressure bonus: higher points when less time remains
            int timePressureBonus = (int)(8 * (1 - timeRatio));

            // Award bonus points (minimum of 2)
            int bonus = Math.max(timePressureBonus, 2);
            getPlayer().incrementScore(bonus);

            // Notify observers about bonus points
            setChanged();
            notifyObservers("BONUS_POINTS_ADDED");
        }

        return result;
    }
}


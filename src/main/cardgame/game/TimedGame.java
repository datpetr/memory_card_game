package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.util.Timer;
import main.cardgame.model.Card;


public class TimedGame extends Game {
    public static final int EASY_TIME = 5 * 60;   // 5 minutes
    public static final int MEDIUM_TIME = 3 * 60; // 3 minutes
    public static final int HARD_TIME = 60; // 1 minute

    private boolean isStopped;

    public TimedGame(GameBoard board, Player player, int countdownSeconds) {
        super(board, player, countdownSeconds); // Pass GameBoard and playerName to the Game constructor
        this.isStopped = false;// Initialize the timer
    }

    public void startTimer() {
        getTimer().startTimer();
    }

    public void stopGame() {
        this.isStopped = true;
    }

    @Override
    public boolean isGameOver() {
        if (isStopped) {
            return true; // Game ends if stopped by the player
        }
        if (timer.isTimeUp()) {
            return true; // Game ends when the timer is up
        }
        if (getBoard().allCardsMatched()) {
            return true; // Game ends when all cards are matched
        }
        return false;
    }

    // creating the bonus points for remaining time
    @Override
    public void endGame() {
        super.endGame();

        if (!getTimer().isTimeUp()) {
            int remainingSeconds = (int) getTimer().getRemainingTime();
            getPlayer().incrementScore(remainingSeconds / 2);
        }
    }

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
        }

        return result;
    }
}


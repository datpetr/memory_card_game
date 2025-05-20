package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.model.Card;


public class TimedGame extends Game {
    public static final int EASY_TIME = 2 * 60;   // 2 minutes
    public static final int MEDIUM_TIME = 4 * 60; // 4 minutes
    public static final int HARD_TIME = 5 * 60; // 5 minute

    private boolean isStopped;

    public TimedGame(GameBoard board, Player player, int countdownSeconds) {
        super(board, player, countdownSeconds); // Pass GameBoard and playerName to the Game constructor
    }


    @Override
    public boolean isGameOver() {
        return !isActive() || getBoard().allCardsMatched() || getTimer().isTimeUp();
    }

    @Override
    public void endGame() {
        if (!getTimer().isTimeUp()) {
            int remainingSeconds = (int) getTimer().getRemainingTime() / 1000;
            getPlayer().incrementScore(remainingSeconds / 2);
        }
        getTimer().stopTimer(); // Always stop the timer

        // Prevent duplicate end logic
        if (isActive()) {
            super.endGame();
        }
    }


    public void startTimer() {
        getTimer().startTimer();
    }

    public void stopGame() {
        this.isStopped = true;

    }
    // creating the bonus points for remaining time

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


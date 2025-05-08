package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.util.Timer;

public class TimedGame extends Game {
    public static final int EASY_TIME = 5 * 60;   // 5 minutes
    public static final int MEDIUM_TIME = 3 * 60; // 3 minutes
    public static final int HARD_TIME = 60;

    private boolean isStopped;
    private Timer timer;// 1 minute

    public TimedGame(GameBoard board, Player player, int countdownSeconds) {
        super(board, player); // Pass GameBoard and playerName to the Game constructor
        this.timer = new Timer(countdownSeconds);
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
}


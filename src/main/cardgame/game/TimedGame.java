package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.util.Timer;

public class TimedGame extends Game {
    public static final int EASY_TIME = 5 * 60;   // 5 minutes
    public static final int MEDIUM_TIME = 3 * 60; // 3 minutes
    public static final int HARD_TIME = 60;   // 1 minute

    public TimedGame(int boardSize, String playerName, int countdownSeconds) {
        super(boardSize, playerName);
        this.timer = new Timer(countdownSeconds);
    }

    public void startTimer() {
        getTimer().startTimer();
    }

    public boolean isGameOver() {
        if (getTimer().isTimeUp()) {
            return true;
        }
        if (getBoard().allCardsMatched()) {
            return true;
        }
        return false;
    }
}


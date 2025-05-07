package main.cardgame.mode;

import main.cardgame.model.GameBoard;
import main.cardgame.util.Timer;
import main.cardgame.difficulty.DifficultyLevel;

public class TimeMode extends GameMode {
    public TimeMode(DifficultyLevel difficultyLevel) {
        super(difficultyLevel);
    }
    
    @Override
    public boolean isGameOver(GameBoard board, Timer timer) {
        // Game is over when all cards are matched OR time runs out
        return board.allCardsMatched() || timer.isTimeUp();
    }
    
    @Override
    public int calculateScore(int matches, int attempts, long timeElapsed) {
        // Time mode gives bonus points for remaining time
        int baseScore = difficultyLevel.calculateScore(matches, attempts, timeElapsed);
        int timeBonus = (int)(timer.getRemainingTime() / 5);
        return baseScore + timeBonus;
    }
    
    @Override
    public String getModeName() {
        return "Time Attack Mode";
    }
}

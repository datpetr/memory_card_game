package main.cardgame.mode;

import main.cardgame.model.GameBoard;
import main.cardgame.util.Timer;
import main.cardgame.difficulty.DifficultyLevel;

public abstract class GameMode {
    protected DifficultyLevel difficultyLevel;
    
    public GameMode(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public abstract boolean isGameOver(GameBoard board, Timer timer);
    public abstract int calculateScore(int matches, int attempts, long timeElapsed);
    public abstract String getModeName();
    
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
}

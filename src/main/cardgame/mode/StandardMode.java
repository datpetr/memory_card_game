package main.cardgame.mode;

import main.cardgame.model.GameBoard;
import main.cardgame.util.Timer;
import main.cardgame.difficulty.DifficultyLevel;

public class StandardMode extends GameMode {
    public StandardMode(DifficultyLevel difficultyLevel) {
        super(difficultyLevel);
    }
    
    @Override
    public boolean isGameOver(GameBoard board, Timer timer) {
        // Game is over when all cards are matched
        return board.allCardsMatched();
    }
    
    @Override
    public int calculateScore(int matches, int attempts, long timeElapsed) {
        // Standard scoring based on matches and attempts
        return difficultyLevel.calculateScore(matches, attempts, timeElapsed);
    }
    
    @Override
    public String getModeName() {
        return "Standard Mode";
    }
}

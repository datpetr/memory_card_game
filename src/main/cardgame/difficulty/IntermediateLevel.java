package main.cardgame.difficulty;

public class IntermediateLevel extends DifficultyLevel {
    public IntermediateLevel() {
        super(6, 90); // 6x6 board, 90 seconds time limit
    }
    
    @Override
    public int calculateScore(int matches, int attempts, long timeElapsed) {
        return matches * 15 - attempts;
    }
    
    @Override
    public int getHintCount() {
        return 2; // Intermediate players get 2 hints
    }
    
    @Override
    public String getName() {
        return "Intermediate";
    }
}

package main.cardgame.difficulty;

public class ExpertLevel extends DifficultyLevel {
    public ExpertLevel() {
        super(8, 60); // 8x8 board, 60 seconds time limit
    }
    
    @Override
    public int calculateScore(int matches, int attempts, long timeElapsed) {
        return matches * 20 - attempts * 2;
    }
    
    @Override
    public int getHintCount() {
        return 1; // Experts only get 1 hint
    }
    
    @Override
    public String getName() {
        return "Expert";
    }
}

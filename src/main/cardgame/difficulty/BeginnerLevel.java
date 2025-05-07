package main.cardgame.difficulty;

public class BeginnerLevel extends DifficultyLevel {
    public BeginnerLevel() {
        super(4, 120); // 4x4 board, 120 seconds time limit
    }
    
    @Override
    public int calculateScore(int matches, int attempts, long timeElapsed) {
        return matches * 10 - attempts / 2;
    }
    
    @Override
    public int getHintCount() {
        return 3; // Beginners get 3 hints
    }
    
    @Override
    public String getName() {
        return "Beginner";
    }
}

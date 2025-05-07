package main.cardgame.difficulty;

public abstract class DifficultyLevel {
    protected int boardSize;
    protected int timeLimit;
    
    public DifficultyLevel(int boardSize, int timeLimit) {
        this.boardSize = boardSize;
        this.timeLimit = timeLimit;
    }
    
    public abstract int calculateScore(int matches, int attempts, long timeElapsed);
    public abstract int getHintCount();
    public abstract String getName();
    
    public int getBoardSize() {
        return boardSize;
    }
    
    public int getTimeLimit() {
        return timeLimit;
    }
}

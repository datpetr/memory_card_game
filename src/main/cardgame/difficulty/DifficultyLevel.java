package main.cardgame.difficulty;

/**
 * Abstract base class for difficulty levels.
 */

public abstract class DifficultyLevel {
    // Common properties for difficulty levels
    protected int gridSize; // Grid size for the game (e.g., 4x4, 6x6)
    protected int timeLimit; // Time limit in seconds
    protected int maxMistakes; // Maximum allowable mistakes

    // Constructor to initialize difficulty properties
    public DifficultyLevel(int gridSize, int timeLimit, int maxMistakes) {
        this.gridSize = gridSize;
        this.timeLimit = timeLimit;
        this.maxMistakes = maxMistakes;
    }

    // Abstract method to configure game-specific settings
    public abstract void configureGameSettings();

    // Getters
    public int getGridSize() {
        return gridSize;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getMaxMistakes() {
        return maxMistakes;
    }
}
package main.cardgame.difficulty;

/**
 * Hard difficulty implementation.
 */
public class ExpertLevel extends DifficultyLevel {
    public ExpertLevel() {
        super(8, 120, 3); // 8x8 grid, 120 seconds, 3 mistakes
    }

    @Override
    public void configureGameSettings() {
        System.out.println("Configuring game for Expert Level...");
        // Additional expert-specific configuration can go here
    }
}
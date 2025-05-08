package main.cardgame.difficulty;

public class IntermediateLevel extends DifficultyLevel {
    public IntermediateLevel() {
        super(6, 180, 5); // 6x6 grid, 180 seconds, 5 mistakes
    }

    @Override
    public void configureGameSettings() {
        System.out.println("Configuring game for Intermediate Level...");
        // Additional intermediate-specific configuration can go here
    }
}

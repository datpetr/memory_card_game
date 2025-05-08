package main.cardgame.difficulty;

public class BeginnerLevel extends DifficultyLevel {
    public BeginnerLevel() {
        super(4, 300, Integer.MAX_VALUE); // 4 rows and 4 columns for beginner level
    }

    @Override
    public void configureGameSettings() {
        System.out.println("Setting up settings for " + getClass().getSimpleName());
    }
}

// src/main/cardgame/stats/StatsManager.java
package main.cardgame.stats;

/**
 * Singleton manager for game statistics.
 * Provides static methods to access and modify statistics across the application.
 */
public class StatsManager {
    /** Singleton instance of game statistics */
    private static GameStatistics stats = GameStatistics.loadFromDisk();

    /**
     * Gets the total number of games played
     * @return Total games played
     */
    public static int getGamesPlayed() {
        return stats.getTotalGames();
    }

    /**
     * Gets the fastest time to complete a game
     * @return Best time in seconds
     */
    public static int getBestTime() {
        return (int) stats.getBestTime();
    }

    /**
     * Gets the highest score achieved in any game mode
     * @return Best score
     */
    public static int getBestScore() {
        return stats.getBestScore();
    }

    /**
     * Gets the average number of moves per game
     * @return Average number of moves
     */
    public static double getAverageMoves() {
        return stats.getAverageMoves();
    }

    /**
     * Gets the average time spent per game
     * @return Average time in seconds
     */
    public static double getAverageTime() {
        return stats.getAverageTime();
    }

    /**
     * Records statistics from a completed game
     * @param matches Number of matches found
     * @param moves Number of moves made
     * @param timeInSeconds Time taken to complete the game
     */
    public static void recordGame(int matches, int moves, long timeInSeconds) {
        stats.updateGameStats(matches, moves, timeInSeconds);
    }

    /**
     * Resets all statistics to default values
     */
    public static void resetStats() {
        stats = new GameStatistics();
        stats.updateGameStats(0, 0, 0); // Optionally save/reset file
    }
}


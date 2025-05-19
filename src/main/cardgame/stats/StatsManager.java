// src/main/cardgame/stats/StatsManager.java
package main.cardgame.stats;

public class StatsManager {
    private static GameStatistics stats = GameStatistics.loadFromDisk();

    public static int getGamesPlayed() {
        return stats.getTotalGames();
    }

    public static int getBestTime() {
        return (int) stats.getBestTime();
    }

    public static int getBestScore() {
        return stats.getBestScore();
    }

    public static double getAverageMoves() {
        return stats.getAverageMoves();
    }

    public static double getAverageTime() {
        return stats.getAverageTime();
    }

    public static void recordGame(int matches, int moves, long timeInSeconds) {
        stats.updateGameStats(matches, moves, timeInSeconds);
    }

    public static void resetStats() {
        stats = new GameStatistics();
        stats.updateGameStats(0, 0, 0); // Optionally save/reset file
    }
}
package main.cardgame.stats;


import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Tracks and persists game statistics across multiple game sessions.
 * Stores aggregate data such as total games played, matches made, moves,
 * time spent playing, and best scores for different game modes.
 */

public class GameStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Total number of games played */
    private int totalGames;
    /** Total matches found across all games */
    private int totalMatches;
    /** Total moves made across all games */
    private int totalMoves;
    /** Total time spent playing (in milliseconds) */
    private long totalTime;
    /** Best player score (highest is better) */
    private int bestScore; // Best player score (highest is better)
    /** Best (lowest) time for completing a game */
    private long bestTime;

    /** Highest score achieved in timed mode */
    private int bestTimedScore; // Highest score in timed mode
    /** Highest score achieved in endless mode */
    private int bestEndlessScore; // Highest score in endless mode

    /** Path where statistics are saved */
    private static final String FILE_PATH = "src/main/resources/statistics.json";

    /**
     * Creates a new empty statistics object
     */
    public GameStatistics() {
    }

    /**
     * Loads game statistics from disk
     * @return The loaded statistics, or a new empty statistics object if none exists
     */
    public static GameStatistics loadFromDisk() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new GameStatistics();

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, GameStatistics.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new GameStatistics();
        }
    }

    /**
     * Updates game statistics when a game ends
     *
     * @param matches     Number of matches found
     * @param moves       Number of moves made
     * @param time        Time taken (or elapsed) in milliseconds
     * @param score       Player's final score
     * @param isTimedGame Whether this was a timed game
     */
    public void updateGameStats(int matches, int moves, long time, int score, boolean isTimedGame) {
        totalGames++;
        totalMatches += matches;
        totalMoves += moves;
        totalTime += time;

        // Update best score (highest is better)
        if (score > bestScore) {
            bestScore = score;
        }

        // Update game-specific best scores
        if (isTimedGame) {
            if (score > bestTimedScore) {
                bestTimedScore = score;
            }
        } else {
            if (score > bestEndlessScore) {
                bestEndlessScore = score;
            }
        }

        // Update best time (lowest is better)
        if (bestTime == 0 || time < bestTime) {
            bestTime = time;
        }

        save();
    }

    /**
     * Legacy method for backward compatibility
     */
    public void updateGameStats(int matches, int moves, long time) {
        totalGames++;
        totalMatches += matches;
        totalMoves += moves;
        totalTime += time;

        // For legacy code, we assume best moves is the best score (lower is better)
        if (bestScore == 0 || moves < bestScore) {
            bestScore = moves;
        }

        if (bestTime == 0 || time < bestTime) {
            bestTime = time;
        }

        save();
    }

    /**
     * Calculates the average number of moves per game
     * @return The average number of moves, or 0 if no games have been played
     */
    public double getAverageMoves() {
        return totalGames == 0 ? 0 : (double) totalMoves / totalGames;
    }

    /**
     * Calculates the average time spent per game
     * @return The average time in milliseconds, or 0 if no games have been played
     */
    public double getAverageTime() {
        return totalGames == 0 ? 0 : (double) totalTime / totalGames;
    }

    /**
     * Gets the total number of games played
     * @return The total number of games
     */
    public int getTotalGames() {
        return totalGames;
    }

    /**
     * Gets the total number of matches found
     * @return The total number of matches
     */
    public int getTotalMatches() {
        return totalMatches;
    }

    /**
     * Gets the total number of moves made
     * @return The total number of moves
     */
    public int getTotalMoves() {
        return totalMoves;
    }

    /**
     * Gets the total time spent playing
     * @return The total time in milliseconds
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * Gets the best score achieved in any game mode
     * @return The best score
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Gets the best score achieved in timed game mode
     * @return The best timed mode score
     */
    public int getBestTimedScore() {
        return bestTimedScore;
    }

    /**
     * Gets the best score achieved in endless game mode
     * @return The best endless mode score
     */
    public int getBestEndlessScore() {
        return bestEndlessScore;
    }

    /**
     * Gets the best (lowest) time achieved for completing a game
     * @return The best time in milliseconds
     */
    public long getBestTime() {
        return bestTime;
    }

    /**
     * Saves statistics to disk
     */
    private void save() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads statistics from disk
     */
    private void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            GameStatistics loaded = gson.fromJson(reader, GameStatistics.class);

            // Copy loaded values
            this.totalGames = loaded.totalGames;
            this.totalMatches = loaded.totalMatches;
            this.totalMoves = loaded.totalMoves;
            this.totalTime = loaded.totalTime;
            this.bestScore = loaded.bestScore;
            this.bestTime = loaded.bestTime;

            // Handle potentially missing fields in older saved data
            if (loaded.bestTimedScore != 0) this.bestTimedScore = loaded.bestTimedScore;
            if (loaded.bestEndlessScore != 0) this.bestEndlessScore = loaded.bestEndlessScore;
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}


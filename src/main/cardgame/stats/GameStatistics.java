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

    /** Add a field for best moves (lowest is better) */
    private int bestMoves = Integer.MAX_VALUE;

    /** Best (lowest) moves for timed mode */
    private int bestTimedMoves = Integer.MAX_VALUE;
    /** Best (lowest) moves for endless mode */
    private int bestEndlessMoves = Integer.MAX_VALUE;
    /** Best (lowest) time for timed mode */
    private long bestTimedTime = Long.MAX_VALUE;
    /** Best (lowest) time for endless mode */
    private long bestEndlessTime = Long.MAX_VALUE;

    // Add fields for per-mode totals
    private int timedGamesPlayed;
    private int endlessGamesPlayed;
    private int totalTimedMoves;
    private int totalEndlessMoves;
    private long totalTimedTime;
    private long totalEndlessTime;

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
            GameStatistics loaded = gson.fromJson(reader, GameStatistics.class);
            // Defensive: ensure best scores are not negative or zero if previously set
            if (loaded.bestTimedScore < 0) loaded.bestTimedScore = 0;
            if (loaded.bestEndlessScore < 0) loaded.bestEndlessScore = 0;
            return loaded;
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
     * @param isWin       Whether the game was won
     */
    public void updateGameStats(int matches, int moves, long time, int score, boolean isTimedGame, boolean isWin) {
        totalGames++;
        totalMatches += matches;
        totalMoves += moves;
        totalTime += time;

        if (isTimedGame) {
            timedGamesPlayed++;
            totalTimedMoves += moves;
            totalTimedTime += time;
        } else {
            endlessGamesPlayed++;
            totalEndlessMoves += moves;
            totalEndlessTime += time;
        }

        // Only update bests if the game was won
        if (isWin) {
            // Update best score (highest is better)
            if (score > bestScore) {
                bestScore = score;
            }
            // Update best moves (lowest is better)
            if (moves < bestMoves) {
                bestMoves = moves;
            }
            // Update game-specific best scores and moves/times
            if (isTimedGame) {
                if (score > bestTimedScore) {
                    bestTimedScore = score;
                }
                if (moves < bestTimedMoves) {
                    bestTimedMoves = moves;
                }
                if (time < bestTimedTime) {
                    bestTimedTime = time;
                }
            } else {
                if (score > bestEndlessScore) {
                    bestEndlessScore = score;
                }
                if (moves < bestEndlessMoves) {
                    bestEndlessMoves = moves;
                }
                if (time < bestEndlessTime) {
                    bestEndlessTime = time;
                }
            }
            // Update best time (lowest is better)
            if (time < bestTime || bestTime == 0) {
                bestTime = time;
            }
        }
        save();
    }

    /**
     * Legacy method for backward compatibility
     */
    /*public void updateGameStats(int matches, int moves, long time) {
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
    }*/

    /**
     * Calculates the average number of moves per game
     * @return The average number of moves, or 0 if no games have been played
     */
    public double getAverageMoves() {
        return totalGames == 0 ? 0 : (double) totalMoves / totalGames;
    }

    /**
     * Calculates the average number of moves per timed games
     */
    public double getAverageTimedMoves() {
        int timedGames = getTimedGamesPlayed();
        int timedMoves = totalTimedMoves;
        return timedGames == 0 ? 0 : (double) timedMoves / timedGames;
    }

    /**
     * Calculates the average number of moves per endless games
     */
    public double getAverageEndlessMoves() {
        int endlessGames = getEndlessGamesPlayed();
        int endlessMoves = totalEndlessMoves;
        return endlessGames == 0 ? 0 : (double) endlessMoves / endlessGames;
    }

    /**
     * Calculates the average time spent per game
     * @return The average time in milliseconds, or 0 if no games have been played
     */
    public double getAverageTime() {
        return totalGames == 0 ? 0 : (double) totalTime / totalGames;
    }

    /**
     * Calculates the average time per timed games (in ms)
     */
    public double getAverageTimedTime() {
        int timedGames = getTimedGamesPlayed();
        long timedTime = totalTimedTime;
        return timedGames == 0 ? 0 : (double) timedTime / timedGames;
    }

    /**
     * Calculates the average time per endless games (in ms)
     */
    public double getAverageEndlessTime() {
        int endlessGames = getEndlessGamesPlayed();
        long endlessTime = totalEndlessTime;
        return endlessGames == 0 ? 0 : (double) endlessTime / endlessGames;
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
     * @return The best score (higher is better)
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Gets the best (lowest) moves achieved in any game mode
     * @return The best moves (lower is better)
     */
    public int getBestMoves() {
        return bestMoves == Integer.MAX_VALUE ? 0 : bestMoves;
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

    public int getBestTimedMoves() { return bestTimedMoves == Integer.MAX_VALUE ? 0 : bestTimedMoves; }
    public int getBestEndlessMoves() { return bestEndlessMoves == Integer.MAX_VALUE ? 0 : bestEndlessMoves; }
    public long getBestTimedTime() { return bestTimedTime == Long.MAX_VALUE ? 0 : bestTimedTime; }
    public long getBestEndlessTime() { return bestEndlessTime == Long.MAX_VALUE ? 0 : bestEndlessTime; }

    /**
     * Gets the best (lowest) time achieved for completing a game
     * @return The best time in milliseconds (as a double, in seconds, matching average time)
     */
    public double getBestTime() {
        return bestTime == 0 ? 0 : (double) bestTime / 1000;
    }

    /**
     * Returns the number of timed games played
     */
    public int getTimedGamesPlayed() { return timedGamesPlayed; }
    /**
     * Returns the number of endless games played
     */
    public int getEndlessGamesPlayed() { return endlessGamesPlayed; }

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


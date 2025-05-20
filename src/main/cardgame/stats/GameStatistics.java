package main.cardgame.stats;


import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GameStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    private int totalGames;
    private int totalMatches;
    private int totalMoves;
    private long totalTime;
    private int bestScore; // Best player score (highest is better)
    private long bestTime;

    // Track high scores separately for timed and endless games
    private int bestTimedScore; // Highest score in timed mode
    private int bestEndlessScore; // Highest score in endless mode

    private static final String FILE_PATH = "statistics.json";

    public GameStatistics() {
    }

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

    public double getAverageMoves() {
        return totalGames == 0 ? 0 : (double) totalMoves / totalGames;
    }

    public double getAverageTime() {
        return totalGames == 0 ? 0 : (double) totalTime / totalGames;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public int getBestScore() {
        return bestScore;
    }

    public int getBestTimedScore() {
        return bestTimedScore;
    }

    public int getBestEndlessScore() {
        return bestEndlessScore;
    }

    public long getBestTime() {
        return bestTime;
    }

    private void save() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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


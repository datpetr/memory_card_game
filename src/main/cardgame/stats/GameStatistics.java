package main.cardgame.stats;


import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GameStatistics implements Serializable{
    private static final long serialVersionUID = 1L;
    private int totalGames;
    private int totalMatches;
    private int totalMoves;
    private long totalTime;
    private int bestScore;
    private long bestTime;

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


    public void updateGameStats(int matches, int moves, long time) {
        totalGames++;
        totalMatches += matches;
        totalMoves += moves;
        totalTime += time;

        if (bestScore == 0 || moves < bestScore) bestScore = moves;
        if (bestTime == 0 || time < bestTime) bestTime = time;

        save();
    }

    public double getAverageMoves() {
        return totalGames == 0 ? 0 : (double) totalMoves / totalGames;
    }

    public double getAverageTime() {
        return totalGames == 0 ? 0 : (double) totalTime / totalGames;
    }

    public int getTotalGames() { return totalGames; }
    public int getTotalMatches() { return totalMatches; }
    public int getTotalMoves() { return totalMoves; }
    public long getTotalTime() { return totalTime; }
    public int getBestScore() { return bestScore; }
    public long getBestTime() { return bestTime; }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

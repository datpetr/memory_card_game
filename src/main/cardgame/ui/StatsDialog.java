package main.cardgame.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.cardgame.stats.StatsManager;

public class StatsDialog extends Alert {
    public StatsDialog() {
        super(AlertType.INFORMATION);
        setTitle("Game Stats");
        setHeaderText("Your Statistics");
        int gamesPlayed = StatsManager.getGamesPlayed();
        int bestTime = StatsManager.getBestTime();
        setContentText(String.format("Games Played: %d\nBest Time: %02d:%02d", gamesPlayed, bestTime / 60, bestTime % 60));
    }
}

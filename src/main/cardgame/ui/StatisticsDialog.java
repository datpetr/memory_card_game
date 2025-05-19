package main.cardgame.ui;

import javafx.scene.control.Alert;
import main.cardgame.stats.StatsManager;

public class StatisticsDialog extends Alert {
    public StatisticsDialog() {
        super(AlertType.INFORMATION);
        setTitle("Game Statistics");
        setHeaderText("Your Game Statistics");
        setContentText(
                "Games Played: " + StatsManager.getGamesPlayed() + "\n" +
                        "Best Time: " + StatsManager.getBestTime() + "s\n" +
                        "Best Score: " + StatsManager.getBestScore() + "\n" +
                        "Average Moves: " + String.format("%.2f", StatsManager.getAverageMoves()) + "\n" +
                        "Average Time: " + String.format("%.2f", StatsManager.getAverageTime()) + "s"
        );
    }
}

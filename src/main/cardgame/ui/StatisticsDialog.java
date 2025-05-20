/*package main.cardgame.ui;

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
}*/
package main.cardgame.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.cardgame.stats.GameStatistics;
import main.cardgame.profile.UserProfile;

public class StatisticsDialog extends Alert {
    public StatisticsDialog(UserProfile profile) {
        super(AlertType.INFORMATION);
        setTitle("Player Statistics");

        GameStatistics stats = profile.getStatistics();

        String content = "Games Played: " + stats.getTotalGames() + "\n" +
                "Best Score: " + stats.getBestScore() + " moves\n" +
                "Best Time: " + stats.getBestTime() + " sec\n" +
                "Average Moves: " + String.format("%.2f", stats.getAverageMoves()) + "\n" +
                "Average Time: " + String.format("%.2f", stats.getAverageTime()) + " sec";

        setHeaderText("Statistics for " + profile.getUsername());
        setContentText(content);
    }
}

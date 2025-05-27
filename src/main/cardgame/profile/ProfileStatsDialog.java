package main.cardgame.profile;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.cardgame.profile.ProfileManager;
import main.cardgame.profile.UserProfile;
import main.cardgame.stats.GameStatistics;

import java.io.IOException;
import java.util.List;

public class ProfileStatsDialog extends Dialog<Void> {
    public ProfileStatsDialog() {
        setTitle("View Profile Statistics");

        ComboBox<String> profileSelector = new ComboBox<>();
        profileSelector.getItems().addAll(ProfileManager.listProfiles());
        profileSelector.setPromptText("Select a profile");

        TextArea statsLabel = new TextArea();
        statsLabel.setEditable(false);
        statsLabel.setWrapText(true);
        statsLabel.setPrefRowCount(20); // Make it longer
        statsLabel.setPrefColumnCount(15); // Make it less wide


        profileSelector.setOnAction(e -> {
            String selectedName = profileSelector.getValue();
            if (selectedName != null) {
                try {
                    UserProfile profile = ProfileManager.loadProfile(selectedName);
                    GameStatistics stats = profile.getStatistics();

                    statsLabel.setText(
                            "Username: " + profile.getUsername() + "\n" +
                            "Games Played: " + stats.getTotalGames() + "\n\n" +
                            "--- Timed Mode ---\n" +
                            "Best Score: " + stats.getBestTimedScore() + "\n" +
                            "Best Moves: " + (stats.getBestTimedMoves() == Integer.MAX_VALUE ? "-" : stats.getBestTimedMoves()) + "\n" +
                            "Best Time: " + (stats.getBestTimedTime() == Long.MAX_VALUE ? "-" : (stats.getBestTimedTime() / 1000) + " sec") + "\n" +
                            "Average Moves: " + String.format("%.2f", stats.getAverageTimedMoves()) + "\n" +
                            "Average Time: " + String.format("%.2f", (stats.getAverageTimedTime() / 1000)) + " sec\n\n" +
                            "--- Endless Mode ---\n" +
                            "Best Score: " + stats.getBestEndlessScore() + "\n" +
                            "Best Moves: " + (stats.getBestEndlessMoves() == Integer.MAX_VALUE ? "-" : stats.getBestEndlessMoves()) + "\n" +
                            "Best Time: " + (stats.getBestEndlessTime() == Long.MAX_VALUE ? "-" : (stats.getBestEndlessTime() / 1000) + " sec") + "\n" +
                            "Average Moves: " + String.format("%.2f", stats.getAverageEndlessMoves()) + "\n" +
                            "Average Time: " + String.format("%.2f", (stats.getAverageEndlessTime() / 1000)) + " sec\n\n" +
                            "Average Moves (All): " + String.format("%.2f", stats.getAverageMoves()) + "\n" +
                            "Average Time (All): " + String.format("%.2f", (stats.getAverageTime() / 1000)) + " sec"
                    );
                } catch (IOException | ClassNotFoundException ex) {
                    statsLabel.setText("Failed to load profile.");
                    ex.printStackTrace();
                }
            }
        });

        VBox content = new VBox(15, profileSelector, statsLabel);
        content.setPadding(new Insets(30, 20, 30, 20)); // Less wide, more vertical padding

        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }
}


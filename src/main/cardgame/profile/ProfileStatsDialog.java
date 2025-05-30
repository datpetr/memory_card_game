package main.cardgame.profile;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.cardgame.profile.ProfileManager;
import main.cardgame.profile.UserProfile;
import main.cardgame.stats.GameStatistics;

import java.io.IOException;
import java.util.List;

/**
 * Dialog for displaying user profile statistics.
 * Allows selecting a profile and viewing its associated game statistics.
 */
public class ProfileStatsDialog extends Dialog<Void> {
    /**
     * Creates a new profile statistics dialog.
     * Initializes the UI components for profile selection and statistics display.
     */
    public ProfileStatsDialog() {
        setTitle("View Profile Statistics");

        ComboBox<String> profileSelector = new ComboBox<>();
        profileSelector.getItems().addAll(ProfileManager.listProfiles());
        profileSelector.setPromptText("Select a profile");

        TextArea statsLabel = new TextArea();
        statsLabel.setEditable(false);
        statsLabel.setWrapText(true);
        statsLabel.setPrefRowCount(6);


        profileSelector.setOnAction(e -> {
            String selectedName = profileSelector.getValue();
            if (selectedName != null) {
                try {
                    UserProfile profile = ProfileManager.loadProfile(selectedName);
                    GameStatistics stats = profile.getStatistics();

                    statsLabel.setText(
                            "Username: " + profile.getUsername() + "\n" +
                                    "Games Played: " + stats.getTotalGames() + "\n" +
                                    "Best Score: " + stats.getBestScore() + " moves\n" +
                                    "Best Time: " + stats.getBestTime() / 1000 + " sec\n" +
                                    "Average Moves: " + String.format("%.2f", stats.getAverageMoves()) + "\n" +
                                    "Average Time: " + String.format("%.2f", (stats.getAverageTime() / 1000)) + " sec"
                    );
                } catch (IOException | ClassNotFoundException ex) {
                    statsLabel.setText("Failed to load profile.");
                    ex.printStackTrace();
                }
            }
        });

        VBox content = new VBox(15, profileSelector, statsLabel);
        content.setPadding(new Insets(50));

        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    }
}


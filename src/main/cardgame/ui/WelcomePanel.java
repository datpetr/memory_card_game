package main.cardgame.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.cardgame.profile.ProfileStatsDialog;
import main.cardgame.stats.StatsManager;
import main.cardgame.profile.ProfileSelectionDialog;
import main.cardgame.profile.GlobalProfileContext;
import main.cardgame.profile.UserProfile;


/**
 * Welcome screen for the Memory Card Game.
 * This is the initial screen shown when the game starts.
 * Provides options to play the game, view statistics, and manage user profiles.
 * Preserves the original UI components and behavior from GameBoardVisualizer2.
 */

public class WelcomePanel {
    /** Default padding for UI elements */
    private static final double PADDING = 20;
    /** Reference to the primary stage */
    private Stage primaryStage;
    /** Reference to the parent game board UI */
    private GameBoardUI gameBoard;
    /** Label for displaying game statistics */
    private Label statsLabel;

    /**
     * Creates a new welcome panel
     * @param primaryStage The primary stage
     * @param gameBoard The parent game board UI
     */
    public WelcomePanel(Stage primaryStage, GameBoardUI gameBoard) {
        this.primaryStage = primaryStage;
        this.gameBoard = gameBoard;
    }

    /**
     * Gets the text to display in the statistics label
     * @return A formatted string containing games played and best time statistics
     */
    private String getStatsText() {
        // Replace with actual stats retrieval logic
        int gamesPlayed = StatsManager.getGamesPlayed();
        int bestTime = StatsManager.getBestTime();
        return String.format("Games Played: %d\nBest Time: %02d:%02d", gamesPlayed, bestTime / 60, bestTime % 60);
    }

    /**
     * Shows the welcome screen with game title, play button, profile management,
     * and statistics.
     * Sets up the UI with styled buttons and attaches event handlers.
     */
    public void show() {
        Label titleLabel = new Label("Memory Card Game");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #4682b4;");

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #32cd32; -fx-text-fill: white; -fx-background-radius: 10;");
        playButton.setPrefSize(220, 60);
        ButtonEffectManager.addButtonHoverEffect(playButton);

        Button statisticsButton = new Button("Statistics");
        statisticsButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        statisticsButton.setPrefSize(220, 60);
        ButtonEffectManager.addButtonHoverEffect(statisticsButton);

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #b22222; -fx-text-fill: white; -fx-background-radius: 10;");
        exitButton.setPrefSize(220, 60);
        ButtonEffectManager.addButtonHoverEffect(exitButton);


        statisticsButton.setOnAction(e -> {
            new ProfileStatsDialog().showAndWait();

        });


        playButton.setOnAction(e -> {
            ProfileSelectionDialog dialog = new ProfileSelectionDialog(primaryStage, () -> {
                if (GlobalProfileContext.getActiveProfile() != null) {
                    gameBoard.showModeSelection(primaryStage);
                }
            });
            dialog.showAndWait();
        });



        exitButton.setOnAction(e -> Platform.exit());

        VBox vbox = new VBox(30, titleLabel, playButton, statisticsButton, exitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(PADDING));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa);");
        vbox.setFillWidth(false); // Don't stretch children

        vbox.setPrefWidth(400);
        vbox.setPrefHeight(350);

        Scene scene = new Scene(vbox, 400, 350);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Card Game");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}


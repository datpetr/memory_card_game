package main.cardgame.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.cardgame.stats.StatsManager;

/**
 * Welcome screen for the Memory Card Game
 * Preserves the original UI components and behavior from GameBoardVisualizer2
 */
public class WelcomePanel {
    private static final double PADDING = 20;
    private Stage primaryStage;
    private GameBoardUI gameBoard;
    private Label statsLabel;

    public WelcomePanel(Stage primaryStage, GameBoardUI gameBoard) {
        this.primaryStage = primaryStage;
        this.gameBoard = gameBoard;
    }

    private String getStatsText() {
        // Replace with actual stats retrieval logic
        int gamesPlayed = StatsManager.getGamesPlayed();
        int bestTime = StatsManager.getBestTime();
        return String.format("Games Played: %d\nBest Time: %02d:%02d", gamesPlayed, bestTime / 60, bestTime % 60);
    }

    /**
     * Shows the welcome screen
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
            StatisticsDialog statsDialog = new StatisticsDialog();
            statsDialog.show();
            // Center the dialog after it is shown
            Platform.runLater(() -> {
                Stage dialogStage = (Stage) statsDialog.getDialogPane().getScene().getWindow();
                dialogStage.setX(primaryStage.getX() + (primaryStage.getWidth() - dialogStage.getWidth()) / 2);
                dialogStage.setY(primaryStage.getY() + (primaryStage.getHeight() - dialogStage.getHeight()) / 2);
            });
        });

        playButton.setOnAction(e -> {
            if (gameBoard.promptForPlayerName(primaryStage)) {
                gameBoard.showModeSelection(primaryStage);
            }
            // If canceled, do nothing (stay on welcome panel)
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

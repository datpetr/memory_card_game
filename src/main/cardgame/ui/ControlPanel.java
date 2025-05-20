package main.cardgame.ui;

import main.cardgame.stats.GameStatistics;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.cardgame.game.Game;

/**
 * Manages the game control buttons (pause, restart, main menu)
 * Preserves the original UI components and logic from GameBoardVisualizer2
 */
public class ControlPanel {
    private Button pauseButton;
    private Button restartButton;
    private Button mainMenuButton;
    private HBox buttonsBox;
    private GameBoardUI gameBoard;
    private Stage primaryStage;
    private Game game;
    private Button statsButton;
    // Direct references to required components
    private CardRenderer cardRenderer;
    private GameStatusPanel statusPanel;

    public ControlPanel(Stage primaryStage, Game game) {
        this.primaryStage = primaryStage;
        this.game = game;
        initialize();
    }

    // Separate setters for each dependency to avoid circular references
    public void setGameBoard(GameBoardUI gameBoard) {
        this.gameBoard = gameBoard;
    }
    
    public void setCardRenderer(CardRenderer cardRenderer) {
        this.cardRenderer = cardRenderer;
    }
    
    public void setStatusPanel(GameStatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
     * Initializes the control panel with buttons
     */
    private void initialize() {
        // Create original game control buttons
        pauseButton = new Button("Pause");
        pauseButton.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;"
        );
        pauseButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(pauseButton);
        
        // Improved pause button logic with synchronized handling
        pauseButton.setOnAction(e -> {
            // Don't use Platform.runLater here as it might cause the second-click issue
            togglePauseGame();
        });

        restartButton = new Button("Restart");
        restartButton.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;"
        );
        restartButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(restartButton);
        
        // Direct action handling without Platform.runLater
        restartButton.setOnAction(e -> {
            if (gameBoard != null && gameBoard.showConfirmationDialog("Are you sure you want to restart the game?")) {
                gameBoard.restartGame(primaryStage);
            }
        });

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;"
        );
        mainMenuButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(mainMenuButton);
        
        // Direct action handling without Platform.runLater
        mainMenuButton.setOnAction(e -> {
            if (gameBoard != null && gameBoard.showConfirmationDialog("Are you sure you want to return to the main menu?")) {
                gameBoard.returnToMainMenu(primaryStage);
            }
        });

        // Original layout for buttons
        //buttonsBox = new HBox(10, pauseButton, restartButton, mainMenuButton);
        buttonsBox = new HBox(10, pauseButton, restartButton, mainMenuButton);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsBox.setPadding(new Insets(0));
    }

    /**
     * Gets the container with all control components
     * @return The HBox containing all control components
     */
    public HBox getContainer() {
        return buttonsBox;
    }

    /**
     * Gets the main menu button
     * @return The main menu button
     */
    public Button getMainMenuButton() {
        return mainMenuButton;
    }

    /**
     * Toggles the pause state of the game
     * Fixed implementation to check requirements before action
     */
    private void togglePauseGame() {
        // Before proceeding, verify we have all required components
        if (gameBoard == null || game == null) {
            System.err.println("Cannot toggle pause: GameBoardUI or Game is null");
            return;
        }
        
        // First handle the game state change - this should always work
        if (game.isActive() && !game.isPaused()) {
            game.pauseGame();
            pauseButton.setText("Resume");
        } else if (game.isActive() && game.isPaused()) {
            game.resumeGame();
            pauseButton.setText("Pause");
        }
        
        // Then handle the UI updates - this might fail if references are missing
        try {
            if (statusPanel != null) {
                if (game.isPaused()) {
                    statusPanel.stopTimerUpdates();
                } else {
                    statusPanel.startTimerUpdates();
                }
            }
            
            if (cardRenderer != null) {
                cardRenderer.setCardButtonsDisabled(game.isPaused());
                cardRenderer.showPauseOverlay(game.isPaused());
            }
        } catch (Exception ex) {
            System.err.println("Error updating UI for pause/resume: " + ex.getMessage());
        }
    }
    private void showStatsWindow() {
        if (game == null) return;

        var stats = game.getStatistics();
        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(
                new javafx.scene.control.Label("Games Played: " + stats.getTotalGames()),
                new javafx.scene.control.Label("Total Matches: " + stats.getTotalMatches()),
                new javafx.scene.control.Label("Total Moves: " + stats.getTotalMoves()),
                new javafx.scene.control.Label("Best Score: " + stats.getBestScore()),
                new javafx.scene.control.Label("Best Time: " + stats.getBestTime() / 1000 + " sec"),
                new javafx.scene.control.Label(String.format("Average Moves: %.2f", stats.getAverageMoves())),
                new javafx.scene.control.Label(String.format("Average Time: %.2f sec", stats.getAverageTime() / 1000))
        );

        javafx.stage.Stage statsStage = new javafx.stage.Stage();
        statsStage.setTitle("Game Statistics");
        statsStage.setScene(new javafx.scene.Scene(content));
        statsStage.show();
    }

}

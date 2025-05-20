package main.cardgame.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.cardgame.game.Game;
import main.cardgame.model.GameBoard;
import java.util.concurrent.CompletableFuture; // Add this import

import main.cardgame.profile.GlobalProfileContext;
import main.cardgame.profile.ProfileManager;
import java.io.IOException;

/**
 * Game over dialog
 * Preserves the original dialog implementation from GameBoardVisualizer2
 */
public class GameOverDialog {
    private Game game;
    private GameBoard board;
    private GameBoardUI gameBoardUI;

    public GameOverDialog(Game game, GameBoard board, GameBoardUI gameBoardUI) {
        this.game = game;
        this.board = board;
        this.gameBoardUI = gameBoardUI;
    }

    /**
     * Shows the game over dialog
     * Original implementation from GameBoardVisualizer2
     * @return A CompletableFuture that completes when the dialog is closed
     */
    public CompletableFuture<Void> show() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        Platform.runLater(() -> {
            boolean isWin = board.allCardsMatched();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(isWin ? "Game Won!" : "Game Over");
            alert.setHeaderText(null);

            String timeDisplay;
            if (game.getTimer().isCountdown()) {
                long remainingTime = game.getTimer().getRemainingTime() / 1000;
                timeDisplay = "Time Left: " + remainingTime + " seconds";
            } else {
                long elapsedTime = game.getTimer().getElapsedTime() / 1000;
                timeDisplay = "Time: " + elapsedTime + " seconds";
            }

            alert.setContentText(
                    (isWin ? "All matches found!" : "Time's up!") + "\n" +
                            "Score: " + game.getPlayer().getScore() + "\n" +
                            timeDisplay + "\n" +
                            "Moves: " + game.getPlayer().getMoves()
            );

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa); " +
                            "-fx-border-color: " + (isWin ? "gold" : "red") + "; " +
                            "-fx-border-width: 3; " +
                            "-fx-border-radius: 10; " +
                            "-fx-background-radius: 10;"
            );

            Label headerLabel = new Label(isWin ? "Congratulations!" : "Game Over");
            headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " +
                    (isWin ? "#4682b4" : "#b22222") + ";");
            dialogPane.setHeader(headerLabel);

            alert.showAndWait();

            if (isWin) {
                // Use the actual number of matches (matchedPairsCount) instead of player's score
                int matches = board.getMatchedPairsCount();
                int moves = game.getPlayer().getMoves();
                long timeInSeconds = (game.getTimer().getElapsedTime() / 1000);

                var profile = GlobalProfileContext.getActiveProfile();
                if (profile != null) {
                    profile.getStatistics().updateGameStats(matches, moves, timeInSeconds);
                    try {
                        ProfileManager.saveProfile(profile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            gameBoardUI.returnToMainMenu(gameBoardUI.getPrimaryStage());
            future.complete(null);
        });
        
        return future;
    }
}

package main.cardgame.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import main.cardgame.game.Game;
import main.cardgame.model.GameBoard;

import java.util.concurrent.CompletableFuture;

/**
 * Game over dialog
 * Preserves the original dialog implementation from GameBoardVisualizer2
 */
public class GameOverDialog {
    private Game game;
    private GameBoard board;

    public GameOverDialog(Game game, GameBoard board) {
        this.game = game;
        this.board = board;
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
            future.complete(null);
        });
        
        return future;
    }
}

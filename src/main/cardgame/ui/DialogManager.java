package main.cardgame.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import main.cardgame.game.Game;

/**
 * Utility class for managing dialogs
 * Preserves the original dialog implementations from GameBoardVisualizer2
 */
public class DialogManager {
    /**
     * Shows a confirmation dialog
     * Original implementation from GameBoardVisualizer2
     * @param message The message to display
     * @param game The current game
     * @return True if the user confirmed, false otherwise
     */

    public static boolean showConfirmationDialog(String message, Game game) {
        // Pause game and timer before showing dialog (like Pause)
        if (game.isActive() && !game.isPaused()) {
            game.pauseGame();
            Object observer = game.getBoard() != null ? game.getBoard().getObserver() : null;
            if (observer instanceof GameBoardUI) {
                GameBoardUI ui = (GameBoardUI) observer;
                if (ui.statusPanel != null) {
                    ui.statusPanel.stopTimerUpdates();
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.getDialogPane().lookupButton(ButtonType.NO).requestFocus();
        alert.getDialogPane().setStyle(
                "-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa); " +
                        "-fx-border-color: #4682b4; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;"
        );
        boolean result = alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;

        // Resume game and timer if cancelled (like Resume)
        if (!result && game.isPaused()) {
            game.resumeGame();
            Object observer = game.getBoard() != null ? game.getBoard().getObserver() : null;
            if (observer instanceof GameBoardUI) {
                GameBoardUI ui = (GameBoardUI) observer;
                if (ui.statusPanel != null) {
                    ui.statusPanel.startTimerUpdates();
                }
            }
        }
        return result;
    }
}

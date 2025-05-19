package main.cardgame.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import main.cardgame.game.Game;
import main.cardgame.model.Player;

/**
 * Manages the game status display (score, moves, time)
 * Preserves the original UI components and logic from GameBoardVisualizer2
 */
public class GameStatusPanel {
    private Label scoreLabel;
    private Label moveLabel;
    private Label timeLabel;
    private Timeline timerUpdateTimeline;
    private Game game;
    private HBox statsBox;

    public GameStatusPanel(Game game) {
        this.game = game;
        initialize();
    }

    /**
     * Initializes the status panel with score, moves, and time labels
     */
    private void initialize() {
        // Create original labels for player stats
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        moveLabel = new Label("Moves: 0");
        moveLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        boolean isTimedMode = game.getTimer().isCountdown();
        timeLabel = new Label(isTimedMode ? "Time Left: 00:00" : "Time: 00:00");
        timeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Original layout for stats
        statsBox = new HBox(20, scoreLabel, moveLabel, timeLabel);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(0));
    }

    /**
     * Gets the container with all status components
     * @return The HBox containing all status components
     */
    public HBox getContainer() {
        return statsBox;
    }

    /**
     * Updates player statistics display
     */
    public void updatePlayerStats(Player player) {
        scoreLabel.setText("Score: " + player.getScore());
        moveLabel.setText("Moves: " + player.getMoves());
    }

    /**
     * Starts the timer updates
     */
    public void startTimerUpdates() {
        stopTimerUpdates(); // Clear any existing timer

        // Original timer update logic
        timerUpdateTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateTimerDisplay())
        );
        timerUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        timerUpdateTimeline.play();
    }

    /**
     * Stops the timer updates
     */
    public void stopTimerUpdates() {
        if (timerUpdateTimeline != null) {
            timerUpdateTimeline.stop();
            timerUpdateTimeline = null;
        }
    }

    /**
     * Updates the timer display
     */
    public void updateTimerDisplay() {
        Platform.runLater(() -> {
            if (game.getTimer().isCountdown()) {
                long seconds = game.getTimer().getRemainingTime() / 1000;
                timeLabel.setText(String.format("Time Left: %02d:%02d", seconds / 60, seconds % 60));

                // Check for time-up in timed games (original logic)
                if (game.getTimer().isTimeUp() && !game.isGameOver()) {
                    game.endGame();
                    stopTimerUpdates();
                    ((GameBoardUI)game.getBoard().getObserver()).showGameOverMessage();
                }
            } else {
                long seconds = game.getTimer().getElapsedTime() / 1000;
                timeLabel.setText(String.format("Time: %02d:%02d", seconds / 60, seconds % 60));
            }
        });
    }
}

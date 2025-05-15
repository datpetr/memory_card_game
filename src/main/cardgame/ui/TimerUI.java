package main.cardgame.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;
import main.cardgame.game.Game;

import java.util.Observable;
import java.util.Observer;

public class TimerUI implements Observer {
    private final Game game;
    private Label timerLabel;
    private Timeline timerUpdateTimeline;

    public TimerUI(Game game) {
        this.game = game;
        createComponent();

        // Register as observer for timer events if needed
        game.addObserver(this);
    }

    public Label getComponent() {
        return timerLabel;
    }

    private void createComponent() {
        String labelText = game.getTimer().isCountdown() ? "Time Left: 00:00" : "Time: 00:00";
        timerLabel = new Label(labelText);
        timerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    }

    public void startUpdates() {
        stopUpdates(); // Clear any existing timer

        timerUpdateTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> updateDisplay())
        );
        timerUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        timerUpdateTimeline.play();
    }

    public void stopUpdates() {
        if (timerUpdateTimeline != null) {
            timerUpdateTimeline.stop();
            timerUpdateTimeline = null;
        }
    }

    private void updateDisplay() {
        Platform.runLater(() -> {
            if (game.getTimer().isCountdown()) {
                long seconds = game.getTimer().getRemainingTime() / 1000;
                timerLabel.setText("Time Left: " + formatTime(seconds));

                // Check for time up
                if (game.getTimer().isTimeUp() && game.isActive()) {
                    game.endGame();
                    game.notifyObservers("TIME_UP");
                }
            } else {
                long seconds = game.getTimer().getElapsedTime() / 1000;
                timerLabel.setText("Time: " + formatTime(seconds));
            }
        });
    }

    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }
}
package main.cardgame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import main.cardgame.game.Game;

public class ScoreAndTimerPanel {
    private final ScoreUI scoreUI;
    private final TimerUI timerUI;
    private final HBox component;

    public ScoreAndTimerPanel(Game game) {
        // Create individual UI components
        scoreUI = new ScoreUI(game);
        timerUI = new TimerUI(game);

        // Create panel layout
        component = new HBox(20);
        component.setPadding(new Insets(15, 10, 15, 10));
        component.setAlignment(Pos.CENTER);
        component.setStyle("-fx-background-color: rgba(240, 248, 255, 0.8);");

        // Add components with appropriate spacing
        component.getChildren().add(scoreUI.getComponent());

        // Flexible spacer between score and timer
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        component.getChildren().add(spacer);

        component.getChildren().add(timerUI.getComponent());
    }

    public HBox getComponent() {
        return component;
    }

    public void startTimerUpdates() {
        timerUI.startUpdates();
    }

    public void stopTimerUpdates() {
        timerUI.stopUpdates();
    }

    public void updateScoreDisplay() {
        scoreUI.updateDisplay();
    }
}
package main.cardgame.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import main.cardgame.game.Game;

public class ScoreUI {
    private final Game game;
    private Label scoreLabel;
    private Label moveLabel;
    private HBox component;

    public ScoreUI(Game game) {
        this.game = game;
        createComponent();
    }

    public HBox getComponent() {
        return component;
    }

    private void createComponent() {
        // Create labels
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        moveLabel = new Label("Moves: 0");
        moveLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Create container
        component = new HBox(20, scoreLabel, moveLabel);
        component.setPadding(new Insets(10, 0, 10, 0));
        component.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(component, Priority.ALWAYS);
    }

    public void updateDisplay() {
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + game.getPlayer().getScore());
            moveLabel.setText("Moves: " + game.getPlayer().getMoves());
        });
    }
}

package main.cardgame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.cardgame.game.Game;

public class ControlUI {
    private final Game game;
    private final Stage stage;
    private final GameBoardVisualizer controller;
    private Button pauseButton;
    private Button restartButton;
    private Button exitButton;
    private HBox component;

    public ControlUI(Game game, Stage stage, GameBoardVisualizer controller) {
        this.game = game;
        this.stage = stage;
        this.controller = controller;
        createComponent();
    }

    public HBox getComponent() {
        return component;
    }

    private void createComponent() {
        double buttonWidth = stage.getWidth() * 0.1;
        double buttonHeight = stage.getHeight() * 0.05;

        pauseButton = new Button("Pause");
        pauseButton.setPrefSize(buttonWidth, buttonHeight);
        pauseButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4682b4; -fx-text-fill: white;");
        pauseButton.setOnAction(e -> togglePauseGame());

        restartButton = new Button("Restart");
        restartButton.setPrefSize(buttonWidth, buttonHeight);
        restartButton.setStyle("-fx-font-size: 14px; -fx-background-color: #32cd32; -fx-text-fill: white;");
        restartButton.setOnAction(e -> controller.restartGame());

        exitButton = new Button("Exit");
        exitButton.setPrefSize(buttonWidth, buttonHeight);
        exitButton.setStyle("-fx-font-size: 14px; -fx-background-color: #b22222; -fx-text-fill: white;");
        exitButton.setOnAction(e -> stage.close());

        component = new HBox(10, pauseButton, restartButton, exitButton);
        component.setPadding(new Insets(10, 0, 0, 0));
        component.setAlignment(Pos.CENTER);
    }

    private void togglePauseGame() {
        if (game.isActive() && !game.isPaused()) {
            game.pauseGame();
            pauseButton.setText("Resume");
        } else {
            game.resumeGame();
            pauseButton.setText("Pause");
        }
    }

    public void resetControls() {
        pauseButton.setText("Pause");
    }
}
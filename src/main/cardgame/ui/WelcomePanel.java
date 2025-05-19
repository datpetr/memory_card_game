package main.cardgame.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Welcome screen for the Memory Card Game
 * Preserves the original UI components and behavior from GameBoardVisualizer2
 */
public class WelcomePanel {
    private static final double PADDING = 20;
    private Stage primaryStage;
    private GameBoardUI gameBoard;

    public WelcomePanel(Stage primaryStage, GameBoardUI gameBoard) {
        this.primaryStage = primaryStage;
        this.gameBoard = gameBoard;
    }

    /**
     * Shows the welcome screen
     */
    public void show() {
        // Original welcome screen UI components from GameBoardVisualizer2
        Label titleLabel = new Label("Welcome to Memo!");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #4682b4;");

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #32cd32; -fx-text-fill: white; -fx-background-radius: 10;");
        playButton.setPrefSize(180, 60);
        ButtonEffectManager.addButtonHoverEffect(playButton);

        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #b22222; -fx-text-fill: white; -fx-background-radius: 10;");
        exitButton.setPrefSize(180, 60);
        ButtonEffectManager.addButtonHoverEffect(exitButton);

        // Original event handlers
        playButton.setOnAction(e -> {
            if (gameBoard.promptForPlayerName(primaryStage)) {
                gameBoard.showModeSelection(primaryStage);
            }
        });
        exitButton.setOnAction(e -> Platform.exit());

        // Original layout
        VBox vbox = new VBox(30, titleLabel, playButton, exitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(PADDING));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa);");

        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memo");
        primaryStage.show();
    }
}

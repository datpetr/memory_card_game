package main.cardgame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Mode selection screen for the Memory Card Game
 * Preserves the original UI components and behavior from GameBoardVisualizer2
 */
public class ModeSelectionPanel {
    private static final double PADDING = 20;
    private Stage primaryStage;
    private GameBoardUI gameBoard;

    public ModeSelectionPanel(Stage primaryStage, GameBoardUI gameBoard) {
        this.primaryStage = primaryStage;
        this.gameBoard = gameBoard;
    }

    /**
     * Shows the mode selection screen
     */
    public void show() {
        // Original mode selection UI components from GameBoardVisualizer2
        Label modeLabel = new Label("Select Game Mode");
        modeLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #4682b4;");

        Button endlessButton = new Button("Endless Mode");
        endlessButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        endlessButton.setPrefSize(180, 60);
        ButtonEffectManager.addButtonHoverEffect(endlessButton);

        Button timedButton = new Button("Timed Mode");
        timedButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        timedButton.setPrefSize(180, 60);
        ButtonEffectManager.addButtonHoverEffect(timedButton);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #b22222; -fx-text-fill: white; -fx-background-radius: 10;");
        backButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(backButton);

        // Original event handlers
        endlessButton.setOnAction(e -> gameBoard.showDifficultySelection(primaryStage, "Endless Mode"));
        timedButton.setOnAction(e -> gameBoard.showDifficultySelection(primaryStage, "Timed Mode"));
        backButton.setOnAction(e -> gameBoard.returnToMainMenu(primaryStage));

        // Original layout
        VBox vbox = new VBox(25, modeLabel, endlessButton, timedButton, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(PADDING));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa);");

        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Game Mode");
        primaryStage.show();
    }
}

package main.cardgame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Difficulty selection screen for the Memory Card Game
 * Preserves the original UI components and behavior from GameBoardVisualizer2
 */
public class DifficultySelectionPanel {
    private static final double PADDING = 20;
    private Stage primaryStage;
    private GameBoardUI gameBoard;
    private String mode;

    public DifficultySelectionPanel(Stage primaryStage, GameBoardUI gameBoard, String mode) {
        this.primaryStage = primaryStage;
        this.gameBoard = gameBoard;
        this.mode = mode;
    }

    /**
     * Shows the difficulty selection screen
     */
    public void show() {
        // Original difficulty selection UI components from GameBoardVisualizer2
        Label difficultyLabel = new Label("Select Difficulty");
        difficultyLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #4682b4;");

        Button easyButton = new Button("Easy");
        easyButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        easyButton.setPrefSize(180, 60);
        ButtonEffectManager.addButtonHoverEffect(easyButton);

        Button mediumButton = new Button("Medium");
        mediumButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        mediumButton.setPrefSize(180, 60);
        ButtonEffectManager.addButtonHoverEffect(mediumButton);

        Button hardButton = new Button("Hard");
        hardButton.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        hardButton.setPrefSize(180, 60);
        ButtonEffectManager.addButtonHoverEffect(hardButton);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #b22222; -fx-text-fill: white; -fx-background-radius: 10;");
        backButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(backButton);

        // Original event handlers
        easyButton.setOnAction(e -> gameBoard.startGameWithSettings(primaryStage, mode, "easy"));
        mediumButton.setOnAction(e -> gameBoard.startGameWithSettings(primaryStage, mode, "medium"));
        hardButton.setOnAction(e -> gameBoard.startGameWithSettings(primaryStage, mode, "hard"));
        backButton.setOnAction(e -> gameBoard.showModeSelection(primaryStage));

        // Original layout
        VBox vbox = new VBox(25, difficultyLabel, easyButton, mediumButton, hardButton, backButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(PADDING));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa);");

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Difficulty");
        primaryStage.show();
    }
}

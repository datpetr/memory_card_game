package main.cardgame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Platform;
import main.cardgame.game.Game;

public class GameUI {
    private final Stage stage;
    private final GameBoardVisualizer controller;
    private String selectedDifficulty;

    // Screen dimensions as percentages
    private static final double SCREEN_WIDTH_PERCENT = 80.0;
    private static final double SCREEN_HEIGHT_PERCENT = 80.0;

    public GameUI(Stage stage, GameBoardVisualizer controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void showWelcomeScreen() {
        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth() * (SCREEN_WIDTH_PERCENT / 100);
        double screenHeight = screen.getBounds().getHeight() * (SCREEN_HEIGHT_PERCENT / 100);

        // Create UI components
        Label titleLabel = new Label("Welcome to the Memory Card Game!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Game mode selection
        Label modeLabel = new Label("Select Game Mode:");
        modeLabel.setStyle("-fx-font-size: 16px;");

        ComboBox<String> modeComboBox = new ComboBox<>();
        modeComboBox.getItems().addAll("Endless Mode", "Timed Mode");
        modeComboBox.setValue("Endless Mode");
        modeComboBox.setMaxWidth(Double.MAX_VALUE);

        // Difficulty selection
        Label difficultyLabel = new Label("Select Difficulty:");
        difficultyLabel.setStyle("-fx-font-size: 16px;");

        // Button size based on screen size
        double buttonWidth = screenWidth * 0.15;
        double buttonHeight = screenHeight * 0.06;

        Button easyButton = createDifficultyButton("Easy", buttonWidth, buttonHeight);
        Button mediumButton = createDifficultyButton("Medium", buttonWidth, buttonHeight);
        Button hardButton = createDifficultyButton("Hard", buttonWidth, buttonHeight);

        // Start button
        Button startButton = new Button("Start");
        startButton.setDisable(true);
        startButton.setPrefSize(buttonWidth * 1.2, buttonHeight * 1.2);
        startButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #32cd32; -fx-text-fill: white; -fx-background-radius: 10;");

        // Button actions
        easyButton.setOnAction(e -> {
            setDifficulty("easy");
            highlightSelectedButton(easyButton, mediumButton, hardButton);
            startButton.setDisable(false);
        });

        mediumButton.setOnAction(e -> {
            setDifficulty("medium");
            highlightSelectedButton(mediumButton, easyButton, hardButton);
            startButton.setDisable(false);
        });

        hardButton.setOnAction(e -> {
            setDifficulty("hard");
            highlightSelectedButton(hardButton, easyButton, mediumButton);
            startButton.setDisable(false);
        });

        startButton.setOnAction(e -> {
            controller.startGameWithSettings(stage, modeComboBox.getValue(), selectedDifficulty);
        });

        // Layout
        HBox difficultyButtons = new HBox(screenWidth * 0.015, easyButton, mediumButton, hardButton);
        difficultyButtons.setAlignment(Pos.CENTER);

        VBox welcomeLayout = new VBox(screenHeight * 0.02,
                titleLabel,
                modeLabel, modeComboBox,
                difficultyLabel, difficultyButtons,
                startButton);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setPadding(new Insets(screenHeight * 0.03));
        welcomeLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa);");

        // Size combobox appropriately
        modeComboBox.setMaxWidth(buttonWidth * 3.1);

        // Create scene
        Scene welcomeScene = new Scene(welcomeLayout, screenWidth * 0.5, screenHeight * 0.5);
        stage.setScene(welcomeScene);
        stage.setTitle("Memory Card Game");
        stage.show();
    }

    public Scene createGameScene(BorderPane mainLayout, String gameMode) {
        Screen screen = Screen.getPrimary();
        double sceneWidth = screen.getBounds().getWidth() * (SCREEN_WIDTH_PERCENT / 100);
        double sceneHeight = screen.getBounds().getHeight() * (SCREEN_HEIGHT_PERCENT / 100);

        mainLayout.setPadding(new Insets(sceneHeight * 0.02));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #e6e6fa);");

        return new Scene(mainLayout, sceneWidth, sceneHeight);
    }

    public void showGameOverMessage(boolean isWin, Game game) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(isWin ? "Game Won!" : "Game Over");
            alert.setHeaderText(null);

            // Format time display
            String timeDisplay;
            if (game.getTimer().isCountdown()) {
                long remainingSeconds = game.getTimer().getRemainingTime() / 1000;
                timeDisplay = "Time Left: " + formatTime(remainingSeconds);
            } else {
                long elapsedSeconds = game.getTimer().getElapsedTime() / 1000;
                timeDisplay = "Time: " + formatTime(elapsedSeconds);
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
        });
    }

    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private Button createDifficultyButton(String text, double width, double height) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        return button;
    }

    private void highlightSelectedButton(Button selected, Button... others) {
        selected.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #2e5984; -fx-text-fill: white; -fx-background-radius: 10;");
        for (Button btn : others) {
            btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
        }
    }

    private void setDifficulty(String difficulty) {
        this.selectedDifficulty = difficulty;
    }
}
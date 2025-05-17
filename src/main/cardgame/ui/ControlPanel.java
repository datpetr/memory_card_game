package main.cardgame.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.cardgame.game.Game;

/**
 * Manages the game control buttons (pause, restart, main menu)
 * Preserves the original UI components and logic from GameBoardVisualizer2
 */
public class ControlPanel {
    private Button pauseButton;
    private Button restartButton;
    private Button mainMenuButton;
    private HBox buttonsBox;
    private GameBoardUI gameBoard;
    private Stage primaryStage;
    private Game game;

    public ControlPanel(GameBoardUI gameBoard, Stage primaryStage, Game game) {
        this.gameBoard = gameBoard;
        this.primaryStage = primaryStage;
        this.game = game;
        initialize();
    }

    /**
     * Initializes the control panel with buttons
     */
    private void initialize() {
        // Create original game control buttons
        pauseButton = new Button("Pause");
        pauseButton.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;"
        );
        pauseButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(pauseButton);
        
        // Original pause button logic
        pauseButton.setOnAction(e -> togglePauseGame());

        restartButton = new Button("Restart");
        restartButton.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;"
        );
        restartButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(restartButton);
        
        // Original restart button logic
        restartButton.setOnAction(e -> {
            if (DialogManager.showConfirmationDialog("Are you sure you want to restart the game?", game)) {
                gameBoard.restartGame(primaryStage);
            }
        });

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;"
        );
        mainMenuButton.setPrefSize(120, 40);
        ButtonEffectManager.addButtonHoverEffect(mainMenuButton);
        
        // Original main menu button logic
        mainMenuButton.setOnAction(e -> {
            if (DialogManager.showConfirmationDialog("Are you sure you want to return to the main menu?", game)) {
                gameBoard.returnToMainMenu(primaryStage);
            }
        });

        // Original layout for buttons
        buttonsBox = new HBox(10, pauseButton, restartButton, mainMenuButton);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsBox.setPadding(new Insets(0));
    }

    /**
     * Gets the container with all control components
     * @return The HBox containing all control components
     */
    public HBox getContainer() {
        return buttonsBox;
    }

    /**
     * Gets the main menu button
     * @return The main menu button
     */
    public Button getMainMenuButton() {
        return mainMenuButton;
    }

    /**
     * Toggles the pause state of the game
     * Original implementation from GameBoardVisualizer2
     */
    private void togglePauseGame() {
        CardRenderer cardRenderer = ((GameBoardUI)game.getBoard().getObserver()).cardRenderer;
        GameStatusPanel statusPanel = ((GameBoardUI)game.getBoard().getObserver()).statusPanel;
        
        if (game.isActive() && !game.isPaused()) {
            game.pauseGame();
            pauseButton.setText("Resume");
            statusPanel.stopTimerUpdates();
            cardRenderer.setCardButtonsDisabled(true);
            cardRenderer.showPauseOverlay(true);
        } else if (game.isActive() && game.isPaused()) {
            game.resumeGame();
            pauseButton.setText("Pause");
            statusPanel.startTimerUpdates();
            cardRenderer.setCardButtonsDisabled(false);
            cardRenderer.showPauseOverlay(false);
        }
    }
}

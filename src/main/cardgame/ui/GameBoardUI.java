package main.cardgame.ui;

import main.cardgame.stats.StatsManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.cardgame.game.EndlessGame;
import main.cardgame.game.Game;
import main.cardgame.game.TimedGame;
import main.cardgame.model.Card;
import main.cardgame.model.Deck;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;

import java.util.Optional;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

/**
 * Main entry point for the Memory Card Game.
 * Refactored from GameBoardVisualizer2 to improve code organization
 * while maintaining identical UI components and behavior.
 */
public class GameBoardUI extends Application implements Observer {
    /** Default number of rows in the game board */
    private static int BOARD_ROWS = 3;
    /** Default number of columns in the game board */
    private static int BOARD_COLS = 4;
    /** Aspect ratio for cards (width/height) */
    private static final double CARD_ASPECT_RATIO = 2.0 / 2.0;
    /** Padding around the game board */
    private static final double PADDING = 20;
    /** Gap between cards */
    private static final double GAP = 10;

    /** Current game instance */
    private Game game;
    /** Game board model */
    private GameBoard board;
    /** Current game mode (e.g., "endless", "timed") */
    private String currentMode;
    /** Current difficulty level (e.g., "easy", "medium", "hard") */
    private String currentDifficulty;

    // Organized UI components
    /** Welcome screen panel */
    private WelcomePanel welcomePanel;
    /** Game mode selection panel */
    private ModeSelectionPanel modeSelectionPanel;
    /** Difficulty level selection panel */
    private DifficultySelectionPanel difficultySelectionPanel;
    /** Game status panel */
    public GameStatusPanel statusPanel;
    /** Card renderer for displaying cards */
    public CardRenderer cardRenderer;
    /** Control panel for game controls */
    private ControlPanel controlPanel;

    /** Player's name */
    private String playerName;
    /** Flag to indicate if game over message has been shown */
    private boolean gameOverShown = false;

    /** Primary stage of the application */
    private Stage primaryStage; // Add this field to store the primary stage

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Store the primary stage
        // Setup welcome screen (identical to original implementation)
        welcomePanel = new WelcomePanel(primaryStage, this);
        welcomePanel.show();
    }

    // Add a getter for the primary stage
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Shows the mode selection screen
     * @param primaryStage The primary stage
     */

    /**
     * Shows the difficulty selection screen
     * @param primaryStage The primary stage
     * @param mode The selected game mode
     */
    public void showDifficultySelection(Stage primaryStage, String mode) {
        difficultySelectionPanel = new DifficultySelectionPanel(primaryStage, this, mode);
        difficultySelectionPanel.show();
    }

    public void showModeSelection(Stage primaryStage) {
        modeSelectionPanel = new ModeSelectionPanel(primaryStage, this);
        modeSelectionPanel.show();
    }

    public void startGameWithSettings(Stage primaryStage, String mode, String difficulty) {
        this.gameOverShown = false;
        this.currentMode = mode;
        this.currentDifficulty = difficulty;

        // Set card's background based on difficulty (preserved logic)
        switch (difficulty) {
            case "easy":
                Card.setBackImagePath("file:src/main/resources/images/backCards/easyback.png");
                break;
            case "medium":
                Card.setBackImagePath("file:src/main/resources/images/backCards/mediumback.png");
                break;
            case "hard":
                Card.setBackImagePath("file:src/main/resources/images/backCards/hardback.png");
                break;
            default:
                throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }

        int totalCardsNeeded = BOARD_ROWS * BOARD_COLS;
        int requiredPairs = totalCardsNeeded / 2;

        // Create deck and board (preserved logic)
        // Create deck and board
        Deck deck = Deck.createDeckForLevel(difficulty);
        this.board = new GameBoard(difficulty, deck.getCards()) {};
        this.board.addObserver(this);

        // Get the actual dimensions from the created board
        BOARD_ROWS = board.getRows();
        BOARD_COLS = board.getCols();

        Player player = new Player("Player1");

        // Register as observer for player
        player.addObserver(this);

        // Create appropriate game type based on mode (preserved logic)
        if (mode.equals("Timed Mode")) {
            int timeLimit;
            switch (difficulty) {
                case "easy": timeLimit = TimedGame.EASY_TIME; break;
                case "medium": timeLimit = TimedGame.MEDIUM_TIME; break;
                case "hard": timeLimit = TimedGame.HARD_TIME; break;
                default: timeLimit = TimedGame.EASY_TIME;
            }
            this.game = new TimedGame(board, player, timeLimit);
        } else {
            this.game = new EndlessGame(board, player);
        }

        // Register as observer for the timer
        if (game.getTimer() instanceof Observable) {
            ((Observable) game.getTimer()).addObserver(this);
        }

        // Set up the UI
        setupGameUI(primaryStage, mode);
    }

    /**
     * Sets up the game UI
     * @param primaryStage The primary stage
     * @param mode The game mode
     */
    private void setupGameUI(Stage primaryStage, String mode) {
        // Create the main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(PADDING));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #e6e6fa);");

        // Create CardRenderer with rows and cols from the board
        cardRenderer = new CardRenderer(this, game, board, board.getCols(), board.getRows(), CARD_ASPECT_RATIO, GAP);

        // Then create status panel
        statusPanel = new GameStatusPanel(this.game);

        // Then create control panel with references to both
        controlPanel = new ControlPanel(primaryStage, game);

        // Set component references explicitly (not through constructor)
        controlPanel.setGameBoard(this);
        controlPanel.setCardRenderer(cardRenderer);
        controlPanel.setStatusPanel(statusPanel);

        // Combine status panel and control panel (identical to original)
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(0, 0, 10, 0));
        statusBar.getChildren().addAll(statusPanel.getContainer(), controlPanel.getContainer());
        HBox.setHgrow(statusPanel.getContainer(), javafx.scene.layout.Priority.ALWAYS);

        mainLayout.setTop(statusBar);

        // Create the game board and pause overlay (use only one CardRenderer instance)
        StackPane centerPane = new StackPane(cardRenderer.getGridPane(), cardRenderer.getPauseOverlay());
        mainLayout.setCenter(centerPane);

        // Create the scene (preserved from original)
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;
        Scene scene = new Scene(mainLayout, Math.min(maxWidth, 1024), Math.min(maxHeight, 768));

        primaryStage.setTitle("Memory Card Game - " + mode);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> statusPanel.stopTimerUpdates());

        primaryStage.setUserData(this); // Set GameBoardUI as user data for the stage

        // Center the game window on the screen
        primaryStage.centerOnScreen();

        // Start the game
        game.play();
        statusPanel.startTimerUpdates();
    }

    /**
     * Restarts the game with current settings
     */
    public void restartGame(Stage stage) {
        statusPanel.stopTimerUpdates();
        startGameWithSettings(stage, currentMode, currentDifficulty);
    }

    /**
     * Returns to the main menu
     */
    public void returnToMainMenu(Stage primaryStage) {
        if (statusPanel != null) {
            statusPanel.stopTimerUpdates();
        }
        welcomePanel = new WelcomePanel(primaryStage, this);
        welcomePanel.show();
    }

    @Override
    public void update(Observable observable, Object arg) {
        Platform.runLater(() -> {
            // Handle updates from all observable objects
            // We delegate the updates to appropriate components

            // Handle player updates
            if (observable instanceof Player && statusPanel != null) {
                statusPanel.updatePlayerStats((Player)observable);
            }

            // Handle card updates
            else if (observable instanceof Card && arg instanceof String && cardRenderer != null) {
                cardRenderer.handleCardUpdate((Card)observable);
            }

            // Handle timer updates
            else if (observable == game.getTimer() && "TIMER_STOPPED".equals(arg) && statusPanel != null) {
                statusPanel.updateTimerDisplay();
                showGameOverMessage(); // <-- Add this line
            }
        });
    }

    /**
     * Shows the game over message
     */
    public void showGameOverMessage() {
        // After the game ends, before returning to main menu
        int timeInSeconds = game.getTimer().getElapsedSeconds(); // Adjust this to your timer's API
        if (gameOverShown) return; // Prevent double-counting
        gameOverShown = true;
        StatsManager.recordGame(
                game.getMatches(),
                game.getMoves(),
                game.getTimer().getElapsedSeconds()
        );

        GameOverDialog gameOverDialog = new GameOverDialog(game, board, this); // Pass GameBoardUI instance
        gameOverDialog.show().thenRun(() -> {
            // Return to main menu after dialog closes
            returnToMainMenu((Stage) controlPanel.getMainMenuButton().getScene().getWindow());
        });
    }

        public static void main(String[] args) {
            Card.setBackImagePath("file:src/main/resources/images/back2.jpg");
            launch(args);
        }


    /**
     * Shows a confirmation dialog.
     * @param message The message to display.
     * @return True if the user confirmed, false otherwise.
     */
    public boolean showConfirmationDialog(String message) {
        // Pause game and timer before showing dialog
        if (game.isActive() && !game.isPaused()) {
            game.pause();
            if (statusPanel != null) {
                statusPanel.stopTimerUpdates();
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.getDialogPane().lookupButton(ButtonType.NO).requestFocus();
        alert.getDialogPane().setStyle(
                "-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa); " +
                        "-fx-border-color: #4682b4; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;"
        );
        boolean result = alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;

        // Resume game and timer if cancelled
        if (!result && game.isPaused()) {
            game.resume();
            if (statusPanel != null) {
                statusPanel.startTimerUpdates();
            }
        }
        return result;
    }
}


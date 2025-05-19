package main.cardgame.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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

import java.util.Observable;
import java.util.Observer;

/**
 * Main entry point for the Memory Card Game.
 * Refactored from GameBoardVisualizer2 to improve code organization
 * while maintaining identical UI components and behavior.
 */
public class GameBoardUI extends Application implements Observer {
    // Constants preserved from GameBoardVisualizer2
    private static int BOARD_ROWS = 4;
    private static int BOARD_COLS = 4;
    private static final double CARD_ASPECT_RATIO = 2.0 / 3.0;
    private static final double PADDING = 20;
    private static final double GAP = 10;

    // Core game components (preserved from GameBoardVisualizer2)
    private Game game;
    private GameBoard board;
    private String currentMode;
    private String currentDifficulty;

    // Organized UI components
    private WelcomePanel welcomePanel;
    private ModeSelectionPanel modeSelectionPanel;
    private DifficultySelectionPanel difficultySelectionPanel;
    public GameStatusPanel statusPanel;
    public CardRenderer cardRenderer;
    private ControlPanel controlPanel;
    private BoardCanvas boardCanvas;

    @Override
    public void start(Stage primaryStage) {
        // Setup welcome screen (identical to original implementation)
        welcomePanel = new WelcomePanel(primaryStage, this);
        welcomePanel.show();
    }

    /**
     * Shows the mode selection screen
     * @param primaryStage The primary stage
     */
    public void showModeSelection(Stage primaryStage) {
        modeSelectionPanel = new ModeSelectionPanel(primaryStage, this);
        modeSelectionPanel.show();
    }

    /**
     * Shows the difficulty selection screen
     * @param primaryStage The primary stage
     * @param mode The selected game mode
     */
    public void showDifficultySelection(Stage primaryStage, String mode) {
        difficultySelectionPanel = new DifficultySelectionPanel(primaryStage, this, mode);
        difficultySelectionPanel.show();
    }

    /**
     * Starts the game with the selected settings
     * @param primaryStage The primary stage
     * @param mode The game mode
     * @param difficulty The game difficulty
     */
    public void startGameWithSettings(Stage primaryStage, String mode, String difficulty) {
        this.currentMode = mode;
        this.currentDifficulty = difficulty;

        // Set board dimensions based on difficulty (preserved logic)
        switch (difficulty) {
            case "easy":
                BOARD_ROWS = 4;
                BOARD_COLS = 4;
                break;
            case "medium":
                BOARD_ROWS = 6;
                BOARD_COLS = 6;
                break;
            case "hard":
                BOARD_ROWS = 8;
                BOARD_COLS = 8;
                break;
            default:
                throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }

        int totalCardsNeeded = BOARD_ROWS * BOARD_COLS;
        int requiredPairs = totalCardsNeeded / 2;

        // Create deck and board (preserved logic)
        Deck deck = Deck.createDeckForLevel(difficulty);
        this.board = new GameBoard(difficulty, deck.getCards());
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

        // Create components in correct order
        // First create CardRenderer so it can be referenced by others
        cardRenderer = new CardRenderer(this, game, board, BOARD_ROWS, BOARD_COLS, CARD_ASPECT_RATIO, GAP);
        
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

        // Create the game board
        boardCanvas = new BoardCanvas(board, cardRenderer.getGridPane());

        // Setup pause overlay (identical to original)
        StackPane centerPane = new StackPane(boardCanvas.getGridPane(), cardRenderer.getPauseOverlay());
        mainLayout.setCenter(centerPane);

        // Create the scene (preserved from original)
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;
        Scene scene = new Scene(mainLayout, Math.min(maxWidth, 1024), Math.min(maxHeight, 768));

        primaryStage.setTitle("Memory Card Game - " + mode);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> statusPanel.stopTimerUpdates());

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
        GameOverDialog gameOverDialog = new GameOverDialog(game, board);
        gameOverDialog.show().thenRun(() -> {
            // Return to main menu after dialog closes
            returnToMainMenu((Stage)controlPanel.getMainMenuButton().getScene().getWindow());
        });
    }

    public static void main(String[] args) {
        Card.setBackImagePath("file:src/main/resources/images/back2.jpg");
        launch(args);
    }
}

package main.cardgame.ui;

import main.cardgame.stats.StatsManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
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
    // Constants preserved from GameBoardVisualizer2
    private static int BOARD_ROWS = 3;
    private static int BOARD_COLS = 4;
    private static final double CARD_ASPECT_RATIO = 2.0 / 2.0;
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

    private String playerName;
    private boolean gameOverShown = false;

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

    /**
     * Starts the game with the selected settings
     * @param primaryStage The primary stage
     * @param mode The game mode
     * @param difficulty The game difficulty
     */

    // Method to prompt and validate player name
    boolean promptForPlayerName(Stage primaryStage) {
        Pattern validPattern = Pattern.compile("^[A-Za-z0-9_-]{3,12}$");
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Player Name");
            dialog.setHeaderText(null);
            dialog.setContentText("Name:");
            dialog.initOwner(primaryStage);
            // Center the dialog after it is shown
            Platform.runLater(() -> {
                Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
                dialogStage.setX(primaryStage.getX() + (primaryStage.getWidth() - dialogStage.getWidth()) / 2);
                dialogStage.setY(primaryStage.getY() + (primaryStage.getHeight() - dialogStage.getHeight()) / 2);
            });
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String input = result.get().trim();
                try {
                    if (input.contains(" ")) {
                        throw new IllegalArgumentException("Name cannot contain spaces.");
                    }
                    if (input.length() < 3 || input.length() > 12) {
                        throw new IllegalArgumentException("Name must be 3-12 characters.");
                    }
                    if (!validPattern.matcher(input).matches()) {
                        throw new IllegalArgumentException("Name can only contain latin letters, digits, _ and -.");
                    }
                    playerName = input;
                    return true; // Success
                } catch (IllegalArgumentException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Name");
                    alert.setHeaderText("Invalid player name.");
                    alert.setContentText(ex.getMessage());
                    alert.initOwner(primaryStage);
                    // Center the alert after it is shown
                    Platform.runLater(() -> {
                        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                        alertStage.setX(primaryStage.getX() + (primaryStage.getWidth() - alertStage.getWidth()) / 2);
                        alertStage.setY(primaryStage.getY() + (primaryStage.getHeight() - alertStage.getHeight()) / 2);
                    });
                    alert.showAndWait(); // Wait for user to click OK before looping to show dialog again
                    // Loop continues, so prompt dialog will show again
                }
            } else {
                return false; // Cancel pressed
            }
        }
    }

    public void showModeSelection(Stage primaryStage) {
        modeSelectionPanel = new ModeSelectionPanel(primaryStage, this);
        modeSelectionPanel.show();
    }

    public void startGameWithSettings(Stage primaryStage, String mode, String difficulty) {
        this.gameOverShown = false;
        this.currentMode = mode;
        this.currentDifficulty = difficulty;

        // Set board dimensions based on difficulty (preserved logic)
        switch (difficulty) {
            case "easy":
                BOARD_ROWS = 4;
                BOARD_COLS = 4;
                Card.setBackImagePath("file:src/main/resources/images/backCards/easyback.png");
                break;
            case "medium":
                BOARD_ROWS = 6;
                BOARD_COLS = 6;
                Card.setBackImagePath("file:src/main/resources/images/backCards/mediumback.png");
                break;
            case "hard":
                BOARD_ROWS = 8;
                BOARD_COLS = 8;
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
}


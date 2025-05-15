package main.cardgame.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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

public class GameBoardVisualizer extends Application implements Observer {
    // Game state
    private Game game;
    private GameBoard board;
    private int boardRows = 4;
    private int boardCols = 4;

    // UI Components
    private GameUI gameUI;
    private CardUI cardUI;
    private ScoreAndTimerPanel statusPanel;
    private ControlUI controlUI;

    @Override
    public void start(Stage primaryStage) {
        gameUI = new GameUI(primaryStage, this);
        gameUI.showWelcomeScreen();
    }

    public void startGameWithSettings(Stage primaryStage, String mode, String difficulty) {
        // Set board dimensions based on difficulty
        switch (difficulty) {
            case "easy": boardRows = 4; boardCols = 4; break;
            case "medium": boardRows = 6; boardCols = 6; break;
            case "hard": boardRows = 8; boardCols = 8; break;
            default: throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }

        // Set up game components
        int requiredPairs = (boardRows * boardCols) / 2;
        Deck deck = new Deck().createDeck(requiredPairs);
        this.board = new GameBoard(difficulty, deck.getCards());
        Player player = new Player("Player1");
        player.addObserver(this);

        // Create appropriate game based on mode
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
        game.addObserver(this);

        // Set up UI components
        BorderPane mainLayout = new BorderPane();

        // Initialize UI components
        statusPanel = new ScoreAndTimerPanel(game);
        cardUI = new CardUI(game, board, boardRows, boardCols);
        controlUI = new ControlUI(game, primaryStage, this);

        // Arrange UI components
        mainLayout.setTop(statusPanel.getComponent());
        mainLayout.setCenter(cardUI.getComponent());
        mainLayout.setBottom(controlUI.getComponent());

        // Create scene
        Scene gameScene = gameUI.createGameScene(mainLayout, mode);
        primaryStage.setScene(gameScene);

        // Handle window resizing
        gameScene.widthProperty().addListener((obs, old, newVal) ->
                cardUI.updateCardSizes(newVal.doubleValue(), gameScene.getHeight()));
        gameScene.heightProperty().addListener((obs, old, newVal) ->
                cardUI.updateCardSizes(gameScene.getWidth(), newVal.doubleValue()));

        // Start game
        game.play();
        statusPanel.startTimerUpdates();
    }

    public void restartGame() {
        statusPanel.stopTimerUpdates();
        game.resetGame();
        cardUI.resetCards();
        controlUI.resetControls();
        statusPanel.startTimerUpdates();
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof Player) {
            statusPanel.updateScoreDisplay();
        } else if (arg != null) {
            if ("GAME_OVER".equals(arg)) {
                statusPanel.stopTimerUpdates();
                gameUI.showGameOverMessage(true, game);
            } else if ("TIME_UP".equals(arg)) {
                statusPanel.stopTimerUpdates();
                gameUI.showGameOverMessage(false, game);
            }
        }
    }

    public static void main(String[] args) {
        Card.setBackImagePath("file:src/main/resources/images/back2.jpg");
        launch(args);
    }
}
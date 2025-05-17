//package main.cardgame.ui;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//import javafx.geometry.Pos;
//import javafx.animation.FadeTransition;
//import javafx.animation.PauseTransition;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Screen;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import main.cardgame.game.EndlessGame;
//import main.cardgame.game.Game;
//import main.cardgame.model.Card;
//import main.cardgame.model.Deck;
//import main.cardgame.model.GameBoard;
//import main.cardgame.model.Player;
//import javafx.animation.ScaleTransition;
//import javafx.scene.control.DialogPane;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Observable;
//import java.util.Observer;
//
//
//public class GameBoardVisualizer extends Application implements Observer {
//    private static  int BOARD_ROWS = 4;
//    private static  int BOARD_COLS = 4;
//    private static final double CARD_ASPECT_RATIO = 2.0 / 3.0;
//    private static final double PADDING = 20;
//    private static final double GAP = 10;
//
//    private Game game;
//    private GameBoard board;
//    private Card firstFlippedCard;
//    private Card secondFlippedCard;
//    private final Map<Card, ImageView> cardViews = new HashMap<>();
//    private GridPane gridPane;
//    private Label scoreLabel;
//    private Label timeLabel;
//    private Label moveLabel;
//    private Button restartButton;
//    private Button exitButton;
//
//    private void setupGreetingAndDifficultyScreen(Stage primaryStage) {
//        // Create greeting message
//        Label greetingLabel = new Label("Welcome to the Memory Card Game!");
//        greetingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
//
//        // Create buttons for difficulty levels
//        Button easyButton = new Button("Easy");
//        Button mediumButton = new Button("Medium");
//        Button hardButton = new Button("Hard");
//
//        // Create a start button
//        Button startButton = new Button("Start");
//        startButton.setDisable(true); // Initially disabled
//        startButton.setPrefSize(150,50);
//
//        easyButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
//        mediumButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
//        hardButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #4682b4; -fx-text-fill: white; -fx-background-radius: 10;");
//        startButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #32cd32; -fx-text-fill: white; -fx-background-radius: 10;");
//
//        // Difficulty selection logic
//        easyButton.setOnAction(e -> {
//            setDifficulty("easy");
//            startButton.setDisable(false);
//        });
//        mediumButton.setOnAction(e -> {
//            setDifficulty("medium");
//            startButton.setDisable(false);
//        });
//        hardButton.setOnAction(e -> {
//            setDifficulty("hard");
//            startButton.setDisable(false);
//        });
//
//        // Start button logic
//        startButton.setOnAction(e -> startGameWithDifficulty(primaryStage, selectedDifficulty));
//
//        // Arrange components in a VBox
//        VBox vbox = new VBox(20, greetingLabel, easyButton, mediumButton, hardButton, startButton);
//        vbox.setAlignment(Pos.CENTER);
//        vbox.setPadding(new Insets(PADDING));
//        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa);");
//
//        // Set up the scene
//        Scene scene = new Scene(vbox, 400, 300);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Welcome");
//        primaryStage.show();
//    }
//
//    // Helper method to store the selected difficulty
//    private String selectedDifficulty;
//
//    private void setDifficulty(String difficulty) {
//        selectedDifficulty = difficulty;
//    }
//
//    private void startGameWithDifficulty(Stage primaryStage, String difficulty) {
//        switch (difficulty) {
//            case "easy":
//                BOARD_ROWS = 4;
//                BOARD_COLS = 4;
//                break;
//            case "medium":
//                BOARD_ROWS = 6;
//                BOARD_COLS = 6;
//                break;
//            case "hard":
//                BOARD_ROWS = 8;
//                BOARD_COLS = 8;
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
//        }
//
//        int totalCardsNeeded = BOARD_ROWS * BOARD_COLS;
//        int requiredPairs = totalCardsNeeded / 2;
//
//        Deck deck = new Deck().createDeck(requiredPairs);
//        this.board = new GameBoard(difficulty, deck.getCards());
//        Player player = new Player("Player1");
//        this.game = new EndlessGame(board, player);
//
//        setupUI(primaryStage);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        setupGreetingAndDifficultyScreen(primaryStage);
//    }
//
//
//    private void initializeGame() {
//        int totalCardsNeeded = BOARD_ROWS * BOARD_COLS;
//        int requiredPairs = totalCardsNeeded / 2;
//
//        Deck deck = new Deck().createDeck(requiredPairs);
//        List<Card> cards = deck.getCards();
//
//        if (cards.size() != totalCardsNeeded) {
//            throw new IllegalStateException(
//                    "Deck created " + cards.size() + " cards but need " + totalCardsNeeded
//            );
//        }
//
//        this.board = new GameBoard("easy", cards);
//        Player player = new Player("Player1");
//        this.game = new EndlessGame(board, player);
//    }
//
//    private void setupUI(Stage primaryStage) {
//        Screen screen = Screen.getPrimary();
//        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
//        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;
//
//        double maxCardWidth = (maxWidth - 2 * PADDING - (BOARD_COLS - 1) * GAP) / BOARD_COLS;
//        double maxCardHeight = (maxHeight - 2 * PADDING - (BOARD_ROWS - 1) * GAP) / BOARD_ROWS;
//
//        double cardWidth = Math.min(maxCardWidth, maxCardHeight * CARD_ASPECT_RATIO);
//        double cardHeight = cardWidth / CARD_ASPECT_RATIO;
//
//        gridPane = new GridPane();
//        gridPane.setHgap(GAP);
//        gridPane.setVgap(GAP);
//        gridPane.setPadding(new Insets(PADDING));
//
//        for (int row = 0; row < BOARD_ROWS; row++) {
//            for (int col = 0; col < BOARD_COLS; col++) {
//                Card card = board.getCard(row, col);
//
//                if (card == null) { // Add null check
//                    throw new IllegalStateException(
//                            "No card found at position [" + row + "][" + col + "]"
//                    );
//                }
//
//                Button cardButton = createCardButton(card, cardWidth, cardHeight);
//                gridPane.add(cardButton, col, row);
//                cardViews.put(card, (ImageView) cardButton.getGraphic());
//            }
//        }
//
//        Scene scene = new Scene(gridPane);
//        primaryStage.setTitle("Memory Card Game - Endless Mode");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//
//
//        game.play();
//    }
//
//    private Button createCardButton(Card card, double cardWidth, double cardHeight) {
//        Button cardButton = new Button();
//        cardButton.setPrefSize(cardWidth, cardHeight);
//        cardButton.setPadding(Insets.EMPTY);
//
//        ImageView imageView = new ImageView(new Image(Card.getBackImagePath()));
//        imageView.setFitWidth(cardWidth);
//        imageView.setFitHeight(cardHeight);
//        cardButton.setGraphic(imageView);
//
//        cardButton.setOnAction(event -> handleCardFlip(card, imageView));
//        return cardButton;
//    }
//
//    private void handleCardFlip(Card card, ImageView imageView) {
//        if (card.isMatched() || card.isFaceUp() || secondFlippedCard != null) return;
//
//        ScaleTransition flipOut = new ScaleTransition(Duration.millis(200), imageView);
//        flipOut.setFromX(1.0);
//        flipOut.setToX(0.0); // Shrink horizontally
//        flipOut.setOnFinished(event -> {
//            card.flip(); // Flip the card's state
//            updateCardImage(card, imageView); // Update the image to show the other side
//
//            ScaleTransition flipIn = new ScaleTransition(Duration.millis(200), imageView);
//            flipIn.setFromX(0.0);
//            flipIn.setToX(1.0); // Expand back to full size
//            flipIn.play();
//        });
//        flipOut.play();
//
//        if (firstFlippedCard == null) {
//            firstFlippedCard = card;
//        } else {
//            secondFlippedCard = card;
//            boolean isMatch = game.processTurn(firstFlippedCard, secondFlippedCard);
//            checkForMatch(isMatch);
//        }
//    }
//
//    private void updateCardImage(Card card, ImageView imageView) {
//        String path = card.isFaceUp() ? card.getImagePath() : Card.getBackImagePath();
//        imageView.setImage(new Image(path));
//    }
//
//    private void checkForMatch(boolean isMatch) {
//        if (isMatch) {
//            handleMatch();
//        } else {
//            handleMismatch();
//        }
//        resetSelections();
//    }
//
//    private void handleMatch() {
//        animateMatch(firstFlippedCard, secondFlippedCard);
//        if (game.isGameOver()) {
//            game.endGame();
//            showWinMessage();
//        }
//    }
//
//    private void handleMismatch() {
//        animateMismatch(firstFlippedCard, secondFlippedCard);
//    }
//
//    private void resetSelections() {
//        firstFlippedCard = null;
//        secondFlippedCard = null;
//    }
//
//    private void animateMatch(Card card1, Card card2) {
//        FadeTransition ft = new FadeTransition(Duration.millis(500), cardViews.get(card1));
//        ft.setFromValue(1.0);
//        ft.setToValue(0.3);
//        ft.setCycleCount(2);
//        ft.setAutoReverse(true);
//        ft.play();
//
//        ft = new FadeTransition(Duration.millis(500), cardViews.get(card2));
//        ft.setFromValue(1.0);
//        ft.setToValue(0.3);
//        ft.setCycleCount(2);
//        ft.setAutoReverse(true);
//        ft.play();
//    }
//
//    private void animateMismatch(Card card1, Card card2) {
//        PauseTransition pause = new PauseTransition(Duration.seconds(1));
//        pause.setOnFinished(event -> Platform.runLater(() -> {
//            card1.flip();
//            card2.flip();
//            updateCardImage(card1, cardViews.get(card1));
//            updateCardImage(card2, cardViews.get(card2));
//        }));
//        pause.play();
//    }
//
//    private void showWinMessage() {
//        Platform.runLater(() -> {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Game Won!");
//            alert.setHeaderText(null); // Disable default header text
//            alert.setContentText(
//                    "All matches found!\n" +
//                            "Score: " + game.getPlayer().getScore() + "\n" +
//                            "Time: " + game.getTimer().getElapsedTime() + " seconds\n" +
//                            "Moves: " + game.getPlayer().getMoves()
//            );
//
//            // Apply custom styling to the dialog
//            DialogPane dialogPane = alert.getDialogPane();
//            dialogPane.setStyle(
//                    "-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa); " +
//                            "-fx-border-color: gold; " +
//                            "-fx-border-width: 3; " +
//                            "-fx-border-radius: 10; " +
//                            "-fx-background-radius: 10;"
//            );
//
//            // Add a custom header label
//            Label headerLabel = new Label("Congratulations!");
//            headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4682b4;");
//            dialogPane.setHeader(headerLabel);
//
//            alert.showAndWait();
//        });
//    }
//
//    public static void main(String[] args) {
//        Card.setBackImagePath("file:src/main/resources/images/back2.jpg");
//        launch(args);
//    }
//}

package main.cardgame.ui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.cardgame.game.EndlessGame;
import main.cardgame.game.Game;
import main.cardgame.game.TimedGame;
import main.cardgame.model.Card;
import main.cardgame.model.Deck;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GameBoardVisualizer2 extends Application implements Observer {
    private static int BOARD_ROWS = 4;
    private static int BOARD_COLS = 4;
    private static final double CARD_ASPECT_RATIO = 2.0 / 3.0;
    private static final double PADDING = 20;
    private static final double GAP = 10;

    private Game game;
    private GameBoard board;
    private Card firstFlippedCard;
    private Card secondFlippedCard;
    private final Map<Card, ImageView> cardViews = new HashMap<>();
    private GridPane gridPane;
    private Label scoreLabel;
    private Label moveLabel;
    private Label timeLabel;
    private Button pauseButton;
    private Button restartButton;
    private Timeline timerUpdateTimeline;
    private String currentMode;
    private String currentDifficulty;
    private Button mainMenuButton;

    @Override
    public void start(Stage primaryStage) {
        setupSelectionScreen(primaryStage);
    }

    private void setupSelectionScreen(Stage primaryStage) {
        Label titleLabel = new Label("Memory Card Game");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Game mode selection
        Label modeLabel = new Label("Select Game Mode:");
        modeLabel.setStyle("-fx-font-size: 16px;");

        ComboBox<String> modeComboBox = new ComboBox<>();
        modeComboBox.getItems().addAll("Endless Mode", "Timed Mode");
        modeComboBox.setValue("Endless Mode");

        // Difficulty selection
        Label difficultyLabel = new Label("Select Difficulty:");
        difficultyLabel.setStyle("-fx-font-size: 16px;");

        ComboBox<String> difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyComboBox.setValue("Easy");

        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #32cd32; -fx-text-fill: white; -fx-background-radius: 10;");
        startButton.setPrefSize(150, 50);

        startButton.setOnAction(e -> {
            String mode = modeComboBox.getValue();
            String difficulty = difficultyComboBox.getValue().toLowerCase();
            startGameWithSettings(primaryStage, mode, difficulty);
        });

        VBox vbox = new VBox(20, titleLabel, modeLabel, modeComboBox, difficultyLabel, difficultyComboBox, startButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(PADDING));
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa);");

        Scene scene = new Scene(vbox, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Memory Card Game");
        primaryStage.show();
    }

    private void startGameWithSettings(Stage primaryStage, String mode, String difficulty) {
        this.currentMode = mode;
        this.currentDifficulty = difficulty;
        // Set board dimensions based on difficulty
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

        // Create deck and board
        Deck deck = Deck.createDeckForLevel(difficulty);
        this.board = new GameBoard(difficulty, deck.getCards());
        Player player = new Player("Player1");

        // Register as observer for player
        player.addObserver(this);

        // Create appropriate game type based on mode
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

    private void setupGameUI(Stage primaryStage, String mode) {
        // Create the main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(PADDING));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #e6e6fa);");

        // Create status panel for score, moves, and time
        createStatusPanel(mainLayout, mode);

        // Create the game board
        createGameBoard(mainLayout);

        // Create the scene
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;
        Scene scene = new Scene(mainLayout, Math.min(maxWidth, 1024), Math.min(maxHeight, 768));

        primaryStage.setTitle("Memory Card Game - " + mode);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> stopTimerUpdates());

        // Start the game
        game.play();
        startTimerUpdates();
    }

    private void createStatusPanel(BorderPane mainLayout, String mode) {
        // Create labels for player stats
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        moveLabel = new Label("Moves: 0");
        moveLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        timeLabel = new Label(mode.equals("Timed Mode") ? "Time Left: 00:00" : "Time: 00:00");
        timeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Create game control buttons
        pauseButton = new Button("Pause");
        pauseButton.setStyle("-fx-font-size: 14px;");
        pauseButton.setOnAction(e -> togglePauseGame());

        restartButton = new Button("Restart");
        restartButton.setStyle("-fx-font-size: 14px;");
        restartButton.setOnAction(e -> {
            if (showConfirmationDialog("Are you sure you want to restart the game?")) {
                restartGame();
            }
        });

        mainMenuButton = new Button("Main Menu");
        mainMenuButton.setStyle("-fx-font-size: 14px;");
        mainMenuButton.setOnAction(e -> {
            if (showConfirmationDialog("Are you sure you want to return to the main menu?")) {
                stopTimerUpdates();
                setupSelectionScreen((Stage) mainMenuButton.getScene().getWindow());
            }
        });


        // Create layout for stats
        HBox statsBox = new HBox(20, scoreLabel, moveLabel, timeLabel);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        // Create layout for buttons
        HBox buttonsBox = new HBox(10, pauseButton, restartButton, mainMenuButton);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        // Combine stats and buttons
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(0, 0, 10, 0));
        statusBar.getChildren().addAll(statsBox, buttonsBox);
        HBox.setHgrow(statsBox, javafx.scene.layout.Priority.ALWAYS);

        mainLayout.setTop(statusBar);
    }

    private void createGameBoard(BorderPane mainLayout) {
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;

        double maxCardWidth = (maxWidth - 2 * PADDING - (BOARD_COLS - 1) * GAP) / BOARD_COLS;
        double maxCardHeight = (maxHeight - 2 * PADDING - (BOARD_ROWS - 1) * GAP - 60) / BOARD_ROWS; // 60 for status panel

        double cardWidth = Math.min(maxCardWidth, maxCardHeight * CARD_ASPECT_RATIO);
        double cardHeight = cardWidth / CARD_ASPECT_RATIO;

        gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setAlignment(Pos.CENTER);

        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Card card = board.getCard(row, col);

                if (card == null) {
                    throw new IllegalStateException(
                            "No card found at position [" + row + "][" + col + "]"
                    );
                }

                // Register as observer for each card
                card.addObserver(this);

                Button cardButton = createCardButton(card, cardWidth, cardHeight);
                gridPane.add(cardButton, col, row);
                cardViews.put(card, (ImageView) cardButton.getGraphic());
            }
        }

        mainLayout.setCenter(gridPane);
    }

    private Button createCardButton(Card card, double cardWidth, double cardHeight) {
        Button cardButton = new Button();
        cardButton.setPrefSize(cardWidth, cardHeight);
        cardButton.setPadding(Insets.EMPTY);

        ImageView imageView = new ImageView(new Image(Card.getBackImagePath()));
        imageView.setFitWidth(cardWidth);
        imageView.setFitHeight(cardHeight);
        cardButton.setGraphic(imageView);

        cardButton.setOnAction(event -> handleCardFlip(card, imageView));
        return cardButton;
    }

    private void handleCardFlip(Card card, ImageView imageView) {
        if (card.isMatched() || card.isFaceUp() || secondFlippedCard != null || !game.isActive()) return;

        ScaleTransition flipOut = new ScaleTransition(Duration.millis(200), imageView);
        flipOut.setFromX(1.0);
        flipOut.setToX(0.0);
        flipOut.setOnFinished(event -> {
            card.flip();
            updateCardImage(card, imageView);

            ScaleTransition flipIn = new ScaleTransition(Duration.millis(200), imageView);
            flipIn.setFromX(0.0);
            flipIn.setToX(1.0);
            flipIn.play();
        });
        flipOut.play();

        if (firstFlippedCard == null) {
            firstFlippedCard = card;
        } else {
            secondFlippedCard = card;
            boolean isMatch = game.processTurn(firstFlippedCard, secondFlippedCard);
            checkForMatch(isMatch);
        }
    }

    private void updateCardImage(Card card, ImageView imageView) {
        String path = card.isFaceUp() ? card.getImagePath() : Card.getBackImagePath();
        imageView.setImage(new Image(path));
    }

    private void checkForMatch(boolean isMatch) {
        if (isMatch) {
            handleMatch();
        } else {
            handleMismatch();
        }
        resetSelections();
    }

    private void handleMatch() {
        animateMatch(firstFlippedCard, secondFlippedCard);
        if (game.isGameOver()) {
            game.endGame();
            stopTimerUpdates();
            showGameOverMessage();
        }
    }

    private void handleMismatch() {
        animateMismatch(firstFlippedCard, secondFlippedCard);
    }

    private void resetSelections() {
        firstFlippedCard = null;
        secondFlippedCard = null;
    }

    private void animateMatch(Card card1, Card card2) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), cardViews.get(card1));
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();

        ft = new FadeTransition(Duration.millis(500), cardViews.get(card2));
        ft.setFromValue(1.0);
        ft.setToValue(0.3);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.play();
    }


    private void animateMismatch(Card card1, Card card2) {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> Platform.runLater(() -> {
            card1.flip();
            card2.flip();
            updateCardImage(card1, cardViews.get(card1));
            updateCardImage(card2, cardViews.get(card2));
        }));
        pause.play();
    }

    private void showGameOverMessage() {
        Platform.runLater(() -> {
            boolean isWin = board.allCardsMatched();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(isWin ? "Game Won!" : "Game Over");
            alert.setHeaderText(null);

            String timeDisplay;
            if (game.getTimer().isCountdown()) {
                long remainingTime = game.getTimer().getRemainingTime() / 1000;
                timeDisplay = "Time Left: " + remainingTime + " seconds";
            } else {
                long elapsedTime = game.getTimer().getElapsedTime() / 1000;
                timeDisplay = "Time: " + elapsedTime + " seconds";
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

    private void startTimerUpdates() {
        stopTimerUpdates(); // Clear any existing timer

        timerUpdateTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> updateTimerDisplay())
        );
        timerUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        timerUpdateTimeline.play();
    }

    private void stopTimerUpdates() {
        if (timerUpdateTimeline != null) {
            timerUpdateTimeline.stop();
            timerUpdateTimeline = null;
        }
    }

    private void updateTimerDisplay() {
        Platform.runLater(() -> {
            if (game.getTimer().isCountdown()) {
                long seconds = game.getTimer().getRemainingTime() / 1000;
                timeLabel.setText(String.format("Time Left: %02d:%02d", seconds / 60, seconds % 60));

                // Check for time-up in timed games
                if (game.getTimer().isTimeUp() && !game.isGameOver()) {
                    game.endGame();
                    stopTimerUpdates();
                    showGameOverMessage();
                }
            } else {
                long seconds = game.getTimer().getElapsedTime() / 1000;
                timeLabel.setText(String.format("Time: %02d:%02d", seconds / 60, seconds % 60));
            }
        });
    }

    private void togglePauseGame() {
        if (game.isActive() && !game.isPaused()) {
            game.pauseGame();
            pauseButton.setText("Resume");
            stopTimerUpdates();
        } else if (game.isActive() && game.isPaused()) {
            game.resumeGame();
            pauseButton.setText("Pause");
            startTimerUpdates();
        }
    }

    private boolean showConfirmationDialog(String message) {
        // Pause game and timer before showing dialog (like Pause)
        if (game.isActive() && !game.isPaused()) {
            game.pauseGame();
            stopTimerUpdates();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, javafx.scene.control.ButtonType.YES, javafx.scene.control.ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.NO).requestFocus();
        alert.getDialogPane().setStyle(
                "-fx-background-color: linear-gradient(to bottom, #f0f8ff, #87cefa); " +
                        "-fx-border-color: #4682b4; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 10; " +
                        "-fx-background-radius: 10;"
        );
        boolean result = alert.showAndWait().orElse(javafx.scene.control.ButtonType.NO) == javafx.scene.control.ButtonType.YES;

        // Resume game and timer if cancelled (like Resume)
        if (!result && game.isPaused()) {
            game.resumeGame();
            startTimerUpdates();
        }
        return result;
    }
    private void restartGame() {
        stopTimerUpdates();
        // Re-initialize everything as if starting fresh
        Stage stage = (Stage) restartButton.getScene().getWindow();
        startGameWithSettings(stage, currentMode, currentDifficulty);
    }
    private void returnToMainMenu(Stage primaryStage) {
        stopTimerUpdates();
        setupSelectionScreen(primaryStage);
    }

    @Override
    public void update(Observable observable, Object arg) {
        Platform.runLater(() -> {
            // Handle player updates
            if (observable instanceof Player) {
                scoreLabel.setText("Score: " + game.getPlayer().getScore());
                moveLabel.setText("Moves: " + game.getPlayer().getMoves());
            }

            // Handle card updates
            else if (observable instanceof Card && arg instanceof String) {
                Card card = (Card) observable;
                ImageView imageView = cardViews.get(card);
                if (imageView != null) {
                    updateCardImage(card, imageView);
                }
            }

            // Handle timer updates
            else if (observable == game.getTimer() && "TIMER_STOPPED".equals(arg)) {
                updateTimerDisplay();
            }
        });
    }

    public static void main(String[] args) {
        Card.setBackImagePath("file:src/main/resources/images/back2.jpg");
        launch(args);
    }
}
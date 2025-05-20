package main.cardgame.ui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.cardgame.game.Game;
import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;

import java.util.HashMap;
import java.util.Map;

/**
 * Comprehensive card rendering and board management class
 * Combines card rendering, animations, and board layout functionality
 */

public class CardRenderer {
    /** Default padding for card layout */
    private static final double PADDING = 20;
    /** Default gap between cards */
    private static final double GAP = 10;

    /** Reference to the parent GameBoardUI */
    private GameBoardUI gameBoardUI;
    /** Current game instance */
    private Game game;
    /** Game board model */
    private GameBoard board;

    /** Number of columns in the grid */
    private int cols;
    /** Number of rows in the grid */
    private int rows;
    /** Aspect ratio for cards (width/height) */
    private double cardAspectRatio;
    /** Gap between cards */
    private double gap;
    /** Grid pane containing all card buttons */
    private GridPane gridPane;

    /** First card selected in a turn */
    private Card firstFlippedCard;
    /** Second card selected in a turn */
    private Card secondFlippedCard;
    /** Map of cards to their ImageView representations */
    private final Map<Card, ImageView> cardViews = new HashMap<>();
    /** Map of cards to their Button containers */
    private final Map<Card, Button> cardButtons = new HashMap<>();

    /** Overlay pane for pause (blocks input and grays out) */
    private StackPane pauseOverlay;
    /** Label displayed when game is paused */
    private Label gamePausedLabel;

    /** Flag indicating if cards are currently flipping */
    private boolean cardClicksBlocked = false;
    /** Flag indicating if the first card is being flipped */
    private boolean flippingFirstCard = false;

    /**
     * Creates a new card renderer
     * @param gameBoardUI Parent GameBoardUI instance
     * @param game Current game instance
     * @param board Game board model
     * @param cols Number of columns
     * @param rows Number of rows
     * @param cardAspectRatio Card aspect ratio
     * @param gap Gap between cards
     */
    public CardRenderer(GameBoardUI gameBoardUI, Game game, GameBoard board, int cols, int rows,
                        double cardAspectRatio, double gap) {
        this.gameBoardUI = gameBoardUI;
        this.game = game;
        this.board = board;
        this.cols = cols;
        this.rows = rows;
        this.cardAspectRatio = cardAspectRatio;
        this.gap = gap;

        initializeGrid();
        initializeCards();
        initializePauseOverlay();
    }

    /**
     * Initializes the game board grid
     */
    private void initializeGrid() {
        gridPane = new GridPane();
        gridPane.setHgap(gap);
        gridPane.setVgap(gap);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(PADDING));

        // Responsive card sizing
        gridPane.widthProperty().addListener((obs, oldVal, newVal) -> resizeCards());
        gridPane.heightProperty().addListener((obs, oldVal, newVal) -> resizeCards());
    }

    /**
     * Initializes all card elements
     */
    private void initializeCards() {
        double initialCardWidth = 100;
        double initialCardHeight = 100 / cardAspectRatio;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = board.getCard(row, col);
                if (card == null) {
                    throw new IllegalStateException(
                            "No card found at position [" + row + "][" + col + "]"
                    );
                }

                card.addObserver(gameBoardUI);
                Button cardButton = createCardButton(card, initialCardWidth, initialCardHeight);
                cardButtons.put(card, cardButton);
                gridPane.add(cardButton, col, row);
                cardViews.put(card, (ImageView) cardButton.getGraphic());
            }
        }
    }

    /**
     * Initializes the pause overlay
     */
    private void initializePauseOverlay() {
        // Create a semi-transparent white rectangle to gray out the board
        Rectangle dim = new Rectangle();
        dim.setFill(Color.rgb(255, 255, 255, 0.45)); // White, subtle opacity
        dim.widthProperty().bind(gridPane.widthProperty());
        dim.heightProperty().bind(gridPane.heightProperty());

        // Create the pause label
        gamePausedLabel = new Label("Game Paused");
        gamePausedLabel.setStyle(
                "-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #fff; " +
                        "-fx-background-color: rgba(70,130,180,0.85); -fx-padding: 30px; -fx-background-radius: 15;"
        );
        gamePausedLabel.setAlignment(Pos.CENTER);

        // Overlay pane
        pauseOverlay = new StackPane(dim, gamePausedLabel);
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setVisible(false);
        pauseOverlay.setPickOnBounds(true); // Block mouse events
    }

    /**
     * Creates a styled card button with click handler
     * @param card The card to create a button for
     * @param cardWidth The width of the card
     * @param cardHeight The height of the card
     * @return The created card button
     */
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

    /**
     * Handles responsive card resizing
     */
    private void resizeCards() {
        double gridWidth = gridPane.getWidth();
        double gridHeight = gridPane.getHeight();

        if (gridWidth <= 0 || gridHeight <= 0) return;

        double availableWidth = gridWidth - (cols - 1) * gap - 2 * PADDING;
        double availableHeight = gridHeight - (rows - 1) * gap - 2 * PADDING;

        double cardWidth = Math.min(availableWidth / cols, (availableHeight / rows) * cardAspectRatio);
        double cardHeight = cardWidth / cardAspectRatio;

        for (Card card : cardButtons.keySet()) {
            Button btn = cardButtons.get(card);
            btn.setPrefSize(cardWidth, cardHeight);
            ImageView iv = cardViews.get(card);
            if (iv != null) {
                iv.setFitWidth(cardWidth);
                iv.setFitHeight(cardHeight);
            }
        }
    }

    /**
     * Handles card flip animation and game logic
     * @param card The card to flip
     * @param imageView The ImageView of the card
     */
    private void handleCardFlip(Card card, ImageView imageView) {
        if (cardClicksBlocked || card.isMatched() || card.isFaceUp() || !game.isActive()) return;

        if (firstFlippedCard != null && card == firstFlippedCard) return;

        if (firstFlippedCard == null && !flippingFirstCard) {
            cardClicksBlocked = true;
            flippingFirstCard = true;
            animateCardFlip(card, imageView, () -> {
                firstFlippedCard = card;
                flippingFirstCard = false;
                cardClicksBlocked = false;
            });
        } else if (secondFlippedCard == null) {
            cardClicksBlocked = true;
            animateCardFlip(card, imageView, () -> {
                secondFlippedCard = card;
                boolean isMatch = game.processTurn(firstFlippedCard, secondFlippedCard);
                checkForMatch(isMatch);
            });
        }
    }

    /**
     * Animates a card flip with optional completion handler
     * @param card The card to flip
     * @param imageView The ImageView of the card
     * @param onFinished The completion handler to run after the flip
     */
    private void animateCardFlip(Card card, ImageView imageView, Runnable onFinished) {
        ScaleTransition flipOut = new ScaleTransition(Duration.millis(200), imageView);
        flipOut.setFromX(1.0);
        flipOut.setToX(0.0);
        flipOut.setOnFinished(event -> {
            card.flip();
            updateCardImage(card, imageView);
            ScaleTransition flipIn = new ScaleTransition(Duration.millis(200), imageView);
            flipIn.setFromX(0.0);
            flipIn.setToX(1.0);
            if (onFinished != null) flipIn.setOnFinished(e -> onFinished.run());
            flipIn.play();
        });
        flipOut.play();
    }

    /**
     * Updates the displayed image for a card
     * @param card The card to update
     * @param imageView The ImageView of the card
     */
    private void updateCardImage(Card card, ImageView imageView) {
        String path = card.isFaceUp() ? card.getImagePath() : Card.getBackImagePath();
        imageView.setImage(new Image(path));
    }

    /**
     * Handles match checking logic
     * @param isMatch True if the cards match, false otherwise
     */
    private void checkForMatch(boolean isMatch) {
        cardClicksBlocked = true;
        if (isMatch) {
            handleMatch();
        } else {
            handleMismatch();
        }
    }

    /**
     * Handles successful matches
     */
    private void handleMatch() {
        animateMatch(firstFlippedCard, secondFlippedCard);
        PauseTransition pause = new PauseTransition(Duration.millis(250));
        pause.setOnFinished(e -> {
            cardClicksBlocked = false;
            resetSelections();
            if (game.isGameOver()) {
                game.endGame();
                gameBoardUI.statusPanel.stopTimerUpdates();
                gameBoardUI.showGameOverMessage();
            }
        });
        pause.play();
    }

    /**
     * Handles unsuccessful matches
     */
    private void handleMismatch() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.35));
        pause.setOnFinished(event -> Platform.runLater(() -> {
            animateCardFlip(firstFlippedCard, cardViews.get(firstFlippedCard), null);
            animateCardFlip(secondFlippedCard, cardViews.get(secondFlippedCard), null);
            cardClicksBlocked = false;
            resetSelections();
        }));
        pause.play();
    }

    /**
     * Animates a successful match
     * @param card1 The first card in the match
     * @param card2 The second card in the match
     */
    private void animateMatch(Card card1, Card card2) {
        if (card1 == null || card2 == null || card1 == card2) return;

        FadeTransition ft1 = new FadeTransition(Duration.millis(500), cardViews.get(card1));
        ft1.setFromValue(1.0);
        ft1.setToValue(0.3);
        ft1.setCycleCount(2);
        ft1.setAutoReverse(true);
        ft1.play();

        FadeTransition ft2 = new FadeTransition(Duration.millis(500), cardViews.get(card2));
        ft2.setFromValue(1.0);
        ft2.setToValue(0.3);
        ft2.setCycleCount(2);
        ft2.setAutoReverse(true);
        ft2.play();
    }

    /**
     * Resets the card selection state
     */
    private void resetSelections() {
        firstFlippedCard = null;
        secondFlippedCard = null;
    }

    // ========== Public Interface ========== //

    /**
     * Gets the game board grid pane
     * @return The grid pane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Gets the pause overlay
     * @return The pause overlay
     */
    public StackPane getPauseOverlay() {
        return pauseOverlay;
    }

    /**
     * Shows or hides the pause overlay
     * @param visible True to show the overlay, false to hide it
     */
    public void showPauseOverlay(boolean visible) {
        pauseOverlay.setVisible(visible);
        setCardButtonsDisabled(visible);
    }

    /**
     * Enables/disables all card buttons
     * @param disabled True to disable buttons, false to enable them
     */
    public void setCardButtonsDisabled(boolean disabled) {
        for (Button btn : cardButtons.values()) {
            btn.setDisable(disabled);
        }
    }

    /**
     * Updates a card's display after state change
     * @param card The card to update
     */
    public void handleCardUpdate(Card card) {
        ImageView imageView = cardViews.get(card);
        if (imageView != null) {
            updateCardImage(card, imageView);
        }
    }
}


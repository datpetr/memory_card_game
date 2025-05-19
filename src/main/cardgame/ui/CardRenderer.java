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
import javafx.util.Duration;
import main.cardgame.game.Game;
import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles card display and animations
 * Preserves the original UI components and animation logic from GameBoardVisualizer2
 */
public class CardRenderer {
    private static final double PADDING = 20;
    private static final double GAP = 10;

    private GameBoardUI gameBoardUI;
    private Game game;
    private GameBoard board;
    private int cols;  // Using cols first to match GridPane convention (col, row)
    private int rows;
    private double cardAspectRatio;
    private double gap;

    private Card firstFlippedCard;
    private Card secondFlippedCard;
    private final Map<Card, ImageView> cardViews = new HashMap<>();
    private final Map<Card, Button> cardButtons = new HashMap<>();
    private GridPane gridPane;
    private Label gamePausedLabel;
    private boolean cardClicksBlocked = false;
    private boolean flippingFirstCard = false;

    public CardRenderer(GameBoardUI gameBoardUI, Game game, GameBoard board,
                        int cols, int rows, double cardAspectRatio, double gap) {
        this.gameBoardUI = gameBoardUI;
        this.game = game;
        this.board = board;
        this.cols = cols;
        this.rows = rows;
        this.cardAspectRatio = cardAspectRatio;
        this.gap = gap;

        initialize();
    }

    /**
     * Initializes the card renderer and creates the game board
     */
    private void initialize() {
        // Create the grid pane for cards
        gridPane = new GridPane();
        gridPane.setHgap(gap);
        gridPane.setVgap(gap);
        gridPane.setAlignment(Pos.CENTER);

        // Responsive card sizing: bind to parent grid size
        gridPane.widthProperty().addListener((obs, oldVal, newVal) -> resizeCards());
        gridPane.heightProperty().addListener((obs, oldVal, newVal) -> resizeCards());

        // Initial card creation with placeholder size (will be resized)
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

        // Create pause overlay (identical to original)
        gamePausedLabel = new Label("Game Paused");
        gamePausedLabel.setStyle(
                "-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #ffffff; " +
                        "-fx-background-color: rgba(70,130,180,0.8); -fx-padding: 30px; -fx-background-radius: 15;"
        );
        gamePausedLabel.setVisible(false);
        gamePausedLabel.setAlignment(Pos.CENTER);
    }

    // Responsive resizing for cards
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
     * Creates a button for a card with the original style and behavior
     * @param card The card to create a button for
     * @param cardWidth The width of the card
     * @param cardHeight The height of the card
     * @return The created button
     */
    private Button createCardButton(Card card, double cardWidth, double cardHeight) {
        // Original button creation code
        Button cardButton = new Button();
        cardButton.setPrefSize(cardWidth, cardHeight);
        cardButton.setPadding(Insets.EMPTY);

        ImageView imageView = new ImageView(new Image(Card.getBackImagePath()));
        imageView.setFitWidth(cardWidth);
        imageView.setFitHeight(cardHeight);
        cardButton.setGraphic(imageView);

        // Original click handler
        cardButton.setOnAction(event -> handleCardFlip(card, imageView));
        return cardButton;
    }

    /**
     * Handles a card flip
     * Original implementation from GameBoardVisualizer2
     * @param card The card to flip
     * @param imageView The image view to update
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

    private void handleCardFlip(Card card, ImageView imageView) {
        if (cardClicksBlocked || card.isMatched() || card.isFaceUp() || !game.isActive()) return;

        // Allow two quick flips: do not block on first flip
        if (firstFlippedCard == null && !flippingFirstCard) {
            flippingFirstCard = true;
            animateCardFlip(card, imageView, () -> {
                firstFlippedCard = card;
                flippingFirstCard = false;
            });
        } else if (secondFlippedCard == null && card != firstFlippedCard) {
            cardClicksBlocked = true; // Block further clicks after second card
            animateCardFlip(card, imageView, () -> {
                secondFlippedCard = card;
                boolean isMatch = game.processTurn(firstFlippedCard, secondFlippedCard);
                checkForMatch(isMatch);
            });
        }
    }
    /**
     * Updates a card's image
     * Original implementation from GameBoardVisualizer2
     * @param card The card to update
     * @param imageView The image view to update
     */
    private void updateCardImage(Card card, ImageView imageView) {
        String path = card.isFaceUp() ? card.getImagePath() : Card.getBackImagePath();
        imageView.setImage(new Image(path));
    }

    /**
     * Checks if the flipped cards match
     * Original implementation from GameBoardVisualizer2
     * @param isMatch Whether the cards match
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
     * Handles a match
     * Original implementation from GameBoardVisualizer2
     */
    private void handleMatch() {
        animateMatch(firstFlippedCard, secondFlippedCard);
        PauseTransition pause = new PauseTransition(Duration.millis(300)); // Match animation total duration
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
     * Handles a mismatch
     * Original implementation from GameBoardVisualizer2
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
     * Resets the selected cards
     * Original implementation from GameBoardVisualizer2
     */
    private void resetSelections() {
        firstFlippedCard = null;
        secondFlippedCard = null;
    }

    /**
     * Animates a match
     * Original implementation from GameBoardVisualizer2
     * @param card1 The first card/
     * @param card2 The second card
     */
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

    /**
     * Animates a mismatch
     * Original implementation from GameBoardVisualizer2
     * @param card1 The first card
     * @param card2 The second card
     */
    private void animateMismatch(Card card1, Card card2) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(event -> Platform.runLater(() -> {
            animateCardFlip(card1, cardViews.get(card1), null);
            animateCardFlip(card2, cardViews.get(card2), null);
        }));
        pause.play();
    }

    /**
     * Sets whether card buttons are disabled
     * @param disabled Whether the buttons should be disabled
     */
    public void setCardButtonsDisabled(boolean disabled) {
        for (Button btn : cardButtons.values()) {
            btn.setDisable(disabled);
        }
    }

    /**
     * Updates a card's image after an update
     * @param card The card to update
     */
    public void handleCardUpdate(Card card) {
        ImageView imageView = cardViews.get(card);
        if (imageView != null) {
            updateCardImage(card, imageView);
        }
    }

    /**
     * Gets the grid pane containing all cards
     * @return The grid pane
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Gets the pause overlay label
     * @return The pause overlay label
     */
    public Label getPauseOverlay() {
        return gamePausedLabel;
    }

    /**
     * Shows or hides the pause overlay
     * @param visible Whether the overlay should be visible
     */
    public void showPauseOverlay(boolean visible) {
        gamePausedLabel.setVisible(visible);
    }
}


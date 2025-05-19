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

    public CardRenderer(GameBoardUI gameBoardUI, Game game, GameBoard board, 
                       int cols, int rows, double cardAspectRatio, double gap) {
        this.gameBoardUI = gameBoardUI;
        this.game = game;
        this.board = board;
        this.rows = rows;
        this.cols = cols;
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
        
        // Calculate card dimensions
        javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;

        double maxCardWidth = (maxWidth - 2 * PADDING - (cols - 1) * gap) / cols;
        double maxCardHeight = (maxHeight - 2 * PADDING - (rows - 1) * gap - 60) / rows; // 60 for status panel

        double cardWidth = Math.min(maxCardWidth, maxCardHeight * cardAspectRatio);
        double cardHeight = cardWidth / cardAspectRatio;
        
        // Create cards
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = board.getCard(row, col);

                if (card == null) {
                    throw new IllegalStateException(
                            "No card found at position [" + row + "][" + col + "]"
                    );
                }

                // Register as observer for each card
                card.addObserver(gameBoardUI);

                Button cardButton = createCardButton(card, cardWidth, cardHeight);
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
    private void handleCardFlip(Card card, ImageView imageView) {
        if (card.isMatched() || card.isFaceUp() || secondFlippedCard != null || !game.isActive()) return;

        // Original flip animation
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

        // Original game logic
        if (firstFlippedCard == null) {
            firstFlippedCard = card;
        } else {
            secondFlippedCard = card;
            boolean isMatch = game.processTurn(firstFlippedCard, secondFlippedCard);
            checkForMatch(isMatch);
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
        if (isMatch) {
            handleMatch();
        } else {
            handleMismatch();
        }
        resetSelections();
    }

    /**
     * Handles a match
     * Original implementation from GameBoardVisualizer2
     */
    private void handleMatch() {
        animateMatch(firstFlippedCard, secondFlippedCard);
        if (game.isGameOver()) {
            game.endGame();
            gameBoardUI.statusPanel.stopTimerUpdates();
            gameBoardUI.showGameOverMessage();
        }
    }

    /**
     * Handles a mismatch
     * Original implementation from GameBoardVisualizer2
     */
    private void handleMismatch() {
        animateMismatch(firstFlippedCard, secondFlippedCard);
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
     * @param card1 The first card
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
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> Platform.runLater(() -> {
            card1.flip();
            card2.flip();
            updateCardImage(card1, cardViews.get(card1));
            updateCardImage(card2, cardViews.get(card2));
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

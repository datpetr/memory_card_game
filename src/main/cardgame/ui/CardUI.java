package main.cardgame.ui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import main.cardgame.game.Game;
import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class CardUI extends Observable {
    private final Game game;
    private final GameBoard board;
    private final int rows;
    private final int cols;
    private GridPane gridPane;
    private final Map<Card, ImageView> cardViews = new HashMap<>();
    private Card firstFlippedCard;
    private Card secondFlippedCard;

    // Spacing parameters
    private static final double CARD_SPACING_PERCENT = 0.5;

    public CardUI(Game game, GameBoard board, int rows, int cols) {
        this.game = game;
        this.board = board;
        this.rows = rows;
        this.cols = cols;
        createCardGrid();
    }

    public GridPane getComponent() {
        return gridPane;
    }

    // Set up resize listeners after the component is added to scene
    public void setupResizeListeners() {
        Platform.runLater(() -> {
            if (gridPane.getScene() != null) {
                ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> {
                    updateCardSizes(gridPane.getScene().getWidth(), gridPane.getScene().getHeight());
                };

                gridPane.getScene().widthProperty().addListener(sizeListener);
                gridPane.getScene().heightProperty().addListener(sizeListener);

                // Initial sizing
                updateCardSizes(gridPane.getScene().getWidth(), gridPane.getScene().getHeight());
            }
        });
    }

    private void createCardGrid() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // Calculate percentage-based spacing
        double hGap = 10; // Will be adjusted in updateCardSizes
        double vGap = 10; // Will be adjusted in updateCardSizes
        gridPane.setHgap(hGap);
        gridPane.setVgap(vGap);

        // Initial card creation - sizes will be adjusted by updateCardSizes
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = board.getCard(row, col);
                Button cardButton = createCardButton(card, 100, 150); // Initial size, will be resized
                gridPane.add(cardButton, col, row);
                cardViews.put(card, (ImageView) cardButton.getGraphic());
            }
        }
    }

    public void updateCardSizes(double sceneWidth, double sceneHeight) {
        // Reserve space for top and bottom panels
        double topPanelHeight = sceneHeight * 0.1;     // 10% for score/timer
        double bottomPanelHeight = sceneHeight * 0.08; // 8% for control panel

        // Calculate available space for card grid with margins
        double horizontalMargin = sceneWidth * 0.03;   // 3% margin on each side
        double verticalMargin = sceneHeight * 0.03;    // 3% margin on each side

        double availableWidth = sceneWidth - (2 * horizontalMargin);
        double availableHeight = sceneHeight - topPanelHeight - bottomPanelHeight - (2 * verticalMargin);

        // Calculate optimal spacing between cards (dynamically adjusted)
        double spacing = Math.min(availableWidth, availableHeight) * 0.02;
        gridPane.setHgap(spacing);
        gridPane.setVgap(spacing);

        // Calculate maximum possible card dimensions
        double maxCardWidth = (availableWidth - ((cols - 1) * spacing)) / cols;
        double maxCardHeight = (availableHeight - ((rows - 1) * spacing)) / rows;

        // Maintain card aspect ratio (standard playing cards use ~1.4-1.5 ratio)
        double aspectRatio = 0.75;

        // Calculate optimal size to fit within constraints while maintaining aspect ratio
        double cardWidth, cardHeight;
        if (maxCardWidth / maxCardHeight > aspectRatio) {
            // Height is the limiting factor
            cardHeight = maxCardHeight;
            cardWidth = cardHeight / aspectRatio;
        } else {
            // Width is the limiting factor
            cardWidth = maxCardWidth;
            cardHeight = cardWidth * aspectRatio;
        }

        // Set padding on grid pane to center it
        double leftRightPadding = (sceneWidth - (cols * cardWidth) - ((cols - 1) * spacing)) / 2;
        double topBottomPadding = (availableHeight - (rows * cardHeight) - ((rows - 1) * spacing)) / 2;
        gridPane.setPadding(new Insets(
                topBottomPadding + verticalMargin,
                leftRightPadding,
                topBottomPadding,
                leftRightPadding
        ));

        // Update all card sizes
        cardViews.forEach((card, imageView) -> {
            imageView.setFitWidth(cardWidth);
            imageView.setFitHeight(cardHeight);

            // Update parent button size
            Button button = (Button) imageView.getParent();
            if (button != null) {
                button.setPrefSize(cardWidth, cardHeight);
            }
        });
    }

    private Button createCardButton(Card card, double cardWidth, double cardHeight) {
        Button cardButton = new Button();
        cardButton.setPrefSize(cardWidth, cardHeight);
        cardButton.setPadding(Insets.EMPTY);
        cardButton.setMinSize(10, 10); // Minimum size for responsiveness

        ImageView imageView = new ImageView(new Image(Card.getBackImagePath()));
        imageView.setFitWidth(cardWidth);
        imageView.setFitHeight(cardHeight);
        imageView.setPreserveRatio(true);
        cardButton.setGraphic(imageView);

        cardButton.setOnAction(event -> handleCardFlip(card, imageView));
        return cardButton;
    }

    private void handleCardFlip(Card card, ImageView imageView) {
        if (card.isMatched() || card.isFaceUp() || secondFlippedCard != null || !game.isActive()) return;

        // Flip animation
        ScaleTransition flipOut = new ScaleTransition(Duration.millis(200), imageView);
        flipOut.setFromX(1.0);
        flipOut.setToX(0.0); // Shrink horizontally
        flipOut.setOnFinished(event -> {
            card.flip(); // Flip the card's state in the model
            updateCardImage(card, imageView); // Update the image to show the other side

            ScaleTransition flipIn = new ScaleTransition(Duration.millis(200), imageView);
            flipIn.setFromX(0.0);
            flipIn.setToX(1.0); // Expand back to full size
            flipIn.play();
        });
        flipOut.play();

        // Game logic for handling card selection
        if (firstFlippedCard == null) {
            firstFlippedCard = card;
        } else {
            secondFlippedCard = card;
            // Let animation finish before processing the turn
            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(event -> {
                boolean isMatch = game.processTurn(firstFlippedCard, secondFlippedCard);
                checkForMatch(isMatch);
            });
            pause.play();
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
            setChanged();
            notifyObservers("GAME_OVER");
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

    public void resetCards() {
        // Reset UI state
        for (Card card : cardViews.keySet()) {
            updateCardImage(card, cardViews.get(card));
        }
        resetSelections();
    }
}

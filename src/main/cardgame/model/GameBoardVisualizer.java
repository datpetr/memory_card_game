package main.cardgame.model;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.List;
import main.cardgame.model.Deck;

public class GameBoardVisualizer extends Application {
    private static final int BOARD_ROWS = 4;
    private static final int BOARD_COLS = 4;
    private static final double CARD_ASPECT_RATIO = 2.0 / 3.0; // Standard playing card ratio
    private static final double PADDING = 20; // Padding around the grid
    private static final double GAP = 10; // Gap between cards

    private Card firstFlippedCard = null;
    private Card secondFlippedCard = null;

    @Override
    public void start(Stage primaryStage) {
        // Get screen dimensions (with some margin)
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;

        // Calculate maximum possible card dimensions
        double maxCardWidth = (maxWidth - 2 * PADDING - (BOARD_COLS - 1) * GAP) / BOARD_COLS;
        double maxCardHeight = (maxHeight - 2 * PADDING - (BOARD_ROWS - 1) * GAP) / BOARD_ROWS;

        // Adjust to maintain aspect ratio
        double cardWidth = Math.min(maxCardWidth, maxCardHeight * CARD_ASPECT_RATIO);
        double cardHeight = cardWidth / CARD_ASPECT_RATIO;

        // Calculate total grid size
        double gridWidth = (cardWidth * BOARD_COLS) + (GAP * (BOARD_COLS - 1)) + 2 * PADDING;
        double gridHeight = (cardHeight * BOARD_ROWS) + (GAP * (BOARD_ROWS - 1)) + 2 * PADDING;

        GridPane gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(PADDING));

        Deck deck = new Deck();
        deck = deck.createDeck((BOARD_ROWS * BOARD_COLS) / 2);
        List<Card> cards = deck.getCards();

        int cardIndex = 0;
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Card card = cards.get(cardIndex++);
                Button cardButton = createCardButton(card, cardWidth, cardHeight);
                gridPane.add(cardButton, col, row);
            }
        }

        Scene scene = new Scene(gridPane, gridWidth, gridHeight);
        primaryStage.setTitle("Memory Card Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createCardButton(Card card, double cardWidth, double cardHeight) {
        Button cardButton = new Button();
        cardButton.setPrefSize(cardWidth, cardHeight);
        cardButton.setPadding(Insets.EMPTY);

        String backImagePath = Card.getBackImagePath();
        if (backImagePath == null) {
            throw new IllegalStateException("Back image path is not set.");
        }

        ImageView imageView = new ImageView(new Image(backImagePath));
        imageView.setFitWidth(cardWidth);
        imageView.setFitHeight(cardHeight);
        cardButton.setGraphic(imageView);

        cardButton.setOnAction(event -> handleCardFlip(card, imageView, backImagePath));

        return cardButton;
    }

    private void handleCardFlip(Card card, ImageView imageView, String backImagePath) {
        if (card.isFaceUp() || secondFlippedCard != null) {
            return;
        }

        card.flip();
        String faceImagePath = card.isFaceUp() ? card.getImagePath() : backImagePath;
        if (faceImagePath == null) {
            throw new IllegalStateException("Card face image path is null.");
        }
        imageView.setImage(new Image(faceImagePath));

        if (firstFlippedCard == null) {
            firstFlippedCard = card;
        } else {
            secondFlippedCard = card;
            checkForMatch();
        }
    }

    private void checkForMatch() {
        if (firstFlippedCard.getImagePath().equals(secondFlippedCard.getImagePath())) {
            firstFlippedCard.setMatched(true);
            secondFlippedCard.setMatched(true);
        } else {
            Card tempFirst = firstFlippedCard;
            Card tempSecond = secondFlippedCard;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    tempFirst.flip();
                    tempSecond.flip();
                });
            }).start();
        }

        firstFlippedCard = null;
        secondFlippedCard = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
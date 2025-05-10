package main.cardgame.model;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.cardgame.game.EndlessGame;
import main.cardgame.model.Card;
import main.cardgame.model.Deck;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoardVisualizer extends Application {
    private static final int BOARD_ROWS = 4;
    private static final int BOARD_COLS = 4;
    private static final double CARD_ASPECT_RATIO = 2.0 / 3.0;
    private static final double PADDING = 20;
    private static final double GAP = 10;

    private EndlessGame game;
    private GameBoard board;
    private Card firstFlippedCard;
    private Card secondFlippedCard;
    private final Map<Card, ImageView> cardViews = new HashMap<>();
    private GridPane gridPane;

    @Override
    public void start(Stage primaryStage) {
        initializeGame();
        setupUI(primaryStage);
    }

    private void initializeGame() {
        int totalCardsNeeded = BOARD_ROWS * BOARD_COLS;
        int requiredPairs = totalCardsNeeded / 2;

        Deck deck = new Deck().createDeck(requiredPairs);
        List<Card> cards = deck.getCards();

        if (cards.size() != totalCardsNeeded) {
            throw new IllegalStateException(
                    "Deck created " + cards.size() + " cards but need " + totalCardsNeeded
            );
        }

        this.board = new GameBoard("easy", cards);
        Player player = new Player("Player1");
        this.game = new EndlessGame(board, player);
    }

    private void setupUI(Stage primaryStage) {
        Screen screen = Screen.getPrimary();
        double maxWidth = screen.getVisualBounds().getWidth() * 0.9;
        double maxHeight = screen.getVisualBounds().getHeight() * 0.9;

        double maxCardWidth = (maxWidth - 2 * PADDING - (BOARD_COLS - 1) * GAP) / BOARD_COLS;
        double maxCardHeight = (maxHeight - 2 * PADDING - (BOARD_ROWS - 1) * GAP) / BOARD_ROWS;

        double cardWidth = Math.min(maxCardWidth, maxCardHeight * CARD_ASPECT_RATIO);
        double cardHeight = cardWidth / CARD_ASPECT_RATIO;

        gridPane = new GridPane();
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        gridPane.setPadding(new Insets(PADDING));

        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Card card = board.getCard(row, col);

                if (card == null) { // Add null check
                    throw new IllegalStateException(
                            "No card found at position [" + row + "][" + col + "]"
                    );
                }

                Button cardButton = createCardButton(card, cardWidth, cardHeight);
                gridPane.add(cardButton, col, row);
                cardViews.put(card, (ImageView) cardButton.getGraphic());
            }
        }

        Scene scene = new Scene(gridPane);
        primaryStage.setTitle("Memory Card Game - Endless Mode");
        primaryStage.setScene(scene);
        primaryStage.show();

        game.play();
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
        if (card.isMatched() || card.isFaceUp() || secondFlippedCard != null) return;

        card.flip();
        updateCardImage(card, imageView);

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
            showWinMessage();
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

    private void showWinMessage() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Won!");
            alert.setHeaderText("Congratulations!");
            alert.setContentText(
                    "All matches found!\n" +
                            "Score: " + game.getPlayer().getScore() + "\n" +
                            "Time: " + game.getTimer().getElapsedTime() + " seconds"
            );
            alert.showAndWait();
        });
    }

    public static void main(String[] args) {
        Card.setBackImagePath("file:src/main/resources/images/back2.jpg");
        launch(args);
    }
}
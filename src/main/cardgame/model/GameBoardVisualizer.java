package main.cardgame.model;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class GameBoardVisualizer extends Application {
    private static final int BOARD_ROWS = 4;
    private static final int BOARD_COLS = 4;
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 150;

    private Card firstFlippedCard = null;
    private Card secondFlippedCard = null;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        Deck deck = new Deck((BOARD_ROWS * BOARD_COLS) / 2);
        deck.shuffle();
        List<Card> cards = deck.getCards();

        int cardIndex = 0;
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Card card = cards.get(cardIndex++);
                Button cardButton = createCardButton(card);
                gridPane.add(cardButton, col, row);
            }
        }

        Scene scene = new Scene(gridPane, 500, 600);
        primaryStage.setTitle("GameBoard Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createCardButton(Card card) {
        Button cardButton = new Button();
        cardButton.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        cardButton.setPadding(Insets.EMPTY);

        ImageView imageView = new ImageView(new Image(Card.getBackImagePath()));
        imageView.setFitWidth(CARD_WIDTH);
        imageView.setFitHeight(CARD_HEIGHT);
        cardButton.setGraphic(imageView);

        cardButton.setOnAction(event -> {
            if (card.isFaceUp() || secondFlippedCard != null) {
                return;
            }

            card.flip();
            imageView.setImage(new Image(card.isFaceUp() ? card.getValue() : Card.getBackImagePath()));

            if (firstFlippedCard == null) {
                firstFlippedCard = card;
            } else {
                secondFlippedCard = card;
                checkForMatch();
            }
        });

        return cardButton;
    }

    private void checkForMatch() {
        if (firstFlippedCard.getId() == secondFlippedCard.getId()) {
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
                tempFirst.flip();
                tempSecond.flip();
            }).start();
        }

        firstFlippedCard = null;
        secondFlippedCard = null;
    }

    public static void main(String[] args) {
        Card.setBackImagePath("file:src/main/resources/images/back2.jpg");
        launch(args);
    }
}

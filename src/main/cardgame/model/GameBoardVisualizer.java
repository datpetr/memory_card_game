package main.cardgame.model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameBoardVisualizer extends Application {
    private static final int BOARD_ROWS = 4;
    private static final int BOARD_COLS = 4;
    private static final int CARD_SIZE = 100;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Button cardButton = new Button();
                cardButton.setPrefSize(CARD_SIZE, CARD_SIZE);

                // Corrected image path for the face-down card
                Image faceDownImage = new Image("file:src/main/resources/images/back.png");
                ImageView imageView = new ImageView(faceDownImage);
                imageView.setFitWidth(CARD_SIZE);
                imageView.setFitHeight(CARD_SIZE);
                cardButton.setGraphic(imageView);

                gridPane.add(cardButton, col, row);

                // Corrected image path for the face-up card
                cardButton.setOnAction(event -> {
                    Image faceUpImage = new Image("file:src/main/resources/images/try.png");
                    imageView.setImage(faceUpImage);
                });
            }
        }

        Scene scene = new Scene(gridPane, 500, 500);
        primaryStage.setTitle("GameBoard Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
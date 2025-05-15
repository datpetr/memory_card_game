package main.cardgame.ui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import main.cardgame.game.Game;
import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CardUI implements Observer {
    private GridPane cardGrid;
    private GameBoard gameBoard;
    private Game game;
    private List<ImageView> cardViews;
    private int rows;
    private int cols;
    private Card firstSelectedCard;
    private int firstSelectedRow;
    private int firstSelectedCol;
    private boolean processingMatch = false;

    public CardUI(Game game, GameBoard gameBoard, int rows, int cols) {
        this.game = game;
        this.gameBoard = gameBoard;
        this.rows = rows;
        this.cols = cols;
        this.cardViews = new ArrayList<>();
        
        initializeUI();
    }

    private void initializeUI() {
        cardGrid = new GridPane();
        cardGrid.setAlignment(Pos.CENTER);
        cardGrid.setHgap(10);
        cardGrid.setVgap(10);
        
        // Create all card views
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = gameBoard.getCard(row, col);
                if (card == null) continue;
                
                // Make sure card is observed by this UI
                card.addObserver(this);
                
                // Create ImageView for the card
                ImageView cardView = new ImageView();
                cardView.setPreserveRatio(true);
                
                // Adjust initial card size based on board dimensions
                double initialSize = calculateCardSize();
                cardView.setFitWidth(initialSize);
                cardView.setFitHeight(initialSize);
                
                // Set the initial image (back of card)
                updateCardImage(cardView, card);
                
                // Create a stack pane to hold the card view
                StackPane cardContainer = new StackPane(cardView);
                
                // Store position as user data for easy retrieval
                cardContainer.setUserData(new int[]{row, col});
                
                // Add click handler
                final int finalRow = row;
                final int finalCol = col;
                cardContainer.setOnMouseClicked(event -> handleCardClick(finalRow, finalCol));
                
                // Add to grid
                cardGrid.add(cardContainer, col, row);
                cardViews.add(cardView);
            }
        }
    }
    
    private double calculateCardSize() {
        // Calculate appropriate card size based on board dimensions
        // Larger boards will have smaller cards
        double baseSize = 100;
        double scaleFactor = Math.max(1, Math.sqrt(rows * cols) / 8);
        return baseSize / scaleFactor;
    }
    
    private void handleCardClick(int row, int col) {
        if (processingMatch) return;
        
        Card card = gameBoard.getCard(row, col);
        if (card == null || card.isFaceUp() || card.isMatched()) return;
        
        // Flip the card in the model
        gameBoard.flipCard(row, col);
        
        if (firstSelectedCard == null) {
            // First card selected
            firstSelectedCard = card;
            firstSelectedRow = row;
            firstSelectedCol = col;
        } else {
            // Second card selected - save references locally for use in runLater
            final Card savedFirstCard = firstSelectedCard;
            final int savedFirstRow = firstSelectedRow;
            final int savedFirstCol = firstSelectedCol;
            processingMatch = true;
            
            // Process the turn in the game
            boolean isMatch = game.processTurn(savedFirstCard, card);
            
            // If not a match, schedule the cards to be flipped back
            if (!isMatch) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000); // Delay before flipping back
                        javafx.application.Platform.runLater(() -> {
                            // Use the saved references to avoid null pointer
                            if (savedFirstCard != null && !savedFirstCard.isMatched()) {
                                gameBoard.flipCard(savedFirstRow, savedFirstCol);
                            }
                            if (card != null && !card.isMatched()) {
                                gameBoard.flipCard(row, col);
                            }
                            processingMatch = false;
                        });
                    } catch (InterruptedException e) {
                        javafx.application.Platform.runLater(() -> processingMatch = false);
                    }
                }).start();
            } else {
                processingMatch = false;
            }
            
            // Reset first selection
            firstSelectedCard = null;
        }
    }
    
    private void updateCardImage(ImageView cardView, Card card) {
        try {
            // Get the appropriate image based on card state
            String imagePath = card.isFaceUp() ? card.getImagePath() : Card.getBackImagePath();
            
            // Ensure path exists - use empty string check since JavaFX will throw error otherwise
            if (imagePath == null || imagePath.trim().isEmpty()) {
                System.err.println("Warning: Invalid image path for card");
                return;
            }
            
            // Load and set the image
            Image image = new Image(imagePath, true); // true = load in background
            cardView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading card image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateCardSizes(double width, double height) {
        // Calculate appropriate card size based on available space
        // Add some padding to ensure the bottom controls are visible
        double availableHeight = height * 0.8; // Use 80% of height to leave room for controls
        double cardSize = Math.min((width - 20) / cols, (availableHeight - 20) / rows) - 10;
        
        // Apply size to card views
        for (ImageView cardView : cardViews) {
            cardView.setFitWidth(cardSize);
            cardView.setFitHeight(cardSize);
        }
    }
    
    public void resetCards() {
        firstSelectedCard = null;
        processingMatch = false;
        
        // Reset all card images to show backs
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Card card = gameBoard.getCard(row, col);
                if (card != null) {
                    int index = row * cols + col;
                    if (index < cardViews.size()) {
                        updateCardImage(cardViews.get(index), card);
                    }
                }
            }
        }
    }
    
    public GridPane getComponent() {
        return cardGrid;
    }
    
    @Override
    public void update(Observable observable, Object arg) {
        if (!(observable instanceof Card)) return;
        
        Card card = (Card) observable;
        
        // Find which index this card is at
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (gameBoard.getCard(row, col) == card) {
                    int index = row * cols + col;
                    if (index < cardViews.size()) {
                        // Update the card view on the JavaFX thread
                        javafx.application.Platform.runLater(() -> 
                            updateCardImage(cardViews.get(index), card)
                        );
                    }
                    break;
                }
            }
        }
    }
}

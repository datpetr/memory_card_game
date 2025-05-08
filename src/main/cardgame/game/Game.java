package main.cardgame.game;

import main.cardgame.difficulty.DifficultyLevel;
import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.util.Logger;
import main.cardgame.util.ScoreManager;
import main.cardgame.util.Timer;

public abstract class Game {
    private GameBoard board;
    private Player player;
    private ScoreManager scoreManager;
    protected Timer timer;
    private Card firstSelection;
    private Card secondSelection;
    private DifficultyLevel difficultyLevel; // New field for difficulty

    public Game(GameBoard board, Player player, DifficultyLevel difficultyLevel) {
        if (board == null || player == null || difficultyLevel == null) {
            throw new IllegalArgumentException("GameBoard, Player, and DifficultyLevel cannot be null.");
        }
        this.board = board;
        this.player = player;
        this.difficultyLevel = difficultyLevel; // Assign difficulty level
        this.scoreManager = new ScoreManager(player);
        this.timer = new Timer();

        // Configure game settings based on difficulty
        configureDifficulty();
    }

    // Configure game settings based on the difficulty level
    private void configureDifficulty() {
        board.setGridSize(difficultyLevel.getGridSize());
        timer.setTimeLimit(difficultyLevel.getTimeLimit());
        Logger.log("Game configured for difficulty: " + difficultyLevel.getClass().getSimpleName());
    }

    public void play() {
        // Main game loop logic
    }

    public void processTurn(int row1, int col1, int row2, int col2) {
        Card firstCard = getBoard().getCard(row1, col1);
        Card secondCard = getBoard().getCard(row2, col2);

        if (firstCard != secondCard) {
            firstCard.flip();
            secondCard.flip();

            if (firstCard.getValue().equals(secondCard.getValue())) {
                firstCard.setMatched(true);
                secondCard.setMatched(true);
            } else {
                firstCard.flip();
                secondCard.flip();
            }
        }
    }

    public void pickCard(int row, int col) {
        getBoard().flipCard(row, col); // Flip the card at the specified position
    }

    public abstract boolean isGameOver();

    // Getters
    public GameBoard getBoard() {
        return board;
    }

    public Player getPlayer() {
        return player;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public Timer getTimer() {
        return timer;
    }

    public Card getFirstSelection() {
        return firstSelection;
    }

    public Card getSecondSelection() {
        return secondSelection;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    // Setters
    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setFirstSelection(Card firstSelection) {
        this.firstSelection = firstSelection;
    }

    public void setSecondSelection(Card secondSelection) {
        this.secondSelection = secondSelection;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        if (difficultyLevel == null) {
            throw new IllegalArgumentException("DifficultyLevel cannot be null.");
        }
        this.difficultyLevel = difficultyLevel;
        configureDifficulty(); // Reconfigure game settings
    }
}
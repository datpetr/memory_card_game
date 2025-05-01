package main.cardgame.game;

import main.cardgame.model.Card;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.util.Logger;
import main.cardgame.util.ScoreManager;
import main.cardgame.util.Timer;

public class Game {
    private GameBoard board;
    private Player player;
    private ScoreManager scoreManager;
    private Timer timer;
    private Card firstSelection;
    private Card secondSelection;

    public Game(int boardSize, String playerName) {
        // Initialize game components
    }

    public void play() {
        // Main game loop logic
    }

    public void processTurn(int row1, int col1, int row2, int col2) {
        // Process a player's turn when they select two cards
    }

    public boolean isGameOver() {
        // Check if the game is over
        return false;
    }
}
package main.cardgame.controller;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.game.Game;

public class GameController {
    private Game game;

    public GameController(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }
        this.game = game;
    }

    public void startGame() {
        game.getTimer().startTimer();
        game.play();
        System.out.println("Game started!");
    }

    public void selectCard(int row, int col) {
        if (game.isGameOver()) {
            System.out.println("Game is already over!");
            return;
        }

        game.pickCard(row, col);
        game.getPlayer().incrementMoves();

        if (game.getTimer().isCountdown() && game.getTimer().isTimeUp()) {
            endGame();
        }

        if (game.getBoard().allCardsMatched()) {
            endGame();
        }
    }

    public void endGame() {
        game.getTimer().stopTimer();
        System.out.println("Game over! Final score: " + game.getPlayer().getScore());
    }
}
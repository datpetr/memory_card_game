package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;

public class EndlessGame extends Game {
    private boolean isStopped;

    public EndlessGame(GameBoard board, Player player) {
        super(board, player); // Pass GameBoard and Player to the Game constructor
        this.isStopped = false;
    }

    public void stopGame() {
        this.isStopped = true;
    }

    @Override
    public boolean isGameOver() {
        if (isStopped) {
            return true;
        }
        if (getBoard().allCardsMatched()) {
            return true;
        }
        return false;
    }
}
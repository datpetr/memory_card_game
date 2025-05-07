package main.cardgame.game;

public class EndlessGame extends Game {
    private boolean isStopped;

    public EndlessGame(int boardSize, String playerName) {
        super(boardSize, playerName);
        this.isStopped = false;
    }

    public EndlessGame(String playerName, int boardSize){
        super(boardSize, playerName);
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
package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.model.Card;

public class EndlessGame extends Game {
    public EndlessGame(GameBoard board, Player player) {
        super(board, player); // Pass GameBoard and Player to the Game constructor
    }


    @Override
    public boolean isGameOver() {
        return !isActive() || getBoard().allCardsMatched(); // Game ends if stopped by the player or all cards are matched
    }

    // Add bonus points based on time and efficiency
    @Override
    public void endGame() {
        // Calculate bonus before calling super.endGame() which sets isActive to false
        int timeElapsed = (int) getTimer().getElapsedTime() / 1000;
        int movesBonus = Math.max(100 - getPlayer().getMoves(), 0);

        // Call parent method which handles notifications and state of chage
        super.endGame();

        // Apply the bonus after calling super
        getPlayer().incrementScore(movesBonus);

        // Notify observers about bonus points
        setChanged();
        notifyObservers("BONUS_POINTS_ADDED");

    }

    //
    @Override
    public boolean processTurn(Card card1, Card card2) {
        boolean result = super.processTurn(card1, card2);

        if (result) {
            // Calculate dynamic bonus based on current game state
            int moveCount = getPlayer().getMoves();
            int matchedPairs = getBoard().getMatchedPairsCount();

            // Base bonus points
            int baseBonus = 5;

            // Add efficiency bonus (fewer moves = higher bonus)
            int efficiencyBonus = Math.max(10 - (moveCount / (matchedPairs + 1)), 1);

            // Total bonus is base plus efficiency
            int totalBonus = baseBonus + efficiencyBonus;

            getPlayer().incrementScore(totalBonus);

            setChanged();
            notifyObservers("MATCH_BONUS_ADDED");
        }

        return result;
    }

}
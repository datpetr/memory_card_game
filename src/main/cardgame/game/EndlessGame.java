package main.cardgame.game;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.model.Card;

/**
 * Implementation of an endless game mode without time constraints.
 * Players can take as much time as needed to find all matches.
 * Includes efficiency-based bonus points for completing with fewer moves.
 */
public class EndlessGame extends Game {
    /**
     * Creates a new endless game
     * @param board The game board
     * @param player The player
     */
    public EndlessGame(GameBoard board, Player player) {
        super(board, player); // Pass GameBoard and Player to the Game constructor
    }

    /**
     * Determines if the game is over based on:
     * 1. Game is no longer active
     * 2. All cards are matched
     * @return True if the game is over, false otherwise
     */
    @Override
    public boolean isGameOver() {
        return !isActive() || getBoard().allCardsMatched(); // Game ends if stopped by the player or all cards are matched
    }

    /**
     * Ends the game and applies move efficiency bonus points.
     * Fewer moves result in a higher bonus.
     */
    @Override
    public void endGame() {
        // Only calculate and add bonus if the game is still active
        if (isActive()) {
            // Calculate bonus before calling super.endGame() which sets isActive to false
            int timeElapsed = (int) getTimer().getElapsedTime() / 1000;
            int movesBonus = Math.max(100 - getPlayer().getMoves(), 0);

            // Apply the bonus BEFORE calling super.endGame()
            getPlayer().incrementScore(movesBonus);

            // Notify observers about bonus points
            setChanged();
            notifyObservers("BONUS_POINTS_ADDED");
        }

        // Call parent method which handles notifications and state changes
        super.endGame();
    }

    /**
     * Processes a turn when two cards are selected, adds efficiency-based
     * bonus points for matches (more points for fewer moves)
     * @param card1 The first selected card
     * @param card2 The second selected card
     * @return True if the cards match, false otherwise
     */
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


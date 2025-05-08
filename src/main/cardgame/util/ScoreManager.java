package main.cardgame.util;

import main.cardgame.model.Player;
import main.cardgame.util.Timer;

public class ScoreManager {
    private Player player;

    public ScoreManager(Player player) {
        this.player = player;
    }

    public void calculateAndUpdateScore(boolean isMatch, Timer timer) {
        int points = isMatch ? 10 : 0; // Base points for a match
        if (timer != null) {
            points += (int) (timer.getRemainingTime() / 1000); // Add time bonus if applicable
        }
        player.incrementScore(points);
    }

    public void resetScore() {
        player.setScore(0);
    }

    public int getScore() {
        return player.getScore();
    }
}
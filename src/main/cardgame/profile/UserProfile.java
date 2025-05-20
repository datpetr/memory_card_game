package main.cardgame.profile;

import java.io.Serializable;
import main.cardgame.stats.GameStatistics;

public class UserProfile implements Serializable {
    private String username;
    private GameStatistics statistics;

    public UserProfile(String username) {
        this.username = username;
        this.statistics = new GameStatistics();
    }

    public String getUsername() {
        return username;
    }

    public GameStatistics getStatistics() {
        return statistics;
    }

    public void updateStatistics(GameStatistics newStats) {
        // You can customize merging here
        this.statistics = newStats;
    }

    @Override
    public String toString() {
        return username;
    }
}

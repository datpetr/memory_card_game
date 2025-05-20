package main.cardgame.profile;

import java.io.Serializable;
import main.cardgame.stats.GameStatistics;

/**
 * Represents a user profile in the memory card game.
 * Stores user information and game statistics.
 * Implements Serializable to support saving/loading profiles.
 */
public class UserProfile implements Serializable {
    private String username;
    private GameStatistics statistics;

    /**
     * Creates a new user profile with the specified username.
     * Initializes empty game statistics.
     *
     * @param username The name of the user
     */
    public UserProfile(String username) {
        this.username = username;
        this.statistics = new GameStatistics();
    }

    /**
     * Gets the username of the profile.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the game statistics associated with this profile.
     *
     * @return The user's game statistics
     */
    public GameStatistics getStatistics() {
        return statistics;
    }

    /**
     * Updates the user's statistics with new data.
     *
     * @param newStats The new statistics to replace the current ones
     */
    public void updateStatistics(GameStatistics newStats) {
        // You can customize merging here
        this.statistics = newStats;
    }

    /**
     * Returns a string representation of the user profile.
     *
     * @return The username
     */
    @Override
    public String toString() {
        return username;
    }
}

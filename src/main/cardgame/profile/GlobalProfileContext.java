package main.cardgame.profile;

/**
 * Manages the application-wide active user profile.
 * Provides a central point of access to the currently selected user profile.
 */
public class GlobalProfileContext {
    private static UserProfile activeProfile;

    /**
     * Sets the active user profile for the application.
     *
     * @param profile The user profile to set as active
     */
    public static void setActiveProfile(UserProfile profile) {
        activeProfile = profile;
    }

    /**
     * Gets the currently active user profile.
     *
     * @return The active user profile or null if none is set
     */
    public static UserProfile getActiveProfile() {
        return activeProfile;
    }
}


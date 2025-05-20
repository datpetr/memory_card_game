package main.cardgame.profile;

public class GlobalProfileContext {
    private static UserProfile activeProfile;

    public static void setActiveProfile(UserProfile profile) {
        activeProfile = profile;
    }

    public static UserProfile getActiveProfile() {
        return activeProfile;
    }
}


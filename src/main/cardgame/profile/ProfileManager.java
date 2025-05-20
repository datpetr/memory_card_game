package main.cardgame.profile;

import java.io.*;
import java.util.*;

/**
 * Manages the persistence of user profiles.
 * Handles saving, loading, and listing user profiles from the file system.
 */
public class ProfileManager {
    private static final String PROFILE_DIR = "profiles/";
    //private static final String PROFILE_DIR = System.getProperty("user.home") + File.separator + ".memo_profiles" + File.separator;
    private static final String EXT = ".profile";

    static {
        new File(PROFILE_DIR).mkdirs();
    }

    /**
     * Saves a user profile to the file system.
     * 
     * @param profile The user profile to save
     * @throws IOException If an I/O error occurs during saving
     */
    public static void saveProfile(UserProfile profile) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(PROFILE_DIR + profile.getUsername() + EXT))) {
            out.writeObject(profile);
        }
    }

    /**
     * Loads a user profile from the file system.
     * 
     * @param username The name of the profile to load
     * @return The loaded user profile
     * @throws IOException If an I/O error occurs during loading
     * @throws ClassNotFoundException If the class of the serialized object cannot be found
     */
    public static UserProfile loadProfile(String username) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(PROFILE_DIR + username + EXT))) {
            return (UserProfile) in.readObject();
        }
    }

    /**
     * Deletes a user profile from the file system.
     * 
     * @param username The name of the profile to delete
     */
    public static void deleteProfile(String username) {
        File file = new File(PROFILE_DIR + username + EXT);
        if (file.exists()) file.delete();
    }

    /**
     * Lists all available user profiles.
     * 
     * @return A list of profile names without file extensions
     */
    public static List<String> listProfiles() {
        File dir = new File(PROFILE_DIR);
        String[] names = dir.list((d, name) -> name.endsWith(EXT));
        List<String> result = new ArrayList<>();
        if (names != null) {
            for (String name : names) {
                result.add(name.replace(EXT, ""));
            }
        }
        return result;
    }
}


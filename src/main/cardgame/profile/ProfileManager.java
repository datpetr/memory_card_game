package main.cardgame.profile;

import java.io.*;
import java.util.*;

public class ProfileManager {
    private static final String PROFILE_DIR = "profiles/";
    //private static final String PROFILE_DIR = System.getProperty("user.home") + File.separator + ".memo_profiles" + File.separator;
    private static final String EXT = ".profile";

    static {
        new File(PROFILE_DIR).mkdirs();
    }

    public static void saveProfile(UserProfile profile) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(PROFILE_DIR + profile.getUsername() + EXT))) {
            out.writeObject(profile);
        }
    }

    public static UserProfile loadProfile(String username) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(PROFILE_DIR + username + EXT))) {
            return (UserProfile) in.readObject();
        }
    }

    public static void deleteProfile(String username) {
        File file = new File(PROFILE_DIR + username + EXT);
        if (file.exists()) file.delete();
    }

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


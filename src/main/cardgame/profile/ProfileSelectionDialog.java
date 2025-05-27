package main.cardgame.profile;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ProfileSelectionDialog extends Stage {
    private UserProfile selectedProfile;

    public ProfileSelectionDialog(Stage owner, Runnable onProfileSelected) {
        setTitle("Select Profile");
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);

        ComboBox<String> profileComboBox = new ComboBox<>();
        profileComboBox.getItems().addAll(ProfileManager.listProfiles());

        TextField newProfileField = new TextField();
        newProfileField.setPromptText("New profile name");

        Button createButton = new Button("Create");
        Button deleteButton = new Button("Delete");
        Button continueButton = new Button("Continue");

        Label statusLabel = new Label();

        createButton.setOnAction(e -> {
            String name = newProfileField.getText().trim();

            try {
                // Validate name
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Error: Name cannot be empty.");
                }

                if (name.length() < 3 || name.length() > 12) {
                    throw new IllegalArgumentException("Error: Name must be between \n\t    3-12 characters long.");
                }

                // Validate characters (only Latin letters, numbers, '_' and '-')
                if (!name.matches("^[a-zA-Z0-9_-]+$")) {
                    throw new IllegalArgumentException("Error: Name can only contain \n\t Latin letters, numbers, '_' and '-'.");
                }

                // Check if profile already exists
                if (profileComboBox.getItems().contains(name)) {
                    throw new IllegalArgumentException("Error: This profile name already exists.");
                }

                // All validations passed
            } catch (IllegalArgumentException ex) {
                statusLabel.setText(ex.getMessage());
                return;
            }

            // If all validations pass, create the profile
            UserProfile newProfile = new UserProfile(name);
            try {
                ProfileManager.saveProfile(newProfile);
                profileComboBox.getItems().add(name);
                profileComboBox.setValue(name);
                statusLabel.setText("Profile created successfully.");
                newProfileField.clear();
            } catch (IOException ex) {
                statusLabel.setText("Error: Failed to save profile.");
                ex.printStackTrace();
            }
        });

        deleteButton.setOnAction(e -> {
            String name = profileComboBox.getValue();
            if (name != null) {
                ProfileManager.deleteProfile(name);
                profileComboBox.getItems().remove(name);
                statusLabel.setText("Profile deleted.");
            }
        });

        continueButton.setOnAction(e -> {
            String name = profileComboBox.getValue();
            if (name != null) {
                try {
                    selectedProfile = ProfileManager.loadProfile(name);
                    GlobalProfileContext.setActiveProfile(selectedProfile);
                    statusLabel.setText("Profile loaded: " + name);
                    close();
                    onProfileSelected.run();
                } catch (IOException | ClassNotFoundException ex) {
                    statusLabel.setText("Error loading profile.");
                    ex.printStackTrace();
                }
            } else {
                statusLabel.setText("Please choose a profile first.");
            }
        });

        VBox layout = new VBox(10,
                new Label("Select existing profile:"),
                profileComboBox,
                new Label("Or create new profile:"),
                newProfileField,
                createButton,
                deleteButton,
                continueButton,
                statusLabel);
        layout.setPadding(new Insets(15));
        Scene scene = new Scene(layout, 300, 300);
        setScene(scene);
    }

    public UserProfile getSelectedProfile() {
        return selectedProfile;
    }
}


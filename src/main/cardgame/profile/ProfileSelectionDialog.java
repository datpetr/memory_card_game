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
            if (!name.isEmpty() && !profileComboBox.getItems().contains(name)) {
                UserProfile newProfile = new UserProfile(name);
                try {
                    ProfileManager.saveProfile(newProfile);
                    profileComboBox.getItems().add(name);
                    profileComboBox.setValue(name);
                    statusLabel.setText("Profile created.");
                } catch (IOException ex) {
                    statusLabel.setText("Error saving profile.");
                    ex.printStackTrace();
                }
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
package main.cardgame.ui;

import javafx.scene.control.Button;

/**
 * Utility class for managing button effects
 */
public class ButtonEffectManager {
    /**
     * Adds hover and click effects to a button
     * Original implementation from GameBoardVisualizer2
     * @param button The button to add effects to
     */
    public static void addButtonHoverEffect(Button button) {
        // Store the original style
        final String originalStyle = button.getStyle();

        // Hover effect (preserved from original)
        button.setOnMouseEntered(e -> button.setStyle(originalStyle + "; -fx-effect: dropshadow(gaussian, #4682b4, 12, 0.5, 0, 0); -fx-translate-y: -2;"));
        button.setOnMouseExited(e -> button.setStyle(originalStyle));

        // Click effect (preserved from original)
        button.setOnMousePressed(e -> button.setStyle(originalStyle + "; -fx-background-color: #2e8b57; -fx-translate-y: 2;"));
        button.setOnMouseReleased(e -> button.setStyle(originalStyle + "; -fx-effect: dropshadow(gaussian, #4682b4, 12, 0.5, 0, 0); -fx-translate-y: -2;"));
    }
}

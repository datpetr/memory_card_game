package main.cardgame.ui;

import javafx.scene.layout.GridPane;
import main.cardgame.model.GameBoard;

/**
 * Manages the game board grid
 * Preserves the original grid layout from GameBoardVisualizer2
 */
public class BoardCanvas {
    private GameBoard board;
    private GridPane gridPane;

    public BoardCanvas(GameBoard board, GridPane gridPane) {
        this.board = board;
        this.gridPane = gridPane;
    }

    /**
     * Gets the grid pane containing the game board
     * @return The grid pane
     */
    public GridPane getGridPane() {
        return gridPane;
    }
}

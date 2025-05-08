package main.cardgame.controller;

import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;
import main.cardgame.game.Game;

import java.util.Scanner;

public class MenuController {
    private Scanner scanner;

    public MenuController() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("==== Memory Card Game ====");
        System.out.println("1. Start New Game");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");
    }

    public void handleMenuSelection() {
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                startNewGame();
                break;
            case 2:
                System.out.println("Exiting the game. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void startNewGame() {
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine();
        Player player = new Player(playerName);

        System.out.print("Enter difficulty (easy, medium, hard): ");
        String difficulty = scanner.nextLine();

        GameBoard board = new GameBoard(difficulty, null);
        Game game = new Game(board, player, determineCountdown(difficulty)) {
            @Override
            public boolean isGameOver() {
                return getBoard().allCardsMatched();
            }
        };

        GameController gameController = new GameController(game);
        gameController.startGame();
    }

    private int determineCountdown(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return 300; // 5 minutes
            case "medium":
                return 180; // 3 minutes
            case "hard":
                return 120; // 2 minutes
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }
}
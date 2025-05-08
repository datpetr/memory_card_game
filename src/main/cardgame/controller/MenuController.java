package main.cardgame.controller;

import main.cardgame.game.EndlessGame;
import main.cardgame.game.TimedGame;
import main.cardgame.model.GameBoard;
import main.cardgame.model.Player;

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
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                selectGameMode();
                break;
            case 2:
                System.out.println("Exiting the game. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void selectGameMode() {
        System.out.println("Select Game Mode:");
        System.out.println("1. Endless Game");
        System.out.println("2. Timed Game");
        System.out.print("Enter your choice: ");
        int modeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (modeChoice) {
            case 1:
                startEndlessGame();
                break;
            case 2:
                startTimedGame();
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    private void startEndlessGame() {
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine();
        Player player = new Player(playerName);

        System.out.print("Enter difficulty (easy, medium, hard): ");
        String difficulty = scanner.nextLine();

        GameBoard board = new GameBoard(difficulty, null); // Replace null with card list
        EndlessGame game = new EndlessGame(board, player);

        GameController gameController = new GameController(game);
        gameController.startGame();
    }

    private void startTimedGame() {
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine();
        Player player = new Player(playerName);

        System.out.print("Enter difficulty (easy, medium, hard): ");
        String difficulty = scanner.nextLine();

        int countdownTime = determineCountdown(difficulty);
        GameBoard board = new GameBoard(difficulty, null); // Replace null with card list
        TimedGame game = new TimedGame(board, player, countdownTime);

        GameController gameController = new GameController(game);
        gameController.startGame();
    }

    private int determineCountdown(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return TimedGame.EASY_TIME;
            case "medium":
                return TimedGame.MEDIUM_TIME;
            case "hard":
                return TimedGame.HARD_TIME;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }
}
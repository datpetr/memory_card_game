# Memory Card Game

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)

## Description
A feature-rich, interactive Memory Card Game implemented in Java. The game offers multiple modes (timed, endless), difficulty levels, user profiles, and detailed statistics tracking. Designed for both casual players and those seeking a challenge, it provides a visually engaging and user-friendly experience.

## Tree Structure
```
memory_card_game_new_new/
├── full_memory_game_uml.puml
├── memory_card_game.iml
├── libs/
│   └── gson-2.10.1.jar
├── src/
│   ├── main/
│   │   └── cardgame/
│   │       ├── Main.java
│   │       ├── game/
│   │       │   ├── EndlessGame.java
│   │       │   ├── Game.java
│   │       │   └── TimedGame.java
│   │       ├── model/
│   │       │   ├── Card.java
│   │       │   ├── CardBehavior.java
│   │       │   ├── Deck.java
│   │       │   ├── GameBoard.java
│   │       │   ├── Player.java
│   │       │   └── Timer.java
│   │       ├── profile/
│   │       │   ├── GlobalProfileContext.java
│   │       │   ├── ProfileManager.java
│   │       │   ├── ProfileSelectionDialog.java
│   │       │   ├── ProfileStatsDialog.java
│   │       │   └── UserProfile.java
│   │       ├── stats/
│   │       │   ├── GameStatistics.java
│   │       │   └── StatsManager.java
│   │       └── ui/
│   │           ├── ButtonEffectManager.java
│   │           ├── CardRenderer.java
│   │           ├── ControlPanel.java
│   │           ├── DifficultySelectionPanel.java
│   │           ├── GameBoardUI.java
│   │           ├── GameOverDialog.java
│   │           ├── GameStatusPanel.java
│   │           ├── ModeSelectionPanel.java
│   │           └── WelcomePanel.java
│   └── resources/
       ├── statistics.json
       └── images/
           ├── back2.jpg
           ├── backCards/
           ├── easy/
           ├── hard/
           └── medium/

```

## Installation
### Prerequisites
- GSON
- JavaFX
- Java JDK 17 or higher
- Git

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/datpetr/memory_card_game/tree/master
   ```
2. Navigate to the project directory:
   ```sh
   cd memory_card_game_new_new
   ```
3. Ensure `gson-2.10.1.jar` is present in the `libs/` directory (already included).
4. Compile the project:
   ```sh
   javac -cp libs/gson-2.10.1.jar -d out src/main/cardgame/Main.java
   ```

## Usage
To run the game:
```sh
java -cp "out;libs/gson-2.10.1.jar" cardgame.Main
```

- On Unix/Mac, replace `;` with `:` in the classpath.
- The game launches a GUI for mode, difficulty, and profile selection.

## Main Objectives
- Provide an engaging memory card game with:
  - Multiple game modes (timed, endless)
  - Three difficulty levels
  - User profile management
  - Game statistics tracking
  - Visually appealing and responsive UI

## Implementation Details
- **Language:** Java 17
- **Libraries:** [Gson](https://github.com/google/gson) for JSON handling
- **Architecture:** Modular, OOP, MVC-inspired separation (model, UI, logic)
- **Design:** Extensible for new modes/difficulties, persistent statistics, and user profiles
- **Key Decisions:**
  - Use of Gson for lightweight, fast JSON serialization
  - Decoupled UI and game logic for maintainability

## Screenshots/Diagrams
![Game Board Screenshot](src/resources/images/easy/card1.png)

*For more diagrams, see `full_memory_game_uml.puml`.*

## Contributing
1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request


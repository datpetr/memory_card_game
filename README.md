# Memory Card Game

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Java](https://img.shields.io/badge/java-17%2B-orange)

## Description
A feature-rich, interactive Memory Card Game implemented in Java. The game offers multiple modes (timed, endless), difficulty levels, user profiles, and detailed statistics tracking. Designed for both casual players and those seeking a challenge, it provides a visually engaging and user-friendly experience.

This project aims to enhance memory skills while providing entertainment through customizable gameplay options and progress tracking capabilities.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [Project Structure](#project-structure)
- [Implementation Details](#implementation-details)
- [Screenshots/Diagrams](#screenshotsdiagrams)
- [Contributing](#contributing)
- [Contact](#contact)

## Installation
### Prerequisites
- Java JDK 17 or higher
- GSON library (included in the project)
- JavaFX 24.0.1 (included in the project)
- Git

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/datpetr/memory_card_game/tree/master
   ```
2. Navigate to the project directory:
   ```sh
   cd memory_card_game
   ```
3. Ensure `gson-2.10.1.jar` is present in the `libs/` directory (already included).
4. Compile the project with JavaFX support:
   ```sh
   mkdir -p out
   javac -d out -cp "libs/gson-2.10.1.jar:libs/javafx-sdk-24.0.1/lib/*" src/main/cardgame/Main.java $(find src -name "*.java")
   ```
   Note: On Windows, replace `:` with `;` in the classpath.

## Usage
### Running from Console
To run the game from the command line:
```sh
# On Linux/Mac:
java --module-path libs/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out:libs/gson-2.10.1.jar" main.cardgame.Main

# On Windows:
java --module-path libs/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out;libs/gson-2.10.1.jar" main.cardgame.Main
```

- On Unix/Mac, replace `;` with `:` in the classpath.
- The game launches a GUI where you can:
  1. Select your user profile or create a new one
  2. Choose game mode (timed or endless)
  3. Select difficulty level (easy, medium, hard)
  4. Start playing and match card pairs
  5. View your statistics when the game ends

## Features
- 🎮 **Multiple Game Modes**
  - ⏱️ Timed mode: Race against the clock
  - 🔄 Endless mode: Play at your own pace
- 🏆 **Three Difficulty Levels**
  - 🟢 Easy: Fewer cards, simpler patterns
  - 🟡 Medium: Balanced challenge
  - 🔴 Hard: More cards, complex patterns
- 👤 **User Profile Management**
  - Create and select profiles
  - Track progress across sessions
- 📊 **Statistics Tracking**
  - Games played
  - Win/loss ratio
  - Best times
  - Match accuracy
- 🖼️ **Visually Appealing UI**
  - Smooth animations
  - Responsive design
  - Custom card designs

## Project Structure
```
memory_card_game/
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

### Key Directories:
- **src/main/cardgame/game/**: Contains the core game logic and different game modes
- **src/main/cardgame/model/**: Game model components like Card, Deck, and GameBoard
- **src/main/cardgame/profile/**: User profile management
- **src/main/cardgame/stats/**: Statistics tracking functionality
- **src/main/cardgame/ui/**: User interface components
- **src/resources/**: Game assets including card images and statistics storage

## Implementation Details
- **Language:** Java 17
- **Libraries:** [Gson](https://github.com/google/gson) for JSON handling
- **Architecture:** Modular, OOP, MVC-inspired separation (model, UI, logic)
- **Design:** Extensible for new modes/difficulties, persistent statistics, and user profiles
- **Key Decisions:**
  - Use of Gson for lightweight, fast JSON serialization
  - Decoupled UI and game logic for maintainability
  - State management through dedicated context classes
  - Event-driven UI updates for responsive gameplay

## Screenshots/Diagrams
![Example Of Card](/src/main/resources/images/easy/card1.png)
![Example Of Card](/src/main/resources/images/backCards/easyback.png)

## Contributing
1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## Contact
- **Developers:**
  Petr Datsenko
  Samuil Datsenko
  Elizabeth Polyakova
  Ahmet Mustafa Turac
- **Email:** [dat.petr@gmail.com]
- **Issues:** [GitHub Issues](https://github.com/datpetr/memory_card_game/issues)

---

*Last Updated: 2025*

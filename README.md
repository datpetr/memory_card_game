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
- [Project Structure](#project-structure)
- [Implementation Details](#implementation-details)
- [Screenshots/Diagrams](#screenshotsdiagrams)
- [Contributing](#contributing)
- [Contact](#contact)

## Installation
### Prerequisites
- Java JDK 17 or higher
- GSON library (`libs/gson-2.10.1.jar`, included in the project)
- JavaFX SDK (e.g., version 24.0.1 or newer, to be downloaded separately)
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
3. **Configure JavaFX**:
    - Download the JavaFX SDK from [Gluon](https://gluonhq.com/products/javafx/). Choose a version compatible with your JDK (e.g., 24.0.1 for JDK 17+).
    - Extract the SDK to a known location on your system (e.g., `/path/to/your/javafx-sdk-24.0.1/`).
    - You will need the path to the `lib` folder within this SDK for the commands below. Let's refer to this as `PATH_TO_JAVAFX_LIB` (e.g., `/path/to/your/javafx-sdk-24.0.1/lib`).

4. Compile the project (from bash):
   Ensure `gson-2.10.1.jar` is present in the `libs/` directory.
   Replace `PATH_TO_JAVAFX_LIB` with the actual path to your JavaFX SDK's `lib` folder.
   ```sh
   mkdir -p out
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -d out -cp "libs/gson-2.10.1.jar" $(find src -name "*.java")
   ```

   Replace `/path/to/javafx-sdk/lib` with the actual path to your JavaFX SDK's `lib` folder.
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


## Usage
### Launching the Project from Bash

To run the game after compiling it, use the following commands in your terminal.

**1. Set Your JavaFX Path:**

First, make sure you know the exact path to the `lib` folder of your JavaFX SDK. This is the same path you used during compilation. We'll call this `PATH_TO_JAVAFX_LIB`.

1. **Launch the application** from your terminal:

   **Linux/macOS:**
   ```sh
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out:libs/gson-2.10.1.jar" main.cardgame.Main
   ```

*   **On Windows:**
    Ensure `PATH_TO_JAVAFX_LIB` is a Windows-style path (e.g., `C:\\path\\to\\javafx-sdk\\lib`). The classpath separator is a semicolon (`;`).
    ```sh
    java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp "out;libs\\gson-2.10.1.jar" main.cardgame.Main
    ```

    **Note for Windows Users:** While Java can sometimes be flexible with forward slashes (`/`) versus backslashes (`\\`) in paths provided as arguments, the classpath separator must be a semicolon (`;`).

## Implementation Details
- **Language:** Java 17
- **Libraries:** [Gson](https://github.com/google/gson) for JSON handling
- **Architecture:** Modular, OOP, MVC-inspired separation (model, UI, logic)
- **Design:** Extensible for new modes/difficulties, persistent statistics, and user profiles
- **Key Decisions:**
    - Use of Gson for lightweight, fast JSON serialization
    - Decoupled UI and game logic for maintainability

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
   1. Petr Datsenko
   2. Samuil Datsenko
   3. Elizabeth Polyakova
   4. Ahmet Mustafa Turac
- **Email:** [dat.petr@gmail.com]
- **Issues:** [GitHub Issues](https://github.com/datpetr/memory_card_game/issues)

---

*Last Updated: 2025*




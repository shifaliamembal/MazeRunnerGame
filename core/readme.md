# Lost in the Lab

## Background Story
You are **Dr. Nova**, a scientist working in a cutting-edge research facility. During an experimental AI system test, an error triggers a self-destruct protocol, sealing you inside a labyrinth of corridors and locked doors. Rogue security drones and traps now threaten your survival. To escape, you must collect override codes (keys) to unlock the exit and navigate the maze of dangers.

---

## Project Structure

The project is organized into distinct components, including screens, entities, and utilities. The structure can be described as follows:

### Class Hierarchy Overview
- **Screens**: Implements the libGDX `Screen` interface for various game states such as the main menu, settings, and gameplay.
- **Entities**: Includes core elements like the protagonist (Dr. Nova), enemies, traps, and interactive objects (keys, power-ups, etc.).
- **Utilities**: Handles support functions like font management and scoring.

### UML Class Diagram
_The UML class diagram illustrating the full structure is included in the repository._

---

## How to Run the Game
1. Clone the repository.
2. Ensure Java (JDK 8 or higher) is installed.
3. Install Gradle if not already present.
4. Run the following commands:
   ```bash
   gradle desktop:run
   ```
5. Use the game menu to start, customize settings, or exit the game.

---

## Gameplay Mechanics

### Core Features
- **Character Movement**:
    - **Walk**: Arrow keys or WASD.
    - **Run**: Hold `Shift` + Arrow keys for faster movement.

- **Limited Lives**: Dr. Nova has a finite number of lives, displayed in the HUD.

- **Key Collection**: Find and collect holographic access keycards to unlock the exit.

- **Victory Condition**: Escape the maze by unlocking the exit with the key before losing all lives.

- **Game Over**: Lose all lives or run out of time.

### Obstacles
1. **Laser Grids**: Glowing red beams that deal damage on contact.
    - Flickers briefly before activation.
    - Emits a laser sound when triggered.
2. **Rogue Spider Drones**: Small robots patrolling corridors.
    - Intelligent pathfinding within range.
    - Close-range attacks that deal damage.
3. **Electrified Floor Spikes**: Intermittent traps.
    - Damage and sound effects when active.

### Collectibles & Power-Ups
- **Lives**: Glowing blue orbs restore health.
- **Power-Ups**: Found in chests and stored in the inventory for later use.
    - **Bomb**: Clears surrounding walls or kills enemies.
    - **Sandwich**: Grants endless stamina temporarily.
    - **Arrow**: Points toward the keycard location.
    - **Shield**: Provides temporary immunity.

### HUD Features
- **Lives**: Green bar indicating health.
- **Stamina**: Displays sprinting energy.
- **Key Indicator**: Shows if the keycard is collected.
- **Inventory**: Displays collected power-ups.
- **Exit Arrow**: Points toward the maze exit after collecting the key.
- **Timer**: Displays remaining time.

### Scoring System
- **Time Bonus**: Rewards for minimizing time spent in the maze.
- **Lives Collected**: Bonus for collecting health orbs.
- **Chests Opened**: Bonus for exploring and looting chests.
- **Enemies Killed**: Bonus for eliminating threats using bombs.

---

## Game Menus
1. **Main Menu**:
    - Start the game.
    - Access background story and settings.
    - Mute/unmute music.
    - Exit the game.

2. **Difficulty and Maze Settings**:
    - Difficulty: Easy, Medium, or Hard.
    - Maze Size: Small, Medium, or Large.

3. **Briefing**: Provides gameplay context before starting.

4. **Victory Screen**:
    - Displays final stats (time and score).
    - Congratulatory message: _"You have survived the Lab! The world is safe…for now."_

5. **Game Over Screen**:
    - Message: _"Experiment failed? Try again."_
    - Allows return to the main menu.

---

## Multimedia Elements
- **Background Music**: Sci-fi ambiance enhances immersion.
- **Sound Effects**:
    - Footsteps, laser sounds, zap effects for damage.
    - Victory and game-over tones.
- **Visuals**:
    - Minimalistic sci-fi sprites with animations for walking, running, and reactions.
    - Clean, text-based HUD design.

---

## Additional Information
- **Supported Platforms**: Desktop (Windows, macOS, Linux).
- **Rendering**: Top-down 2D view using libGDX.
- **Dynamic Scaling**: Supports various screen sizes and zoom levels.

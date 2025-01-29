# Lost in the Lab

## Background Story
You are **Dr. Nova**, a scientist working in **NovaTech’s** cutting-edge research facility. During an experimental AI system test, **NEURA**, a rogue artificial intelligence developed by Dr. Nova and his team, experiences a critical bug. The malfunction triggers an automatic lockdown sequence and activates the facility’s self-destruct protocol, sealing you inside a labyrinth of corridors and locked doors. Now, the very security systems designed to protect the lab have turned against you—rogue drones patrol the halls, lethal traps block your path, and survival is uncertain. To escape, you must collect override codes (keys) to disable the lockdown and find your way to the exit before time runs out.

---

## Project Structure

The project is organized into distinct components, including screens, entities, and utilities. The structure can be described as follows:

### Class Hierarchy Overview
- **Screens**: Implements the libGDX `Screen` interface for various game states such as the main menu, settings, gameplay, and pause menu.
- **Entities**: Includes core elements like the protagonist (Dr. Nova), enemies, traps, and interactive objects (keys, power-ups, etc.).
- **Utilities**: Handles support functions like font management and a desktop launcher.

### UML Class Diagram
_The UML class diagram illustrating the full structure is included in the repository. All attributes and methods have been omitted for simplicity, focusing solely on the class hierarchy and relationships._

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

### Note for Mac Users
Mac users need to add the following VM option when running the game:
```bash
-XstartOnFirstThread
```
This can be configured in your IDE or command-line execution.

### Note for Windows & Linux Users
Windows and Linux users **should not** include the `-XstartOnFirstThread` option, as it is only required for macOS.  
Using this option on non-Mac systems may cause errors or prevent the game from running correctly.  
Simply run the game **without** this VM option in your IDE or command-line execution.

---

## Gameplay Mechanics

### Core Features
- **Character Movement**:
    - **Walk**: Arrow keys or WASD.
    - **Run**: Hold `Shift` + Arrow keys for faster movement.

- **Limited Lives**: Dr. Nova has a finite number of lives, displayed in the HUD.

- **Key Collection**: Find a holographic access keycard hidden in **one** of the many chests scattered across the map to unlock the exit.

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

To use a power-up, press the corresponding number key based on its position in the inventory. For example, if slot **3** contains the **Sandwich**, pressing the `3` key equips and activates the Sandwich power-up.


### HUD Features
- **Lives**: Green bar indicating health.
- **Stamina**: Yellow bar displaying sprinting energy.
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
    - Start the game using `To the Lab`.
    - Access background story via the `Info`.
    - Mute/unmute music.
    - Exit the game.

2. **Difficulty and Maze Settings**:
    - Difficulty: Easy, Medium, or Hard.
    - Maze Size: Small, Medium, or Large.

3. **Briefing**: Provides gameplay context before starting.

4. **Pause Menu**:
    - Activated by pressing `ESC` during gameplay.
    - Allows the player to resume the game, mute/unmute background music, return to the main menu, or quit the game.

5. **Victory Screen**:
    - Displays final stats (time and score).
    - Congratulatory message: _"You have survived the Lab! The world is safe…for now."_

6. **Game Over Screen**:
    - Message: _"Experiment failed? Try again."_
    - Allows return to the main menu.

---

## Multimedia Elements
- **Background Music**: Sci-fi ambiance enhances immersion.
- **Sound Effects**:
    - Footsteps, laser sounds, zap effects for damage.
    - Item collection and usage sounds.
    - Victory and game-over tones.
- **Visuals**:
    - Minimalistic sci-fi sprites with animations for walking, running, and reactions.
    - Clean, text-based HUD design.

---

## Additional Information
- **Supported Platforms**: Desktop (Windows, macOS, Linux).
- **Rendering**: Top-down 2D view using libGDX.
- **Dynamic Scaling**: Supports various screen sizes and zoom levels. 
  - Zoom-in: Press `I`
  - Zoom-out: Press `O`

---

## Bonus Implementations
Our project goes beyond the base requirements with several unique and creative enhancements:

1. **Random Maze Generation**: Each maze is procedurally generated, ensuring every game session is unique. This feature serves as a standout aspect of our project.
2. **Nine Distinct Map Types**: Players can select from nine combinations of difficulty levels and maze sizes, offering extensive variety and replayability.
3. **Thematic Consistency**: The story of Dr. Nova is entirely self-made, with all multimedia elements carefully chosen to align with the high-tech, sci-fi narrative.
4. **Customized Assets**: Key game elements, including the character (Dr. Nova) and power-up chests, are uniquely modified versions of assets provided in the base repository. This showcases our attention to detail and dedication to originality.
5. **Story-Focused Screens**: The addition of unique **Briefing** and **Info** screens provides players with immersive context, flexibility during gameplay, and an enriched experience seamlessly tied to the background story.

---

## Project Contributors/Members

- **Shifali Shyamsunder Amembal (go49xuw)** – [shifali.amembal@tum.de](mailto:shifali.amembal@tum.de)
- **Alexandru-Cristian Oprea (go57yub)** – [alexandru.oprea@tum.de](mailto:alexandru.oprea@tum.de)
- **Ebrahim Shabbir Udaipurwala (go57jod)** – [ebrahim.udaipurwala@tum.de](mailto:ebrahim.udaipurwala@tum.de)

---

## Credits
### Visual Assets
- **Glowing Blue Orb**: [Free Glowing Ball Sprite](https://lvgames.itch.io/free-glowing-ball-sprite-pixel-fx-rpg-maker-ready)
- **Wall Tiles**: [Free Industrial Zone Tileset](https://free-game-assets.itch.io/free-industrial-zone-tileset-pixel-art)
- **Spider Drone**: [Spider Drone Sprite](https://vivicat.itch.io/spider-drone)
- **Laser Trap and Exit Barrier**: [Sci-Fi Lab Tileset](https://foozlecc.itch.io/sci-fi-lab-tileset-decor-traps)
- **Sandwich**: [Sandwich Art](https://www.pinterest.com/pin/215258057191207440)
- **Spike Trap**: [Animated Traps](https://stealthix.itch.io/animated-traps)
- **Explosion**: [Explosion Animations Pack](https://ansimuz.itch.io/explosion-animations-pack)
- **Orbitron Font**: [Google Fonts](https://fonts.google.com/specimen/Orbitron)


### Audio Assets
- **Key Card Collected/Challenge Completed Sound**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/c80eea09-8a9f-4207-9357-709df1b1848f/)
- **Movement Sound**: [Uppbeat](https://uppbeat.io/sfx/footsteps-walking-on-concrete/4107/17549)
- **Lives Collected Sound**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/c80eea09-8a9f-4207-9357-709df1b1848f/)
- **Collect Item from Chest**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/8b6ec37b-75fa-4945-975a-9573abbc8a48/)
- **Laser Grid Sound**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/58422a34-2669-47a9-8151-b42d2e8eb345/)
- **Bomb Explosion Sound**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/955d5cf0-6ba1-4b2c-b4e6-3781deeed9d0/)
- **Rogue Spider Drone Sound**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/e86aeb9e-5c5a-4276-aa66-69a2e6f49df9/)
- **Shield Sound**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/36b53973-c6cc-45f1-b855-97cb7756a59a/)
- **Sandwich Stamina Sound**: [Pixabay](https://pixabay.com/sound-effects/eating-effect-254996/)
- **Defeat/Game Over Sound**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/e1cfe332-e03a-478d-984b-8e1e7228db41/)
- **Win/Victory Sound**: [Mixkit](https://mixkit.co/free-sound-effects/game/)
- **In-Game Music**: [Epidemic Sound](https://www.epidemicsound.com/sound-effects/tracks/b090e0fb-26d7-489f-86c7-b553257da5c3/)
- **Background Music Theme**: [Free Sci-Fi Music](https://alkakrab.itch.io/free-sci-fi-music-2)  

### Screen Backdrops
- **MenuScreen, DifficultyScreen, and MazeSizeScreen Backdrop**: AI-generated using [Google Gemini](https://gemini.google.com)
- **InfoScreen and BriefingScreen Backdrop**: AI-generated using [Microsoft Copilot](https://copilot.microsoft.com)
- **VictoryScreen Backdrop**: AI-generated using [Google Gemini](https://gemini.google.com)
- **GameOverScreen Backdrop**: AI-generated using [Google Gemini](https://gemini.google.com)  

package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    //private Player player;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    private FontManager fontManager;

    private PauseMenu pauseMenu;

    private float difficulty = 1f;
    private int mazeSize;

    private Music backgroundMusic;


    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadAssets(); // Load character animation

        // Play some background music
        // Background sound
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("MenuSound.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        mazeSize = 51;
        Maze.saveMaze(Maze.generateMaze(mazeSize,mazeSize, difficulty), "maps/maze.properties");

        goToMenu();// Navigate to the menu screen

        fontManager = new FontManager();
        menuScreen = new MenuScreen(this);
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadAssets() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
        if (fontManager != null) {
            fontManager.dispose();
        }
    }

    // Getter methods
    public Skin getSkin() {
        return skin;
    }

    //public Animation<TextureRegion> getCharacterDownAnimation() {
    //    return characterDownAnimation;
    //}
//    public Player getPlayer() {
//        return player;
//    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public int getMazeSize() {
        return mazeSize;
    }
}

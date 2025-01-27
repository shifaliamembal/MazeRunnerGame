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
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    private SpriteBatch spriteBatch;

    private Skin skin;

    private FontManager fontManager;

    private float difficulty = 1f;
    private int mazeSize;

    private Music backgroundMusic;
    private Music gameMusic;


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
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json"));
        this.loadAssets();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("g_overmusic.mp3"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("MenuSound.mp3"));
        backgroundMusic.setLooping(true);
        gameMusic.setLooping(true);

        playBackgroundMusic();
        mazeSize = 101;

        fontManager = new FontManager();
        menuScreen = new MenuScreen(this);
        goToMenu();
    }

    public void playBackgroundMusic() {
        gameMusic.stop();
        backgroundMusic.play();
    }

    public void playGameMusic() {
        backgroundMusic.stop();
        gameMusic.play();
    }

    public void stopAllMusic() {
        backgroundMusic.stop();
        gameMusic.stop();
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
     * Switches to the game screen and generates a new maze.
     */
    public void goToGame() {
        Maze.saveMaze(Maze.generateMaze(mazeSize, mazeSize, difficulty), "maps/maze.properties");
        this.setScreen(new GameScreen(this));
        if (menuScreen != null) {
            menuScreen.dispose();
            menuScreen = null;
        }
    }

    /**
     * Restarts the game using the same maze.
     */
    public void restartGame() {
        this.setScreen(new GameScreen(this));
        if (menuScreen != null) {
            menuScreen.dispose();
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
        getScreen().hide();
        getScreen().dispose();
        spriteBatch.dispose();
        skin.dispose();
        if (fontManager != null) {
            fontManager.dispose();
        }
        super.dispose();
    }

    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public int getMazeSize() {
        return mazeSize;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void setMazeSize(int mazeSize) {
        this.mazeSize = mazeSize;
    }

    public void stopMusic() {
        backgroundMusic.stop();
    }

    /**
     * Returns the background music of the game.
     * @return The background music of the game.
     */
    public Music getBackgroundMusic() {
        return backgroundMusic;
    }
}

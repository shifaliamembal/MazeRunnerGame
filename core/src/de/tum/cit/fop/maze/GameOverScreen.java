package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The GameOverScreen class is displayed when the player dies.
 * It shows a 'Game-Over' message along with the player's score.
 * Additionally, it provides options to restart the game or return to the main menu.
 */
public class GameOverScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch batch;

    private final BitmapFont titleFont;
    private final BitmapFont font;

    private Viewport viewport;
    private OrthographicCamera hudCamera;

    private final Music gameOverMusic;

    private final String message = "Experiment failed - Try again?";
    private final String scoreMessage;
    private final String retryMessage = "Press R to Retry";
    private final String menuMessage = "Press M to Return to Menu";
    private final Texture background;

    /**
     * Constructs a new GameOverScreen.
     *
     * @param game The game instance of MazeRunner game.
     * @param player The player of the game whose score is displayed on the screen.
     */
    public GameOverScreen(MazeRunnerGame game, Player player) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.titleFont = FontManager.getOrbitronTitleFont();
        this.font = FontManager.getOrbitronBodyFont();
        this.gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("g_overmusic.mp3"));
        this.gameOverMusic.setLooping(true);
        this.gameOverMusic.setVolume(0.7f);
        this.gameOverMusic.play();
        this.scoreMessage = "Your Score: " + player.getScore();
        background = new Texture("gameoverimg.jpeg");

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new StretchViewport(1920, 1080, hudCamera);
        viewport.apply();
    }

    /**
     * Called when the screen is set to be visible.
     */
    @Override
    public void show() {
    }

    /**
     * Renders the "Game Over" screen and displays the message, player's score,
     * and the options to restart the game or return to the main menu.
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        hudCamera.update();
        batch.setProjectionMatrix(hudCamera.combined);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float centerX = hudCamera.viewportWidth / 2f;
        float centerY = hudCamera.viewportHeight / 2f;

        GlyphLayout layout = new GlyphLayout();

        titleFont.getData().setScale(1.3f);
        titleFont.setColor(Color.RED);
        layout.setText(titleFont, message);
        float messageWidth = layout.width;
        titleFont.draw(batch, message, centerX - messageWidth / 2, centerY + 80);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        layout.setText(font, scoreMessage);
        float scoreMessageWidth = layout.width;
        font.draw(batch, scoreMessage, centerX - scoreMessageWidth / 2, centerY + 200);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        layout.setText(font, retryMessage);
        float retryMessageWidth = layout.width;
        font.draw(batch, retryMessage, centerX - retryMessageWidth / 2, centerY - 90);

        layout.setText(font, menuMessage);
        float menuMessageWidth = layout.width;
        font.draw(batch, menuMessage, centerX - menuMessageWidth / 2, centerY - 140);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            gameOverMusic.stop();
            game.restartGame();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            gameOverMusic.stop();
            game.goToMenu();
        }
    }

    /**
     * Updates the screen dimensions when resized.
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hudCamera.setToOrtho(false, width, height);
        hudCamera.position.set(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2, 0);
        hudCamera.update();
    }

    /**
     * Called when the game is paused.
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the screen resumes.
     */
    @Override
    public void resume() {
    }

    /**
     * Called when the screen is hidden.
     */
    @Override
    public void hide() {
    }

    /**
     * Disposes of the resources used by the GameOverScreen,
     * including the SpriteBatch, Background image and Music.
     */
    @Override
    public void dispose() {
        batch.dispose();
        if (gameOverMusic != null){
            gameOverMusic.dispose();
        }
        background.dispose();
    }
}
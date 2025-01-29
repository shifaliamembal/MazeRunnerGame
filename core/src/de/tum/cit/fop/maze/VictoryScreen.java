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
 * VictoryScreen class is displayed when the player finds the key-card
 * to unlock the exit and wins the game.
 * It shows a congratulatory message along with the player's score, time left
 * and provides options to restart the game or return to the main menu.
 */
public class VictoryScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch batch;

    private final BitmapFont titleFont;
    private final BitmapFont font;

    private Viewport viewport;
    private OrthographicCamera hudCamera;

    private final Music gameOverMusic;

    private final String message = "You have survived the Lab!" + "\nThe world is safe...for now.";
    private final String scoreMessage;
    private final String retryMessage = "Press R to Retry";
    private final String menuMessage = "Press M to return to Menu";
    private final Texture background;
    private final String timeLeftMessage;


    /**
     * Constructs a new VictoryScreen.
     *
     * @param game   It is an instance of MazeRunner game.
     * @param player The player whose score will be displayed on the screen when won.
     * @param timeLeft The remaining time when the player wins.
     */
    public VictoryScreen(MazeRunnerGame game, Player player, String timeLeft) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.titleFont = FontManager.getOrbitronTitleFont();
        this.font = FontManager.getOrbitronBodyFont();
        this.gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("g_overmusic.mp3"));
        this.gameOverMusic.setLooping(true);
        this.gameOverMusic.setVolume(0.7f);
        this.gameOverMusic.play();
        this.scoreMessage = "Your Score: " + player.getScore();
        this.timeLeftMessage = "Time Left: " + timeLeft;
        background = new Texture("victorybg.jpeg");

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new StretchViewport(1920, 1080, hudCamera);
        viewport.apply();
    }

    /**
     * Called when the screen is set to be visible
     */
    @Override
    public void show() {
    }

    /**
     * Renders this Victory screen, displaying the congratulatory message,
     * player's score, and the options to restart the game or return to the menu.
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

        titleFont.getData().setScale(1.1f);
        titleFont.setColor(Color.YELLOW);
        layout.setText(titleFont, message);
        float messageWidth = layout.width;
        titleFont.draw(batch, message, centerX - messageWidth / 2, centerY + 340);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        layout.setText(font, scoreMessage);
        float scoreMessageWidth = layout.width;
        font.draw(batch, scoreMessage, centerX - scoreMessageWidth / 2, centerY + 180);

        font.setColor(Color.WHITE);
        layout.setText(font, timeLeftMessage);
        float timeLeftMessageWidth = layout.width;
        font.draw(batch, timeLeftMessage, centerX - timeLeftMessageWidth / 2, centerY + 140);

        font.setColor(Color.WHITE);
        layout.setText(font, retryMessage);
        float retryMessageWidth = layout.width;
        font.draw(batch, retryMessage, centerX - retryMessageWidth / 2, centerY - 240);

        layout.setText(font, menuMessage);
        float menuMessageWidth = layout.width;
        font.draw(batch, menuMessage, centerX - menuMessageWidth / 2, centerY - 280);

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
     * Disposes of the resources used by the VictoryScreen.
     * Including the SpriteBatch, music and background image.
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
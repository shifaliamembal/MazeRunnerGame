package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * VictoryScreen class is displayed when the player finds the key-card
 * to unlock the exit and wins the game.
 * It shows a congratulatory message along with the player's score
 * and provides options to restart the game or return to the main menu.
 */
public class VictoryScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch batch;
    private final BitmapFont titleFont;
    private final BitmapFont font;
    private final Music gameOverMusic;
    private final String message = "You have survived the Lab!" + "\nThe world is safe...for now.";
    private final String scoreMessage;
    private final String retryMessage = "Press R to Retry";
    private final String menuMessage = "Press M to return to Menu";
    private Player player;
    private final Texture background;

    /**
     * Constructs a new VictoryScreen.
     *
     * @param game   It is an instance of MazeRunner game.
     * @param player The player whose score will be displayed on the screen when won.
     */
    public VictoryScreen(MazeRunnerGame game, Player player) {
        this.game = game;
        this.player = player;
        this.batch = new SpriteBatch();
        this.titleFont = FontManager.getOrbitronTitleFont();
        this.font = FontManager.getOrbitronBodyFont();
        this.gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("g_overmusic.mp3"));
        this.gameOverMusic.setLooping(true);
        this.gameOverMusic.setVolume(0.7f);
        this.gameOverMusic.play();
        this.scoreMessage = "Your Score: " + player.getScore();
        background = new Texture("victorybg.jpeg");
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

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout();

        titleFont.getData().setScale(1.1f);
        titleFont.setColor(Color.YELLOW);
        layout.setText(titleFont, message);
        float messageWidth = layout.width;
        titleFont.draw(batch, message, centerX - messageWidth / 2, centerY + 220);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        layout.setText(font, scoreMessage);
        float scoreMessageWidth = layout.width;
        font.draw(batch, scoreMessage, centerX - scoreMessageWidth / 2, centerY + 80);

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
     * Resizes the screen. This method is empty in the VictoryScreen implementation.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
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
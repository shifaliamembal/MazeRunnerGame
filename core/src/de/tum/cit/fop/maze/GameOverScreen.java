package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The GameOverScreen class is displayed when the player dies.
 * It provides options to restart the game or return to the main menu.
 */
public class GameOverScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch batch;
    private final BitmapFont titleFont;
    private final BitmapFont font;
    private final Music gameOverMusic;
    private final String message = "Game Over!";
    private final String scoreMessage;
    private final String retryMessage = "Press R to Retry";
    private final String menuMessage = "Press M to return to Menu";
    private Player player;

    public GameOverScreen(MazeRunnerGame game, Player player) {
        this.game = game;
        this.player = player;
        this.batch = new SpriteBatch();
        this.titleFont = game.getSkin().getFont("font");
        this.font = game.getSkin().getFont("font");
        this.gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("g_overmusic.mp3"));
        this.gameOverMusic.setLooping(true);
        this.gameOverMusic.setVolume(0.7f);
        this.gameOverMusic.play();
        this.scoreMessage = "Your Score: " + player.getScore();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        batch.begin();

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        titleFont.getData().setScale(1.8f);
        titleFont.setColor(Color.RED);
        titleFont.draw(batch, message, centerX - font.getScaleX() * scoreMessage.length() * 10, centerY + 200);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        font.draw(batch, scoreMessage, centerX - font.getScaleX() * scoreMessage.length() * 10, centerY + 80);

        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        font.draw(batch, retryMessage, centerX - font.getScaleX() * retryMessage.length() * 10, centerY - 90);
        font.draw(batch, menuMessage, centerX - font.getScaleX() * menuMessage.length() * 10, centerY - 140);

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

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (gameOverMusic != null){
            gameOverMusic.dispose();
        }
    }
}

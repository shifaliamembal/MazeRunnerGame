package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
    private final BitmapFont font;
    private final String message = "Game Over!";
    private final String retryMessage = "Press R to Retry";
    private final String menuMessage = "Press M to return to Menu";

    public GameOverScreen(MazeRunnerGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = game.getSkin().getFont("font");
    }

    @Override
    public void show() {
        // No specific initialization required on show.
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a dark red background.
        ScreenUtils.clear(Color.DARK_GRAY);

        batch.begin();

        // Draw the "Game Over" message centered on the screen.
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        font.setColor(Color.RED);
        font.draw(batch, message, centerX - font.getScaleX() * message.length() * 10, centerY + 50);

        font.setColor(Color.WHITE);
        font.draw(batch, retryMessage, centerX - font.getScaleX() * retryMessage.length() * 10, centerY - 10);
        font.draw(batch, menuMessage, centerX - font.getScaleX() * menuMessage.length() * 10, centerY - 50);

        batch.end();

        // Handle input for retrying or returning to the menu.
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.goToGame(); // this would restart directly on the new game and not main menu
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.goToMenu(); // Method to return to the main menu.
        }
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing if necessary.
    }

    @Override
    public void pause() {
        // No actions needed on pause.
    }

    @Override
    public void resume() {
        // No actions needed on resume.
    }

    @Override
    public void hide() {
        // No actions needed on hide.
    }

    @Override
    public void dispose() {
        // Dispose of the sprite batch to free resources.
        batch.dispose();
    }
}

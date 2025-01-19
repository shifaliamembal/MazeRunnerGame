package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.audio.Music;

/**
 * The PauseMenu class represents the pause menu in the game.
 * It allows the player to resume the game, return to the main menu, mute/unmute the background music, or quit the game.
 */
public class PauseMenu extends ScreenAdapter {

    /** The instance of MazeRunnerGame. */
    private final MazeRunnerGame game;

    /** The current GameScreen instance to return to after resuming the game. */
    private final GameScreen gameScreen;

    /** The Stage for rendering UI components in the pause menu. */
    private final Stage stage;

    /** The Skin used for UI styling. */
    private final Skin skin;

    /** The font used for rendering text in the UI. */
    private final BitmapFont font;

    /** The table containing all UI elements in the pause menu. */
    private Table table;

    /** Flag indicating whether the background music is muted. */
    private boolean isMuted = false;

    /** The background music being played in the game. */
    private Music backgroundMusic;

    /**
     * Constructs a PauseMenu.
     *
     * @param game       The instance of MazeRunnerGame.
     * @param gameScreen The GameScreen to return to after resuming the game.
     * @param viewport   The viewport used to set up the stage.
     */
    public PauseMenu(MazeRunnerGame game, GameScreen gameScreen, Viewport viewport) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(viewport);
        this.skin = game.getSkin();
        this.font = FontManager.getOrbitronFont(24, Color.WHITE);
        skin.add("default-font", font);
        this.backgroundMusic = game.getBackgroundMusic();

        createButtons();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Creates and configures the buttons in the pause menu.
     */
    private void createButtons() {
        BitmapFont titleFont = FontManager.getOrbitronFont(36, Color.WHITE);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label titleLabel = new Label("Paused", titleStyle);

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.getLabel().setFontScale(1.2f);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.unpause();
                game.setScreen(gameScreen);
            }
        });

        TextButton mainMenuButton = new TextButton("Return to main menu", skin);
        mainMenuButton.getLabel().setFontScale(1.2f);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        TextButton muteButton = new TextButton("Mute", skin);
        muteButton.getLabel().setFontScale(1.2f);
        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMuted = !isMuted;
                if (isMuted) {
                    backgroundMusic.setVolume(0f);
                    muteButton.setText("Unmute");
                } else {
                    backgroundMusic.setVolume(1f);
                    muteButton.setText("Mute");
                }
            }
        });

        TextButton quitButton = new TextButton("Quit game", skin);
        quitButton.getLabel().setFontScale(1.2f);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table = new Table(skin);
        table.setFillParent(true);
        table.add(titleLabel).padBottom(40).row();
        table.add(resumeButton).uniform().pad(10);
        table.row();
        table.add(muteButton).uniform().pad(10);
        table.row();
        table.add(mainMenuButton).uniform().pad(10);
        table.row();
        table.add(quitButton).uniform().pad(10);
        stage.addActor(table);
    }

    /**
     * Called when this screen is displayed.
     * Sets the input processor to handle stage interactions.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the pause menu on top of the GameScreen.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        gameScreen.render(delta);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     * Updates the viewport with the new dimensions.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when this screen is hidden.
     * Removes the stage's input processor.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Disposes of resources used by the pause menu, including the stage.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
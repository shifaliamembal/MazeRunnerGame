package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * The {@code MenuScreen} class represents the main menu screen of the game.
 * It provides options for starting the game, muting/unmuting the background music,
 * viewing information, and exiting the game.
 */
public class MenuScreen implements Screen {

    /**
     * The stage used to organize and render UI elements on the screen.
     */
    private final Stage stage;

    /**
     * Reference to the main game instance for navigation between screens.
     */
    private final MazeRunnerGame game;

    /**
     * Texture for the menu background image.
     */
    private final Texture background;

    /**
     * Flag to track whether the background music is muted.
     */
    private boolean isMuted = false;

    /**
     * Table layout used for organizing menu components.
     */
    private Table table;

    /**
     * Skin used for styling UI elements.
     */
    private final Skin skin;

    /**
     * Font used for rendering menu text in the Orbitron style.
     */
    private final BitmapFont orbitronFont;

    /**
     * Constructs a new {@code MenuScreen} with the given game instance.
     *
     * @param game the {@link MazeRunnerGame} instance for managing screens and game state
     */
    public MenuScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        background = new Texture(Gdx.files.internal("themed_background.jpg"));

        orbitronFont = FontManager.getOrbitronFont(24, Color.WHITE);
        skin.add("default-font", orbitronFont);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = FontManager.getOrbitronFont(50, Color.WHITE);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = orbitronFont;

        createMenu(titleStyle, buttonStyle);
    }

    /**
     * Creates the menu layout, including the title and buttons for navigation.
     *
     * @param titleStyle   the {@link Label.LabelStyle} for the menu title
     * @param buttonStyle  the {@link TextButton.TextButtonStyle} for menu buttons
     */
    private void createMenu(Label.LabelStyle titleStyle, TextButton.TextButtonStyle buttonStyle) {
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Lost in the Lab", titleStyle);
        table.add(title).padBottom(80).row();

        TextButton goToGameButton = new TextButton("To the Lab", skin);
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new DifficultyScreen(game));
            }
        });
        table.add(goToGameButton).width(300).padBottom(20).row();

        TextButton muteButton = new TextButton("Mute", skin);
        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isMuted = !isMuted;
                if (isMuted) {
                    game.getBackgroundMusic().setVolume(0f);
                    muteButton.setText("Unmute");
                } else {
                    game.getBackgroundMusic().setVolume(1f);
                    muteButton.setText("Mute");
                }
            }
        });
        table.add(muteButton).width(300).padBottom(20).row();

        TextButton infoButton = new TextButton("Info", skin);
        infoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InfoScreen(game));
            }
        });
        table.add(infoButton).width(300).padBottom(20).row();

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        table.add(quitButton).width(300).row();
    }

    /**
     * Renders the menu screen, including the background and UI elements.
     *
     * @param delta the time elapsed since the last frame in seconds
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the background image before the stage drawing
        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getSpriteBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Updates the screen's viewport dimensions when the window is resized.
     *
     * @param width  the new window width
     * @param height the new window height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the screen becomes visible, setting the input processor.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen is hidden, removing the input processor.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Called when the game is paused. No additional logic is required.
     */
    @Override
    public void pause() {}

    /**
     * Called when the game is resumed. No additional logic is required.
     */
    @Override
    public void resume() {}

    /**
     * Disposes of resources used by the menu screen, including the stage and background texture.
     */
    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}

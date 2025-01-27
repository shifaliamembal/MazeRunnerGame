package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The DifficultyScreen class allows the player to select the difficulty level of the game.
 * It displays a UI with buttons for different difficulty options (Easy, Medium, Hard) and
 * a back button to return to the main menu.
 */
public class DifficultyScreen implements Screen {

    /** The Stage used to render UI elements. */
    private final Stage stage;

    /** Reference to the MazeRunnerGame instance. */
    private final MazeRunnerGame game;

    /** The Skin used to style UI components. */
    private Skin skin;

    /** The font used for text rendering. */
    private final BitmapFont orbitronFont;

    /** The background texture for the screen. */
    private final Texture background;

    /**
     * Constructs a DifficultyScreen.
     *
     * @param game The instance of MazeRunnerGame.
     */
    public DifficultyScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        background = new Texture(Gdx.files.internal("themed_background.jpg"));

        orbitronFont = FontManager.getOrbitronFont(24, Color.WHITE); // Load Orbitron font
        skin.add("default-font", orbitronFont); // Set the font in the skin

        // Adjust title and button styles to use Orbitron
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = FontManager.getOrbitronFont(36, Color.WHITE);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = orbitronFont;

        createDifficultySelection(titleStyle, buttonStyle);
    }

    /**
     * Creates the UI components for selecting the difficulty level.
     *
     * @param titleStyle   The style used for the title label.
     * @param buttonStyle  The style used for the buttons.
     */
    private void createDifficultySelection(Label.LabelStyle titleStyle, TextButton.TextButtonStyle buttonStyle) {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Select Difficulty", new Label.LabelStyle(FontManager.getOrbitronTitleFont(), Color.WHITE));
        table.add(title).padBottom(40).row();

        TextButton easyButton = new TextButton("Easy", skin);
        easyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setDifficulty(0.6f);
                game.setScreen(new MazeSizeScreen(game));
            }
        });
        table.add(easyButton).width(300).padBottom(20).row();

        TextButton mediumButton = new TextButton("Medium", skin);
        mediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setDifficulty(0.8f);
                game.setScreen(new MazeSizeScreen(game));
            }
        });
        table.add(mediumButton).width(300).padBottom(20).row();

        TextButton hardButton = new TextButton("Hard", skin);
        hardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setDifficulty(1.0f);
                game.setScreen(new MazeSizeScreen(game));
            }
        });
        table.add(hardButton).width(300).padBottom(20).row();

        TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add(backButton).width(300).padBottom(20).row();
    }

    /**
     * Renders the screen.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getSpriteBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Adjusts the viewport size when the screen is resized.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when this screen becomes the current screen for the game.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when this screen is no longer the current screen for the game.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Called when the game is paused.
     */
    @Override
    public void pause() {}

    /**
     * Called when the game is resumed.
     */
    @Override
    public void resume() {}

    /**
     * Disposes of resources used by this screen, including the stage and background texture.
     */
    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}

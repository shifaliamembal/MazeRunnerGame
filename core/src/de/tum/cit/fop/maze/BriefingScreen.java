package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The BriefingScreen class represents the screen where players are briefed about the game objectives and rules
 * before entering the maze. It displays an animated title, briefing text, and a "Start" button to proceed to the game.
 */
public class BriefingScreen implements Screen {

    /** The stage for rendering UI components. */
    private final Stage stage;

    /** The instance of MazeRunnerGame. */
    private final MazeRunnerGame game;

    /** The background texture for the briefing screen. */
    private final Texture background;

    /** The skin used for UI styling. */
    private Skin skin;

    /** The font used for rendering text in the briefing screen. */
    private final BitmapFont orbitronFont;

    private Label title;
    private Label briefingLabel;
    private TextButton startButton;

    private boolean animationsSkipped = false;

    /**
     * Constructs a new BriefingScreen.
     *
     * @param game The instance of MazeRunnerGame.
     */
    public BriefingScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        background = new Texture("InfoScreenBG.jpg");

        orbitronFont = FontManager.getOrbitronFont(24, Color.WHITE);
        skin.add("default", orbitronFont);

        createBriefingContent();

    }

    /**
     * Creates and configures the briefing content, including the title, briefing text, and start button.
     * Animates the UI components for a smooth transition effect.
     */
    private void createBriefingContent() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = FontManager.getOrbitronFont(36, Color.WHITE);

        Label.LabelStyle textStyle = new Label.LabelStyle();
        textStyle.font = orbitronFont;

        Label title = new Label("Briefing", titleStyle);
        title.getColor().a = 0;
        title.addAction(Actions.fadeIn(2f));
        table.add(title).padBottom(40).row();

        String briefingText = "Welcome, Dr. Nova! Before entering the labyrinth, remember:\n\n" +
                "- Collect the override code to unlock the exit.\n" +
                "- Avoid security drones and traps at all costs.\n" +
                "- Use your wits and agility to navigate the maze.\n\n" +
                "Your mission is critical. Good luck!";
        Label briefingLabel = new Label(briefingText, textStyle);
        briefingLabel.setWrap(true);
        briefingLabel.setAlignment(1);
        briefingLabel.getColor().a = 0;
        briefingLabel.addAction(Actions.sequence(Actions.delay(2f), Actions.fadeIn(2f)));
        table.add(briefingLabel).width(600).padBottom(40).row();

        TextButton startButton = new TextButton("Start", skin);
        startButton.getColor().a = 0;
        startButton.addAction(Actions.sequence(Actions.delay(4f), Actions.fadeIn(1f)));
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame();
            }
        });
        table.add(startButton).width(300).padBottom(20).row();

    }

    /**
     * Renders the briefing screen, including the background and stage.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getSpriteBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !animationsSkipped) {
            skipAnimations();
        }

        stage.act(delta);
        stage.draw();
    }

    private void skipAnimations() {
        animationsSkipped = true;

//        title.clearActions();
//        title.getColor().a = 1;
//
//        briefingLabel.clearActions();
//        briefingLabel.getColor().a = 1;
//
//        startButton.clearActions();
//        startButton.getColor().a = 1;
    }

    /**
     * Called when the screen is resized.
     * Updates the stage's viewport with the new dimensions.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
     * Called when this screen is hidden.
     * Removes the stage's input processor.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Called when the game is paused.
     * Currently unused in this screen.
     */
    @Override
    public void pause() {}

    /**
     * Called when the game is resumed.
     * Currently unused in this screen.
     */
    @Override
    public void resume() {}

    /**
     * Disposes of resources used by the briefing screen, including the stage and background texture.
     */
    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }

}

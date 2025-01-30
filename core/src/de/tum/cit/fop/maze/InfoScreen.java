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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The {@code InfoScreen} class displays background story information to the player.
 * It presents a textual description of the game's premise and provides a "Back" button
 * to return to the main menu.
 */
public class InfoScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private final Skin skin;
    private final Texture background;

    /**
     * Constructs the {@code InfoScreen} with the specified game instance.
     *
     * @param game the main game instance
     */
    public InfoScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        background = new Texture(Gdx.files.internal("InfoScreenBG.jpg"));

        createInfo();
    }

    /**
     * Creates the UI elements for the info screen, including the story, controls, and back button.
     */
    private void createInfo() {
        Table table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        Table textBackground = new Table();
        textBackground.setColor(0, 0, 0, 0.6f); // Semi-transparent black
        textBackground.setFillParent(true);
        stage.addActor(textBackground);

        String storyDescription = "You are Dr. Nova, a scientist working in a cutting-edge research facility. " +
                "While testing an experimental AI system, an error triggers the self-destruct protocol, sealing you inside a labyrinth " +
                "of corridors and locked doors. The facility's rogue security drones and traps have turned against you. " +
                "To escape, you must collect override codes (keys) to unlock the exit and avoid becoming a victim of your own creation.\n";

        String controlsDescription = "Controls:\n" +
                "- Use WASD or Arrow Keys to move.\n" +
                "- Hold Shift while moving to run.\n" +
                "- Press 'E' near chests or the exit barrier to interact.\n" +
                "- Use the corresponding number keys to equip power-ups. \n" +
                "- Press ESC to pause in-game\n" +
                "- Use I/O keys in-game to zoom in/out";

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontManager.getOrbitronFont(22, Color.WHITE);

        Label storyLabel = new Label(storyDescription, labelStyle);
        storyLabel.setWrap(true);

        Label controlsLabel = new Label(controlsDescription, labelStyle);
        controlsLabel.setWrap(true);

        table.add(storyLabel).width(600).pad(20).expandX().center().row();
        table.add(controlsLabel).width(600).pad(20).expandX().center().row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(1.2f);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add(backButton).width(220).height(60).pad(30).expandX().center();
    }

    /**
     * Renders the background and UI elements on the screen.
     *
     * @param delta the time in seconds since the last render call
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
     * Updates the screen viewport when resized.
     *
     * @param width  the new width of the screen
     * @param height the new height of the screen
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Shows the screen and sets the input processor to the stage.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Hides the screen and removes the input processor.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Called when the game is paused (unused in this implementation).
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the game is resumed (unused in this implementation).
     */
    @Override
    public void resume() {
    }

    /**
     * Disposes of the screen resources including the stage and background image.
     */
    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
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

public class InfoScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private Skin skin;
    private final Texture background;

    public InfoScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        background = new Texture(Gdx.files.internal("InfoScreenBG.jpg"));

        createInfo();
    }

    private void createInfo() {
        Table table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        String storyDescription = "You are Dr. Nova (main character), a scientist working in a cutting-edge research facility. " +
                "While testing an experimental AI system, an error triggers the self-destruct protocol, sealing you inside a labyrinth " +
                "of corridors and locked doors. The facility's rogue security drones and traps have turned against you. " +
                "To escape, you must collect override codes (keys) to unlock the exit and avoid becoming a victim of your own creation.";

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontManager.getOrbitronFont(24, Color.WHITE);

        Label storyLabel = new Label(storyDescription, labelStyle);
        storyLabel.setWrap(true);

        table.add(storyLabel).width(600).pad(20).expandX().center().row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add(backButton).width(200).pad(20).expandX().center();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getSpriteBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}

package de.tum.cit.fop.maze;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InfoScreen implements Screen {
    private final Stage stage;
    private final MazeRunnerGame game;
    private Skin skin;

    public InfoScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        createInfo();
    }

    private void createInfo() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        String storyDescription = "You are Dr. Nova (main character), a scientist working in a cutting-edge research facility. " +
                "While testing an experimental AI system, an error triggers the self-destruct protocol, sealing you inside a labyrinth "
                + "of corridors and locked doors. The facility's rogue security drones and traps have turned against you. "
                + "To escape, you must collect override codes (keys) to unlock the exit and avoid becoming a victim of your own creation.";

        Label storyLabel = new Label(storyDescription, skin);
        storyLabel.setWrap(true);

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        table.add(backButton).width(300).padTop(20).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override public void pause() {}

    @Override public void resume() {}

    @Override public void dispose() {
        stage.dispose();
    }
}

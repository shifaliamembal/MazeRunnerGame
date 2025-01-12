package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private boolean isMuted = false; // Track mute state
    private Table table;
    private Skin skin;

    public MenuScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        createMenu();
    }

    private void createMenu() {
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add title
        Label title = new Label("Lost in the Lab", skin, "title");
        table.add(title).padBottom(80).row();

        // Go to Game Button
        TextButton goToGameButton = new TextButton("To the Lab", skin);
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame();
            }
        });
        table.add(goToGameButton).width(300).padBottom(20).row();

        // Mute/Unmute Button
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

        // Info Button
        TextButton infoButton = new TextButton("Info", skin);
        infoButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InfoScreen(game)); // Switch to InfoScreen
            }
        });
        table.add(infoButton).width(300).padBottom(20).row();

        // Quit Button
        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        table.add(quitButton).width(300).row();
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}

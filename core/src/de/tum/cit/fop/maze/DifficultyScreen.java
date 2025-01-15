package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class DifficultyScreen implements Screen {

    private final Stage stage;
    private final MazeRunnerGame game;
    private Skin skin;
    private final BitmapFont orbitronFont;

    public DifficultyScreen(MazeRunnerGame game) {
        this.game = game;
        var camera = new OrthographicCamera();
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport, game.getSpriteBatch());
        skin = game.getSkin();

        orbitronFont = FontManager.getOrbitronFont(24, Color.WHITE); // Load Orbitron font
        skin.add("default-font", orbitronFont); // Set the font in the skin

        // Adjust title and button styles to use Orbitron
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = FontManager.getOrbitronFont(36, Color.WHITE);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = orbitronFont;

        createDifficultySelection(titleStyle, buttonStyle);
    }

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
                game.setDifficulty(0.5f);
                game.setScreen(new MazeSizeScreen(game));
            }
        });
        table.add(easyButton).width(300).padBottom(20).row();

        TextButton mediumButton = new TextButton("Medium", skin);
        mediumButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setDifficulty(0.6f);
                game.setScreen(new MazeSizeScreen(game));
            }
        });
        table.add(mediumButton).width(300).padBottom(20).row();

        TextButton hardButton = new TextButton("Hard", skin);
        hardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setDifficulty(0.7f);
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

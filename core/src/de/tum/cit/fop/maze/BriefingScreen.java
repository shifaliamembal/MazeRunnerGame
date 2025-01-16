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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BriefingScreen implements Screen {
    private final Stage stage;
    private final MazeRunnerGame game;
    private final Texture background;
    private Skin skin;
    private final BitmapFont orbitronFont;

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
                game.setScreen(new GameScreen(game));
            }
        });
        table.add(startButton).width(300).padBottom(20).row();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getSpriteBatch().end();

        stage.act(delta);
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
        background.dispose();
    }

}

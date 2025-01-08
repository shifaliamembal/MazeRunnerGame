package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MenuScreen;
import de.tum.cit.fop.maze.FontManager;

import com.badlogic.gdx.Input.Keys;

public class PauseMenu extends ScreenAdapter {

    private Stage stage;
    private Table table;
    private boolean isMuted = false;
    private final MazeRunnerGame game;
    private final GameScreen gameScreen;
    private final Skin skin;
    private final BitmapFont font;

    public PauseMenu(MazeRunnerGame game, GameScreen gameScreen, Viewport viewport) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(viewport);
        this.skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json"));
        this.font = FontManager.getOrbitronFont(24, Color.WHITE);

        createButtons();
    }

    private void createButtons() {
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        TextButton mainMenuButton = new TextButton("Return to main menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        table = new Table(skin);
        table.setFillParent(true);
        table.add(resumeButton).pad(10);
        table.row();
        table.add(mainMenuButton).pad(10);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            game.setScreen(gameScreen);
        }
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw(); }

    @Override public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override public void dispose() {
        stage.dispose();
    }
}


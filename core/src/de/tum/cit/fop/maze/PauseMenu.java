package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.audio.Music;

public class PauseMenu extends ScreenAdapter {

    private final MazeRunnerGame game;
    private final GameScreen gameScreen;
    private final Stage stage;
    private final Skin skin;
    private final BitmapFont font;
    private Table table;
    private boolean isMuted = false;
    private Music backgroundMusic;

    public PauseMenu(MazeRunnerGame game, GameScreen gameScreen, Viewport viewport) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.stage = new Stage(viewport);
        this.skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json"));
        this.font = FontManager.getOrbitronFont(24, Color.WHITE);
        skin.add("default-font", font);
        this.backgroundMusic = game.getBackgroundMusic();

        createButtons();
        Gdx.input.setInputProcessor(stage); // Ensure input processor is set
    }

    private void createButtons() {
        BitmapFont titleFont = FontManager.getOrbitronFont(36, Color.WHITE);
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label titleLabel = new Label("Paused", titleStyle);

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.getLabel().setFontScale(1.2f);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(gameScreen);
            }
        });

        TextButton mainMenuButton = new TextButton("Return to main menu", skin);
        mainMenuButton.getLabel().setFontScale(1.2f);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        TextButton muteButton = new TextButton("Mute", skin);
        muteButton.getLabel().setFontScale(1.2f);
        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMuted = !isMuted;
                if (isMuted) {
                    backgroundMusic.setVolume(0f);
                    muteButton.setText("Unmute");
                } else {
                    backgroundMusic.setVolume(1f);
                    muteButton.setText("Mute");
                }
            }
        });

        TextButton quitButton = new TextButton("Quit game", skin);
        quitButton.getLabel().setFontScale(1.2f);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table = new Table(skin);
        table.setFillParent(true);
        table.add(titleLabel).padBottom(40).row();
        table.add(resumeButton).uniform().pad(10); // Make all buttons have the same size
        table.row();
        table.add(muteButton).uniform().pad(10);
        table.row();
        table.add(mainMenuButton).uniform().pad(10);
        table.row();
        table.add(quitButton).uniform().pad(10);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        gameScreen.render(delta);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update viewport on resize
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
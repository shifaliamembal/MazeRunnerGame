package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.MenuScreen;
import de.tum.cit.fop.maze.FontManager;

public class PauseMenu {

    private Stage stage;
    private Table table;
    private boolean isMuted = false;
    private final MazeRunnerGame game;
    private final GameScreen gameScreen;

    public PauseMenu(MazeRunnerGame game, GameScreen gameScreen, Viewport viewport) {
        this.game = game;
        this.gameScreen = gameScreen;

        stage = new Stage(viewport);
        table = new Table();
        table.setFillParent(true);

        createButtons();
        Gdx.input.setInputProcessor(stage);
    }

    private void createButtons() {
        FontManager fontManager = game.getFontManager();

        TextButton resumeButton = new TextButton("Resume", fontManager.getTextButtonStyle());
        TextButton mainMenuButton = new TextButton("Return to Main Menu", fontManager.getTextButtonStyle());
        TextButton quitButton = new TextButton("Quit Game", fontManager.getTextButtonStyle());
        TextButton muteButton = new TextButton("Mute: OFF", fontManager.getTextButtonStyle());

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                resumeGame();
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                toggleMute(muteButton);
            }
        });

        table.add(resumeButton).pad(10).row();
        table.add(mainMenuButton).pad(10).row();
        table.add(quitButton).pad(10).row();
        table.add(muteButton).pad(10);

        stage.addActor(table);
    }

    private void resumeGame() {
        gameScreen.setPaused(false);
        Gdx.input.setInputProcessor(gameScreen.getInputProcessor());
    }

    private void toggleMute(TextButton muteButton) {
        isMuted = !isMuted;
        muteButton.setText(isMuted ? "Mute: ON" : "Mute: OFF");

        if (isMuted) {
            game.getAudioManager().muteAllSounds();
        } else {
            game.getAudioManager().unmuteAllSounds();
        }
    }

    public Stage getStage() {
        return stage;
    }
}


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

        //createButtons();
        Gdx.input.setInputProcessor(stage);
    }
}


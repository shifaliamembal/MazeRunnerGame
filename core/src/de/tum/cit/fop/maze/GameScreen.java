package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;

    private Maze maze;
    private Player player;
    private Viewport viewport;
    private List<Maze> levels;
    public static final int tileSize = 64;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        maze = new Maze("maps/test.properties");
        player = new Player(maze);

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        camera.position.set(player.getX(), player.getY(), 0);
        zoom();
        camera.update(); // Update the camera

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) camera.position.x;
        float textY = (float) camera.position.y;

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        // Render the text
        //font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        // Draw the character next to the text :) / We can reuse sinusInput here
        maze.draw(game.getSpriteBatch());
        //System.out.println(player.getSpeed() + " " + delta);
        game.getSpriteBatch().draw(
                player.getCurrentAnimation().getKeyFrame(sinusInput, player.takeInput(delta)),
                camera.position.x - (float) tileSize / 2,
                camera.position.y - (float) tileSize / 2,
                tileSize,
                tileSize * 2
        );
        //font.draw(game.getSpriteBatch(), "O", camera.position.x, camera.position.y);


        game.getSpriteBatch().end(); // Important to call this after drawing everything
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {;
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    public void zoom() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (camera.zoom > 0.25) {
                camera.zoom -= 0.01f;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.zoom += 0.01f;
        }
    }

    // Additional methods and logic can be added as needed for the game screen
}

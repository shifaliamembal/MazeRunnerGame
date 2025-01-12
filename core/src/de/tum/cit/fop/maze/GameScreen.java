package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.util.ArrayList;
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
    private List<Entity> entities;
    public static final int tileSize = 64;
    private OrthographicCamera hudCamera;
    private SpriteBatch hudBatch;
    private ExitPointer pointer;
    private PauseMenu pauseMenu;
    private FontManager fontManager;
    private boolean isPaused;
    private float gameOverTime = 3;
    private ShapeRenderer shapeRenderer;

    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        maze = new Maze("maps/maze.properties", game.getMazeSize());
        player = new Player(maze);
        entities = new ArrayList<Entity>();

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 1f;
        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudBatch = new SpriteBatch();
        hudBatch.setProjectionMatrix(camera.combined);
        hudCamera.position.set(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2, 0);
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        pauseMenu = new PauseMenu(game, this, viewport);

        spawnEntities();
        shapeRenderer = new ShapeRenderer();
    }

    public void spawnEntities() {
        for (var entry : maze.getEntityMap().entrySet()) {
            Point a = new Point(entry.getKey().x, entry.getKey().y + 1);
            Point b = new Point(entry.getKey().x, entry.getKey().y - 1);
            boolean vertical = maze.getMazeMap().containsKey(a) && maze.getMazeMap().get(a) == 0;
            if (vertical && maze.getMazeMap().containsKey(b) && maze.getMazeMap().get(b) != 0) {
                vertical = false;
            }
            if (entry.getValue() == 10) {
                entities.add(new TreasureChest(entry.getKey().x, entry.getKey().y, player));
            }
            else if (entry.getValue() == 11) {
                entities.add(new Enemy(entry.getKey().x, entry.getKey().y, maze, player, game.getDifficulty()));
            } else if (entry.getValue() == 12) {
                entities.add(new ExitBarrier(entry.getKey().x, entry.getKey().y, player, vertical));
                maze.getMazeMap().put(new Point(a.x + (vertical ? 0 : 1), a.y - (vertical ? 0 : 1)), 2);
                maze.getMazeMap().put(entry.getKey(), 2);
                pointer = new ExitPointer(
                        (int) (entry.getKey().x * (tileSize + 1) + (vertical ? 0 : tileSize)),
                        (int) (entry.getKey().y * (tileSize + 1) + (vertical ? tileSize: 0)));
            } else if (entry.getValue() == 13) {
                entities.add(new LaserTrap(entry.getKey().x, entry.getKey().y, player, vertical, game.getDifficulty()));
            } else if (entry.getValue() == 14) {
                entities.add(new SpikeTrap(entry.getKey().x, entry.getKey().y, player, game.getDifficulty()));
            } else if (entry.getValue() == 15) {
                entities.add(new HealthOrb(entry.getKey().x, entry.getKey().y, player));
            }
        }
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (isPaused) {
                game.setScreen(this);
                isPaused = false;
            }
            else {
                game.setScreen(pauseMenu);
                isPaused = true;
            }
        }

        if (!isPaused) {
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

            viewport.setCamera(camera);
            viewport.apply();
            game.getSpriteBatch().begin(); // Important to call this before drawing anything

            // Render the text
            //font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

            // Draw the character next to the text :) / We can reuse sinusInput here

            if (!player.isDead()) {
                maze.draw(game.getSpriteBatch());
                for (Entity e : entities) {
                    e.draw(game.getSpriteBatch(), delta);
                }
            } else {
                game.getSpriteBatch().setColor(Color.RED);
            }

            player.draw(game.getSpriteBatch(), delta);

            game.getSpriteBatch().end();

            viewport.setCamera(hudCamera);
            viewport.apply();

            drawHud(delta);
        }

    }

    public void drawHud(float delta) {
        if (!player.isDead()) {
            hudBatch.begin();
            pointer.draw(hudBatch, camera, viewport);
            font.draw(hudBatch, "Score: " + player.getScore(), 10, Gdx.graphics.getHeight() - 80);
            font.draw(hudBatch, "Time: " + String.format("%.1f", sinusInput), 10, Gdx.graphics.getHeight() - 120);
            hudBatch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(10, Gdx.graphics.getHeight() - 30, 200, 20);
            shapeRenderer.rect(10, Gdx.graphics.getHeight() - 60, 200, 20);
            shapeRenderer.setColor(Color.GREEN);
            float width = ((float) player.getHealth() / player.getMaxHealth() * 200);
            shapeRenderer.rect(10, Gdx.graphics.getHeight() - 30, width, 20);
            shapeRenderer.setColor(Color.YELLOW);
            width = ((float) player.getStamina() / player.getMaxStamina() * 200);
            shapeRenderer.rect(10, Gdx.graphics.getHeight() - 60, width, 20);
            shapeRenderer.end();
        } else {
            gameOverTime -= delta;
            if (gameOverTime < 0) {
                game.setScreen(new GameOverScreen(game));
            }
        }
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
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        player.dispose();
        maze.getTexture().dispose();
        for (Entity e : entities) {
            e.dispose();
        }
        font.dispose();
    }

    public void zoom() {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)) {
            if (camera.zoom > 0.25) {
                camera.zoom -= 0.01f;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.MINUS) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            if (camera.zoom < 2) {
                camera.zoom += 0.01f;
            }
        }
    }

    // Additional methods and logic can be added as needed for the game screen
}

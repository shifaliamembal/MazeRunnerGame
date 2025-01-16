package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;

    private float sinusInput = 0f;

    public static final int tileSize = 64;
    private Maze maze;
    private Player player;
    private Viewport viewport;
    private List<Entity> entities;
    private OrthographicCamera hudCamera;
    private SpriteBatch hudBatch;
    private ExitPointer pointer;
    private PauseMenu pauseMenu;
    private FontManager fontManager;
    private boolean isPaused;
    private float gameOverTime = 3;
    private ShapeRenderer shapeRenderer;
    private int timeLimit = 300;

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
        camera.zoom = 1f;;
        hudBatch = new SpriteBatch();
        viewport = new ExtendViewport(1920, 1080, camera);
        viewport.apply();

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        hudCamera.up.set(0, 1, 0);
        hudCamera.position.set(hudCamera.viewportWidth / 2, -hudCamera.viewportHeight / 2, 0);
        hudCamera.update();
        hudBatch.setProjectionMatrix(hudCamera.combined);

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        pauseMenu = new PauseMenu(game, this, viewport);

        spawnEntities();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
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
                Item.types randomItem;
                if (new Random().nextInt(0, 2) == 0) {
                    randomItem = Item.types.BOOST;
                } else {
                    randomItem = Item.types.BOMB;
                }
                entities.add(new TreasureChest(entry.getKey().x, entry.getKey().y, player, randomItem));
            }
            else if (entry.getValue() == 11) {
                entities.add(new Enemy(entry.getKey().x, entry.getKey().y, maze, player, game.getDifficulty()));
            } else if (entry.getValue() == 12) {
                entities.add(new ExitBarrier(entry.getKey().x, entry.getKey().y, player, vertical));
                maze.getMazeMap().put(new Point(a.x + (vertical ? 0 : 1), a.y - (vertical ? 0 : 1)), 2);
                maze.getMazeMap().put(entry.getKey(), 2);
                pointer = new ExitPointer(
                        (int) (entry.getKey().x * tileSize + (vertical ? 0 : tileSize)),
                        (int) (entry.getKey().y * tileSize + (vertical ? tileSize: 0)));
            } else if (entry.getValue() == 13) {
                entities.add(new LaserTrap(entry.getKey().x, entry.getKey().y, player, vertical, game.getDifficulty()));
            } else if (entry.getValue() == 14) {
                entities.add(new SpikeTrap(entry.getKey().x, entry.getKey().y, player, game.getDifficulty()));
            } else if (entry.getValue() == 15) {
                entities.add(new HealthOrb(entry.getKey().x, entry.getKey().y, player));
            }

        }
        List<TreasureChest> chests = entities.stream().filter(TreasureChest.class::isInstance).map(TreasureChest.class::cast).toList();
        if (!chests.isEmpty()) {
            chests.get(new Random().nextInt(chests.size())).setContent(new Item(Item.types.KEY));
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
                player.stopSound();
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
            if (sinusInput > timeLimit) {
                player.die();
            }

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
            int space = Gdx.graphics.getHeight() / 80;
            hudBatch.begin();
            font.draw(hudBatch, "Score: " + player.getScore(), space, - space * 8);
            font.draw(hudBatch, "Time: " + String.format("%d:%d", (int) (timeLimit - sinusInput) / 60, (int) (timeLimit - sinusInput) % 60), space, - space * 12);
            font.draw(hudBatch, "Items: ", space, - space * 17);
            font.draw(hudBatch, "Key: ", space, - space * 22);
            for (int i = 0; i < player.getInventory().size(); i++) {
                Texture tex = player.getInventory().get(i).getTexture();
                hudBatch.draw(tex, space * 11 + space * (i + 1) + space * 3 * i, - space * 20,
                        space * 3, space * 3);
            }
            if (player.getKey() != null) {
                hudBatch.draw(player.getKey().getTexture(), space * 9, - space * 25,
                        space * 5, space * 3);
            }
            hudBatch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(space, - space * 3, space * 30, space * 2);
            shapeRenderer.rect(space, - space * 6, space * 30, space * 2);
            shapeRenderer.setColor(Color.GREEN);
            float width = ((float) player.getHealth() / player.getMaxHealth() * space * 30);
            shapeRenderer.rect(space, - space * 3, width, space * 2);
            shapeRenderer.setColor(Color.YELLOW);
            width = ((float) player.getStamina() / player.getMaxStamina() * space * 30);
            shapeRenderer.rect(space, - space * 6, width, space * 2);
            shapeRenderer.end();
            hudBatch.begin();
            if (player.getKey() != null) {
                pointer.draw(hudBatch, camera, viewport);
            }
            hudBatch.end();
        } else {
            gameOverTime -= delta;
            if (gameOverTime < 0) {
                game.setScreen(new GameOverScreen(game, player));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hudCamera.setToOrtho(false, width, height);
        hudCamera.position.set(hudCamera.viewportWidth / 2, -hudCamera.viewportHeight / 2, 0);
        hudCamera.update();
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
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
                camera.zoom -= 0.02f;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.MINUS) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            if (camera.zoom < 2) {
                camera.zoom += 0.02f;
            }
        }
    }

    public Player getPlayer(){
        return player;
    }
}

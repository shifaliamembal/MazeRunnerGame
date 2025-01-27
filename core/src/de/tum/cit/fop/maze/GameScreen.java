package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    private boolean isPaused;
    private float gameOverTime = 3;
    private ShapeRenderer shapeRenderer;
    private int timeLimit;
    private Music gameMusic;


    /**
     * Constructor for GameScreen. Sets up the camera and font and initializes the maze.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        maze = new Maze("maps/maze.properties", game.getMazeSize());
        player = new Player(maze);
        entities = new ArrayList<Entity>();

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("MenuSound.mp3"));

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

        font = FontManager.getOrbitronFont(24, Color.WHITE);

        pauseMenu = new PauseMenu(game, this, viewport);

        spawnEntities();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(hudCamera.combined);

        switch (maze.getSize()) {
            case 75 -> timeLimit = 120;
            case 100 -> timeLimit = 180;
            default -> timeLimit = 240;
        }

        game.playGameMusic();
    }

    /**
     * Populates the maze with entities.
     */
    public void spawnEntities() {
        Point keyChest = new Point(0, 0);
        for (var entry : maze.getEntityMap().entrySet()) {
            Point a = new Point(entry.getKey().x, entry.getKey().y + 1);
            Point b = new Point(entry.getKey().x, entry.getKey().y - 1);
            boolean vertical = maze.getMazeMap().containsKey(a) && maze.getMazeMap().get(a) == 0;
            if (vertical && maze.getMazeMap().containsKey(b) && maze.getMazeMap().get(b) != 0) {
                vertical = false;
            }

            switch (entry.getValue()) {
                case 20, 21, 22, 23, 24 -> {
                    Item.types chestItem;
                    switch (entry.getValue()) {
                        case 20 -> chestItem = Item.types.BOOST;
                        case 21 -> chestItem = Item.types.BOMB;
                        case 22 -> chestItem = Item.types.ARROW;
                        case 23 -> chestItem = Item.types.SHIELD;
                        default -> chestItem = Item.types.KEY;
                    }
                    if (chestItem == Item.types.KEY) {
                        keyChest = new Point(entry.getKey().x, entry.getKey().y);
                    }
                    entities.add(new TreasureChest(entry.getKey().x, entry.getKey().y, player, chestItem));
                }
                case 11 ->
                        entities.add(new Enemy(entry.getKey().x, entry.getKey().y, maze, player, game.getDifficulty()));
                case 12 -> {
                    entities.add(new ExitBarrier(entry.getKey().x, entry.getKey().y, player, font, vertical));
                    maze.getMazeMap().put(new Point(a.x + (vertical ? 0 : 1), a.y - (vertical ? 0 : 1)), 2);
                    maze.getMazeMap().put(entry.getKey(), 2);
                    pointer = new ExitPointer(
                            (int) (entry.getKey().x * tileSize + (vertical ? 0 : tileSize)),
                            (int) (entry.getKey().y * tileSize + (vertical ? tileSize : 0))
                    );
                }
                case 13 ->
                        entities.add(new LaserTrap(entry.getKey().x, entry.getKey().y, player, vertical, game.getDifficulty()));
                case 14 ->
                        entities.add(new SpikeTrap(entry.getKey().x, entry.getKey().y, player, game.getDifficulty()));
                case 15 ->
                        entities.add(new HealthOrb(entry.getKey().x, entry.getKey().y, player));
            }

        }

        List<TreasureChest> chests = entities.stream().filter(TreasureChest.class::isInstance).map(TreasureChest.class::cast).toList();
        if (!chests.isEmpty()) {
            for (TreasureChest chest : chests) {
                chest.setKeyLocation(keyChest);
            }
        }
    }


    /**
     * Renders the game. Also serves as the main loop for user input while the game is active.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (isPaused) {
                game.setScreen(this);
                isPaused = false;
                gameMusic.play();
            }
            else {
                game.setScreen(pauseMenu);
                player.stopSound();
                isPaused = true;
                gameMusic.pause();
            }
        }


        if (!isPaused) {
            ScreenUtils.clear(0, 0, 0, 1);

            camera.position.set(player.getX(), player.getY(), 0);
            zoom();
            camera.update();

            sinusInput += delta;
            float textX = (float) camera.position.x;
            float textY = (float) camera.position.y;

            game.getSpriteBatch().setProjectionMatrix(camera.combined);

            viewport.setCamera(camera);
            viewport.apply();
            game.getSpriteBatch().begin();

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

            if (player.victory()) {
                player.addPoints((int) (((timeLimit - sinusInput) / timeLimit) * 2000));
                game.playBackgroundMusic();
                game.setScreen(new VictoryScreen(game, player));
            }

            player.draw(game.getSpriteBatch(), delta);

            game.getSpriteBatch().end();

            viewport.setCamera(hudCamera);
            viewport.apply();

            drawHud(delta);
        }

    }

    /**
     * Renders the HUD.
     * @param delta The time in seconds since the last render.
     */
    public void drawHud(float delta) {
        if (!player.isDead()) {
            int space = Gdx.graphics.getHeight() / 80;
            hudBatch.begin();
            font.draw(hudBatch, "Score: " + player.getScore(), space, - space * 8);
            font.draw(hudBatch, "Time: " + String.format("%d:%02d", (int) (timeLimit - sinusInput) / 60, (int) (timeLimit - sinusInput) % 60), space, - space * 12);
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
            if (pointer != null) {
                pointer.draw(hudBatch, camera, viewport);
            }
            hudBatch.end();
        } else {
            gameOverTime -= delta;
            if (gameOverTime < 0) {
                game.playBackgroundMusic();
                dispose();
                game.setScreen(new GameOverScreen(game, player));
            }
        }
    }

    /**
     * Resizes the game when the window is resized.
     * @param width New screen width.
     * @param height New screen height.
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hudCamera.setToOrtho(false, width, height);
        hudCamera.position.set(hudCamera.viewportWidth / 2, -hudCamera.viewportHeight / 2, 0);
        hudCamera.update();
    }

    /**
     * Pauses the game.
     */
    @Override
    public void pause() {
        isPaused = true;
    }

    /**
     * Resumes the game.
     */
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

    /**
     * Disposes all disposable data.
     */
    @Override
    public void dispose() {
        player.dispose();
        maze.getTexture().dispose();
        for (Entity e : entities) {
            e.dispose();
        }
        font.dispose();
        gameMusic.dispose();
    }

    /**
     * Adjusts camera zoom based on user input.
     */
    public void zoom() {
        if (Gdx.input.isKeyPressed(Input.Keys.I) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ADD)) {
            if (camera.zoom > 0.25) {
                camera.zoom -= 0.02f;
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.O) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_SUBTRACT)) {
            if (camera.zoom < 2) {
                camera.zoom += 0.02f;
            }
        }
    }

    /**
     * Returns the Player attribute.
     * @return The Player.
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Returns the time in minutes that was left at the end of the game.
     * @return The time in string format.
     */
    public String timeLeft() {
        return String.format("%d:%02d", (int) (timeLimit - sinusInput) / 60, (int) (timeLimit - sinusInput) % 60);
    }

    /**
     * Unpauses the game.
     */
    public void unpause() {
        isPaused = false;
    }

}

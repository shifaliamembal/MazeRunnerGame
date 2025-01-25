package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The player character.
 */
public class Player {
    private int x;
    private int y;
    private int dir;
    private int baseSpeed;
    private int speed;
    private Texture texture;
    private List<Animation<TextureRegion>> characterAnimation;
    private Animation<TextureRegion> explosionAnimation;
    private Maze maze;
    private List<Item> inventory;
    private int health;
    private int stamina;
    private boolean running_cooldown;
    private boolean exitOpen;
    private float frameCounter;
    private final int[] DX = {0, 1, 0, -1};
    private final int[] DY = {-1, 0, 1, 0};
    private final int MAX_STAMINA = 400;
    private final int MAX_HEALTH = 100;
    private Sound movementSound;
    private long soundId;
    private boolean isSoundPlaying;
    private int damageEffectFrames;
    private boolean dead;
    private int score;
    private Sound deathSound;
    private float boostDuration;
    private Bomb bomb;
    private Item shield;
    private float shieldTime;
    private Item key;
    private boolean victory;
    private Sound keycardSound;
    private Sound victorySound;
    private Sound boostSound;
    private Sound bombSound;
    private Sound boostUseSound;
    private Sound bombUseSound;

    /**
     * Constructor for Player. Default values are set and the starting position is determined by finding
     * the entrance tile in the maze map.
     * @param maze The maze in which the game takes place.
     */
    public Player(Maze maze) {
        characterAnimation = new ArrayList<>();
        inventory = new ArrayList<>();
        loadAssets();
        this.maze = maze;
        for (var entry : maze.getMazeMap().entrySet()) {
            if (entry.getValue().equals(3)) {
                x = entry.getKey().x * GameScreen.tileSize + GameScreen.tileSize / 2;
                y = entry.getKey().y * GameScreen.tileSize + GameScreen.tileSize / 2;;
            }
        }
        baseSpeed = GameScreen.tileSize * 9;
        stamina = MAX_STAMINA;
        health = MAX_HEALTH;
        damageEffectFrames = 0;
        score = 0;
        boostDuration = 0;
        shieldTime = 0;
        for (int i = 0; i < 5; i++) {
            inventory.add(new Item(Item.types.BOMB));
        }
    }

    /**
     * Renders the player.
      * @param batch The SpriteBatch to render the player in.
     * @param delta The time in seconds since the last render.
     */
    public void draw(SpriteBatch batch, float delta) {
        frameCounter += delta;

        if (bomb != null) {
            bomb.draw(batch, delta);
            if (bomb.isFinished()) {
                bomb = null;
            }
        }

        if (damageEffectFrames > 0) {
            batch.setColor(Color.RED);
            damageEffectFrames--;
        }
        batch.draw(
                getCurrentAnimation().getKeyFrame(frameCounter, !dead && takeInput(delta)),
                x - (float) GameScreen.tileSize / 2,
                y - (float) GameScreen.tileSize / 2,
                GameScreen.tileSize,
                GameScreen.tileSize * 2
        );
        batch.setColor(Color.WHITE);
        if (shield != null) {
            batch.draw(
                    shield.getTexture(),
                    x - (float) GameScreen.tileSize / 2,
                    y - (float) GameScreen.tileSize / 2,
                    GameScreen.tileSize,
                    GameScreen.tileSize * 2);
            shieldTime -= delta;
            if (shieldTime <= 0) {
                shield = null;
            }
        }

    }

    /**
     * Processes user input for movement and using items.
     * @param delta The time in seconds since the last render.
     * @return true if the player is moving, otherwise false.
     */
    public boolean takeInput(float delta) {

        int keyNum = getKeyNumber();
        if (keyNum >= 0 && inventory.size() > keyNum) {
            Item usedItem = inventory.remove(keyNum);
            if (usedItem.getType().equals(Item.types.BOOST)) {
                if (boostDuration < 0) {
                    boostDuration = 0;
                }
                boostDuration += 7;

                if (boostUseSound != null) {
                    boostUseSound.play();
                }
            } else if (usedItem.getType().equals(Item.types.BOMB)) {
                bomb = new Bomb(x / GameScreen.tileSize + DX[dir], y / GameScreen.tileSize + DY[dir], maze, bombUseSound);
            } else if (usedItem.getType().equals(Item.types.SHIELD)) {
                shield = usedItem;
                shieldTime = 7;
            }
        }
        boostDuration -= delta;

        boolean running = false;

        if ((Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))
                && stamina > 0 && !running_cooldown) {
            if (boostDuration < 0) {
                stamina -= 2;
            } else {
                stamina++;
            }
            speed = (int) (baseSpeed * delta * 1.5);
            running = true;
            getCurrentAnimation().setFrameDuration(0.1f);
        } else {
            if (stamina < MAX_STAMINA) {
                stamina++;
            }
            running_cooldown = stamina < 50;
            speed = (int) (baseSpeed * delta);
            getCurrentAnimation().setFrameDuration(0.15f);
        };

        boolean isMoving = true;

        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dir = 0;
            if (!wallCollision(x, y - speed))
                y -= speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dir = 1;
            if (!wallCollision(x + speed, y))
                x += speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dir = 2;
            if (!wallCollision(x, y + speed))
                y += speed;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dir = 3;
            if (!wallCollision(x - speed, y))
                x -= speed;
        } else {
            isMoving = false;
        }

        if (isMoving) {
            if (!isSoundPlaying && !dead) {
                isSoundPlaying = true;
                soundId = movementSound.loop();
            }
        } else {
            if (isSoundPlaying) {
                movementSound.stop();
                isSoundPlaying = false;
            }
        }
        movementSound.setPitch(soundId, running ? 1.8f : 1.2f);

        if (isMoving && x < 0 || y < 0 || x > maze.getSize() * GameScreen.tileSize || y > maze.getSize() * GameScreen.tileSize) {
            movementSound.stop();
            victory = true;
            if (victorySound != null) {
                victorySound.play();
            }
        }
        return isMoving;

    }

    /**
     * Detects whether a player movement would result in a wall collision.
     * @param x The x position to check for collision.
     * @param y The y position to check for collision.
     * @return true if the given coordinates are in a wall or closed gate, otherwise false
     */
    private boolean wallCollision(int x, int y) {
        int tileSize = GameScreen.tileSize;
        Point[] points = {
                new Point(x / tileSize, y / tileSize),
                new Point((x + tileSize / 4) / tileSize, y / tileSize),
                new Point((x - tileSize / 4) / tileSize, y / tileSize),
                new Point(x / tileSize, (y + tileSize / 8) / tileSize),
                new Point(x / tileSize, (y - tileSize / 8) / tileSize)
        };
        return Arrays.stream(points)
                .anyMatch(point -> maze.getMazeMap().containsKey(point)
                && (maze.getMazeMap().get(point) == 0 || (maze.getMazeMap().get(point) == 2 && !exitOpen)));
    }

    /**
     * Checks if any of the number keys 1-9 is pressed and returns the corresponding number.
     * @return The key number.
     */
    private int getKeyNumber() {
        for (int i = 0; i <= 8; i++) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1 + i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Loads all required textures and sounds.
     */
    private void loadAssets() {
        texture = new Texture(Gdx.files.internal("character.png"));
        movementSound = Gdx.audio.newSound(Gdx.files.internal("walk.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("chardefeat.mp3"));
        keycardSound = Gdx.audio.newSound(Gdx.files.internal("collectkey.mp3"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("exitsound.wav"));
        boostSound = Gdx.audio.newSound(Gdx.files.internal("healthboost.mp3"));
        bombSound = Gdx.audio.newSound(Gdx.files.internal("collectbomb.mp3"));
        boostUseSound = Gdx.audio.newSound(Gdx.files.internal("collectsandwich.mp3"));
        bombUseSound = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        Array<TextureRegion> frames;

        for (int i = 0; i < 4; i++) {
            frames = new Array<>(TextureRegion.class);

            for (int col = 0; col < animationFrames; col++) {
                frames.add(new TextureRegion(texture, col * frameWidth, i * frameHeight, frameWidth, frameHeight));
            }

            characterAnimation.add(new Animation<>(0.1f, frames));
        }
    }

    /**
     * Changes the player's health by the given amount. If the health drops to 0, the player dies.
     * @param amount The amount of HP to add.
     */
    public void updateHealth(int amount) {
        if (shield != null && amount < 0) {
            return;
        }
        health += amount;
        if (health > MAX_HEALTH){
            health = MAX_HEALTH;
        }
        else if (health <= 0){
            health = 0;
            movementSound.stop();
            dead = true;

            if (deathSound != null){
                deathSound.play();
            }
        }
        if (amount < 0) {
            damageEffectFrames = 10;
        }
    }

    /**
     * Adds an item to the player's inventory.
     * @param item The item received by the player.
     */
    public void receiveItem(Item item) {
        if (item.getType() == Item.types.KEY) {
            key = item;
            if (keycardSound != null) {
                keycardSound.play();
            }
        } else if (item.getType() == Item.types.BOOST) {
            inventory.add(item);
            if (boostSound != null) {
                boostSound.play();
            }
        } else if (item.getType() == Item.types.BOMB) {
            inventory.add(item);
            if (bombSound != null) {
                    bombSound.play();
            }
        }
        else {
            inventory.add(item);
        }
    }

    /**
     * Returns the inventory list.
     * @return The list of items in the inventory.
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Returns the animation to be played depending on the direction the player is facing.
     * @return The current player animation.
     */
    Animation<TextureRegion> getCurrentAnimation() {
        return characterAnimation.get(dir);
    }

    /**
     * Returns the x position of the player.
     * @return The x position of the player.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y position of the player.
     * @return The y position of the player.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the key.
     * @return The key Item, null if the player does not have a key.
     */
    public Item getKey() {
        return key;
    }

    /**
     * Allows the player to traverse the exit gate and leave the maze.
     */
    public void allowExit() {
        exitOpen = true;
    }

    /**
     * Returns the health points of the player.
     * @return The health points of the layer.
     */
    public int getHealth(){
        return health;
    }

    /**
     * Returns the maximum amount of health the player can have.
     * @return The maximum amount of health the player can have.
     */
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    /**
     * Returns the stamina points of the player.
     * @return The stamina points of the layer.
     */
    public int getStamina() {
        return stamina;
    }

    /**
     * Returns the maximum amount of stamina the player can have.
     * @return The maximum amount of stamina the player can have.
     */
    public int getMaxStamina() {
        return MAX_STAMINA;
    }

    /**
     * Disposes all assets used by the player.
     */
    public void dispose() {
        texture.dispose();
        movementSound.dispose();
        if (deathSound != null){
            deathSound.dispose();
        }
        if (keycardSound != null) {
            keycardSound.dispose();
        }
        if (victorySound != null) {
            victorySound.dispose();
        }
        if (boostSound != null){
            boostSound.dispose();
        }
        if (bombSound != null){
                bombSound.dispose();
        }
        if (boostUseSound != null) {
            boostUseSound.dispose();
        }
        if (bombUseSound != null) {
            bombUseSound.dispose();
        }
    }

    /**
     * Returns whether the player is dead.
     * @return true if the player is dead, otherwise false.
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Kills the player.
     */
    public void die() {
        dead = true;
    }

    /**
     * Gives points to the player.
     * @param points The amount of points to give to the player.
     */
    public void addPoints(int points) {
        score += points;
    }

    /**
     * Returns the current score of the player.
     * @return The amount of points the player has.
     */
    public int getScore() {
        return score;
    }

    /**
     * Stops the player sound.
     */
    public void stopSound() {
        movementSound.stop();
    }

    /**
     * Returns whether the player has won the game.
     * @return true if the player has won the game, otherwise false.
     */
    public boolean victory() {
        return victory;
    }

}

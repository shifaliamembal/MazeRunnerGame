package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    private int x;
    private int y;
    private int dir;
    private int baseSpeed;
    private int speed;
    private Texture texture;
    private List<Animation<TextureRegion>> characterAnimation;
    //private List<VFX> vfx;
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
//    public static class Effect {
//        public int x;
//        public int y;
//        public int speed;
//        public Effect(int x, int y, int speed) {
//            this.x = x;
//            this.y = y;
//            this.speed = speed;
//        }
//    }

    private enum action {
        DOWN, RIGHT, UP, LEFT
    }

    public Player(Maze maze) {
        characterAnimation = new ArrayList<>();
        inventory = new ArrayList<>();
        //vfx = new CopyOnWriteArrayList<>();
        loadCharacterAnimation();
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
    }

    public void draw(SpriteBatch batch, float delta) {
        frameCounter += delta;

        batch.draw(
                getCurrentAnimation().getKeyFrame(frameCounter, takeInput(delta)),
                x - (float) GameScreen.tileSize / 2,
                y - (float) GameScreen.tileSize / 2,
                GameScreen.tileSize,
                GameScreen.tileSize * 2
        );

//        for (VFX v : vfx) {
//            v.draw(batch, delta, x, y);
//            if (v.finished()) {
//                v.dispose();
//                vfx.remove(v);
//            }
//        }

    }

    public boolean takeInput(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && stamina > 0 && !running_cooldown) {
            stamina -= 2;
            speed = (int) (baseSpeed * delta * 1.5);
            getCurrentAnimation().setFrameDuration(0.1f);
        } else {
            if (stamina < MAX_STAMINA) {
                stamina++;
            }
            running_cooldown = stamina < 50;
            speed = (int) (baseSpeed * delta);
            getCurrentAnimation().setFrameDuration(0.15f);
        };

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            dir = 0;
            if (!wallCollision(x, y - speed))
                y -= speed;
            return true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            dir = 1;
            if (!wallCollision(x + speed, y))
                x += speed;
            return true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            dir = 2;
            if (!wallCollision(x, y + speed))
                y += speed;
            return true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            dir = 3;
            if (!wallCollision(x - speed, y))
                x -= speed;
            return true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            maze.getMazeMap().put(new Point(x / GameScreen.tileSize + DX[dir], y / GameScreen.tileSize + DY[dir]), 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            maze.getMazeMap().put(new Point(x / GameScreen.tileSize + DX[dir], y / GameScreen.tileSize + DY[dir]), 0);
        }
        return false;
    }

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


    private void loadCharacterAnimation() {
        texture = new Texture(Gdx.files.internal("character.png"));

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

    public void updateHealth(int amount) {
        health += amount;
        if (health > MAX_HEALTH){
            health = MAX_HEALTH;
        }
        else if (health <= 0){
            health = 0;
            onPlayerDeath();
        }

//        if (amount < 0) {
//            vfx.add(new VFX(VFX.types.BLOOD));
//        }
    }

    public void onPlayerDeath() {
        System.out.println("Player has died!");
    }

    public void receiveItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    Animation<TextureRegion> getCurrentAnimation() {
        return characterAnimation.get(dir);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public Texture getTexture() {
        return texture;
    }

    public void allowExit() {
        exitOpen = true;
    }

    public int getHealth(){
        return health;
    }

}

package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private int x;
    private int y;
    private int dir;
    private int baseSpeed;
    private int speed;
    private List<Animation<TextureRegion>> characterAnimation;
    private Maze maze;


    private enum action {
        DOWN, RIGHT, UP, LEFT
    }

    public Player(Maze maze) {
        characterAnimation = new ArrayList<>();
        loadCharacterAnimation();
        this.maze = maze;
        x = (int) (GameScreen.tileSize * 1.5);
        y = (int) (GameScreen.tileSize * 1.5);
       // for (int i = 0 < maze.)
        baseSpeed = GameScreen.tileSize * 7;
    }

    public boolean takeInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
             speed = (int) (baseSpeed * delta * 1.5);
             getCurrentAnimation().setFrameDuration(0.1f);
        } else {
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
                .anyMatch(point -> maze.getMap().containsKey(point)
                        && maze.getMap().get(point) == 0);
    }


    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;

        for (int i = 0; i < 4; i++) {
            Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

            for (int col = 0; col < animationFrames; col++) {
                walkFrames.add(new TextureRegion(walkSheet, col * frameWidth, i * frameHeight, frameWidth, frameHeight));
            }

            characterAnimation.add(new Animation<>(0.1f, walkFrames));
        }
    }

    List<Animation<TextureRegion>> getAnimation() {
        return characterAnimation;
    }

    Animation<TextureRegion> getCurrentAnimation() {
        return characterAnimation.get(dir);
    }

    int getSpeed() {
        return speed;
    }

    int getX() {
        return x;
    }
    int getY() {
        return y;
    }
}

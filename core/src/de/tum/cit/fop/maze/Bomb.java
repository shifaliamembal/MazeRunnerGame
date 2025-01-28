package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

/**
 * The bomb item which can be found in treasure chests and used by the player to destroy walls and kill enemies.
 */
public class Bomb {
    private final int x, y;
    private float frameCounter;
    private Texture bombTexture;
    private Texture explosionTexture;
    private Animation<TextureRegion> animation;
    private boolean finished;
    private final Maze maze;
    private boolean exploded;
    private boolean soundPlayed;
    private final Sound explosionSound;

    /**
     * Constructor for Bomb. Sets the x and y position where the bomb is places and passes the maze for modification.
     * @param x The x position of the bomb.
     * @param y The y position of the bomb.
     * @param maze The maze in which the game takes place.
     */
    public Bomb(int x, int y, Maze maze, Sound explosionSound) {
        this.x = x;
        this.y = y;
        this.maze = maze;
        this.explosionSound = explosionSound;
        this.soundPlayed = false;
        frameCounter = 0;
        loadAssets();
    }

    /**
     * Renders the bomb.
     */
    public void draw(SpriteBatch batch, float delta) {
        if (frameCounter > 1 && !exploded) {
            exploded = true;
            removeWalls(3);
            frameCounter = 0;

            if (!soundPlayed && explosionSound != null) {
                explosionSound.play();
                soundPlayed = true;
            }
        }
        if (exploded && frameCounter > animation.getAnimationDuration()) {
            finished = true;
            removeWalls(1);
            dispose();
            return;
        }
        frameCounter += delta;
        if (!exploded) {
            batch.setColor(1, 1 - frameCounter, 1 - frameCounter / 2, 1);
            batch.draw(bombTexture, (x + 0.15f) * GameScreen.tileSize, (y + 0.15f) * GameScreen.tileSize,
                    GameScreen.tileSize * 0.7f, GameScreen.tileSize * 0.7f);
            batch.setColor(Color.WHITE);
        } else {
            batch.draw(animation.getKeyFrame(frameCounter, false), (x - 1f) * GameScreen.tileSize, (y - 1f) * GameScreen.tileSize,
                    GameScreen.tileSize * 3, GameScreen.tileSize * 3);
        }
    }

    /**
     * Loads all assets required for the Bomb class.
     */
    private void loadAssets() {
        explosionTexture = new Texture(Gdx.files.internal("explosion.png"));
        bombTexture = new Texture(Gdx.files.internal("bomb.png"));
        Array<TextureRegion> frames = new Array<>(TextureRegion.class);
        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(explosionTexture, (i % 3) * 32, (i / 3) * 32, 32, 32));
        }
        animation = new Animation<>(0.1f, frames);
    }

    /**
     * Removes all walls near the bomb when it explodes.
     * @param type The tile type to replace the walls with.
     */
    private void removeWalls(int type) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Point p = new Point(x + j, y + i);
                if (p.x <= 0 || p.x >= maze.getSize() - 1 || p.y <= 0 || p.y >= maze.getSize() - 1 ) {
                    continue;
                }
                if (maze.getMazeMap().containsKey(p)) {
                    maze.getMazeMap().put(p, type);
                }
            }
        }
    }

    /**
     * Disposes all assets.
     */
    private void dispose() {
        explosionTexture.dispose();
        bombTexture.dispose();
    }

    /**
     * Returns whether the bomb animation has ended.
     * @return true if the bomb has terminated, otherwise false.
     */
    public boolean isFinished() {
        return finished;
    }
}

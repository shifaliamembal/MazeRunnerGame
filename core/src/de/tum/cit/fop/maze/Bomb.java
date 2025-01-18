package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
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
    private int x;
    private int y;
    private float frameCounter;
    private Texture texture;
    private Animation<TextureRegion> animation;
    private boolean finished;
    private Maze maze;

    /**
     * Constructor for Bomb. Sets the x and y position where the bomb is places and passes the maze for modification.
     * @param x The x position of the bomb.
     * @param y The y position of the bomb.
     * @param maze The maze in which the game takes place.
     */
    public Bomb(int x, int y, Maze maze) {
        this.x = x;
        this.y = y;
        this.maze = maze;
        removeWalls(2);
        frameCounter = 0;
        loadAssets();
    }

    /**
     * Renders the bomb.
     */
    public void draw(SpriteBatch batch, float delta) {
        if (frameCounter > animation.getAnimationDuration()) {
            finished = true;
            removeWalls(1);
            dispose();
            return;
        }
        frameCounter += delta;
        batch.draw(animation.getKeyFrame(frameCounter, false), (x - 1f) * GameScreen.tileSize, (y - 1f) * GameScreen.tileSize,
        GameScreen.tileSize * 3, GameScreen.tileSize * 3);
    }

    /**
     * Loads all assets required for the Bomb class.
     */
    private void loadAssets() {
        texture = new Texture(Gdx.files.internal("explosion.png"));
        Array<TextureRegion> frames = new Array<>(TextureRegion.class);
        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(texture, (i % 3) * 32, (i / 3) * 32, 32, 32));
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
        texture.dispose();
    }

    /**
     * Returns whether the bomb animation has ended.
     * @return finished A boolean value which indicates whether the bomb has terminated.
     */
    public boolean isFinished() {
        return finished;
    }
}

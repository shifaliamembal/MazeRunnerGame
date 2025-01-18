package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * The exit barrier that prevents the player from leaving the maze without the key.
 */
public class ExitBarrier extends Entity {
    private boolean active;
    private boolean vertical;

    /**
     * Constructor for the ExitBarrier. Sets its position, references to the maze and player as well as whether it
     * should be displayed vertically or horizontally.
     * @param x The x position of the exit barrier.
     * @param y The y position of the exit barrier.
     * @param player The player with which to interact.
     * @param vertical True if the barrier is vertical, false if it is horizontal.
     */
    public ExitBarrier(int x, int y, Player player, boolean vertical) {
        super(x, y, player);
        this.vertical = vertical;
        active = true;
    }

    public void loadAssets() {
        spriteSheets.add(new Texture(Gdx.files.internal("barrier_idle.png")));
        spriteSheets.add(new Texture(Gdx.files.internal("barrier_deactivate.png")));

        int frameWidth = 32;
        int frameHeight = 64;

        int animationFrames = 18;

        Array<TextureRegion> barrierActive = new Array<>(TextureRegion.class);
        Array<TextureRegion> barrierDeactivate = new Array<>(TextureRegion.class);

        for (int col = 0; col < animationFrames; col++) {
            barrierActive.add(new TextureRegion(spriteSheets.get(0), col * frameWidth, 0, frameWidth, frameHeight));
        }
        for (int col = 0; col < animationFrames; col++) {
            barrierDeactivate.add(new TextureRegion(spriteSheets.get(1), col * frameWidth, 0, frameWidth, frameHeight));
        }

        animations.add(new Animation<>(0.1f, barrierActive));
        animations.add(new Animation<>(1f, barrierDeactivate));
    }

    public void draw(SpriteBatch batch, float delta) {
        frameCounter += delta;

        batch.draw(animations.get(active ? 0 : 1).getKeyFrame(frameCounter, active),
                x - (float) GameScreen.tileSize / 2 + (vertical ? 0 : GameScreen.tileSize * 2),
                y - (float) GameScreen.tileSize / 2,
                0, 0,
                GameScreen.tileSize, GameScreen.tileSize * 2,
                1, 1, vertical ? 0 : 90);

        if (Gdx.input.isKeyPressed(Input.Keys.E) && Math.abs(x - player.getX()) < GameScreen.tileSize && Math.abs(y - player.getY()) < GameScreen.tileSize) {
            if (player.getKey() != null) {
                active = false;
                player.allowExit();
            }
        }
    }
}

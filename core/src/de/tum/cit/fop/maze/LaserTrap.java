package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class LaserTrap extends Entity {
    boolean active;
    boolean vertical;
    private static final int ACTIVATE_INTERVAL = 3;
    private float timeOffset;
    private float damageCooldown;

    public LaserTrap(int x, int y, Player player, boolean vertical) {
        super(x, y, player);
        this.vertical = vertical;
        this.active = true;
        damageCooldown = 0;
        timeOffset = new Random().nextFloat(ACTIVATE_INTERVAL);
    }

    public void loadAssets() {
        spriteSheets.add(new Texture(Gdx.files.internal("laser_activate.png")));
        spriteSheets.add(new Texture(Gdx.files.internal("laser_deactivate.png")));

        int frameWidth = 30;
        int frameHeight = 32;

        Array<TextureRegion> activate = new Array<>(TextureRegion.class);
        Array<TextureRegion> deactivate = new Array<>(TextureRegion.class);

        for (int col = 0; col < 12; col++) {
            activate.add(new TextureRegion(spriteSheets.get(0), col * frameWidth + col * 2, 0, frameWidth, frameHeight));
        }
        for (int col = 0; col < 9; col++) {
            deactivate.add(new TextureRegion(spriteSheets.get(1), col * frameWidth + col * 2, 0, frameWidth, frameHeight));
        }

        animations.add(new Animation<>(0.1f, activate));
        animations.add(new Animation<>(0.1f, deactivate));
    }

    public void draw(SpriteBatch batch, float delta) {
        frameCounter += delta;
        damageCooldown -= delta;

        if (frameCounter < timeOffset) {
            active = false;
        } else if (timeOffset > 0) {
            timeOffset = 0;
            frameCounter = 0;
        }

        if (frameCounter > ACTIVATE_INTERVAL) {
            active = !active;
            frameCounter = 0;
        }

        batch.draw(animations.get(active ? 0 : 1).getKeyFrame(frameCounter, false),
                x - (float) GameScreen.tileSize / 2 + (vertical ? 0 : GameScreen.tileSize),
                y - (float) GameScreen.tileSize / 2,
                0, 0,
                GameScreen.tileSize, GameScreen.tileSize,
                1, 1, vertical ? 0 : 90);

        handlePlayer();
    }

    public void handlePlayer() {
        if (((active && frameCounter > 1) || (!active && frameCounter < 0.5)) && damageCooldown <= 0
                &&  player.getX() / GameScreen.tileSize == x / GameScreen.tileSize
                && player.getY() / GameScreen.tileSize == y / GameScreen.tileSize) {
            player.updateHealth(-2);
            damageCooldown = 0.1f;
        }
    }
}

package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Random;
import java.util.SortedMap;

public class LaserTrap extends Entity {
    boolean active;
    boolean vertical;
    private static final int ACTIVATE_INTERVAL = 3;
    private float timeOffset;
    private float damageCooldown;
    private float difficulty;
    private Sound laserAttack;

    public LaserTrap(int x, int y, Player player, boolean vertical, float difficulty) {
        super(x, y, player);
        this.vertical = vertical;
        this.active = true;
        this.difficulty = difficulty;
        damageCooldown = 0;
        timeOffset = new Random().nextFloat(ACTIVATE_INTERVAL);
        laserAttack = Gdx.audio.newSound(Gdx.files.internal("lasersound.mp3"));

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
            player.updateHealth((int) (-5 * difficulty));
            damageCooldown = 0.1f;
            laserAttack.play();
        }
    }
}

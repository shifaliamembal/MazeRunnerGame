package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class SpikeTrap extends Entity {
    private float timeOffset;
    private float damageCooldown;
    private float difficulty;
    private Sound hitSound;

    public SpikeTrap(int x, int y, Player player, float difficulty) {
        super(x, y, player);
        this.difficulty = difficulty;
        damageCooldown = 0;
        timeOffset = new Random().nextFloat(3);
    }

    public void loadAssets() {
        spriteSheets.add(new Texture(Gdx.files.internal("spike_trap.png")));

        int frameWidth = 32;
        int frameHeight = 32;

        Array<TextureRegion> animation = new Array<>(TextureRegion.class);

        for (int col = 0; col < 14; col++) {
            animation.add(new TextureRegion(spriteSheets.get(0), col * frameWidth, 0, frameWidth, frameHeight));
        }

        animations.add(new Animation<>(0.1f, animation));

        hitSound = Gdx.audio.newSound(Gdx.files.internal("spiketrap.mp3"));

    }

    public void draw(SpriteBatch batch, float delta) {
        damageCooldown -= delta;

        if (timeOffset <= 0) {
            frameCounter += delta;
        } else {
            timeOffset -= delta;
        }

        if (frameCounter > 1.4) {
            frameCounter = 0;
        }

        batch.draw(animations.get(0).getKeyFrame(frameCounter, true),
                x - (float) GameScreen.tileSize / 2,
                y - (float) GameScreen.tileSize / 2,
                0, 0,
                GameScreen.tileSize, GameScreen.tileSize,
                1, 1, 0);

        handlePlayer();
    }

    public void handlePlayer() {
        if (frameCounter > 0.8 && frameCounter < 1.2 && damageCooldown <= 0
                &&  player.getX() / GameScreen.tileSize == x / GameScreen.tileSize
                && player.getY() / GameScreen.tileSize == y / GameScreen.tileSize) {
            player.updateHealth((int) (-20 * difficulty));
            damageCooldown = 1f;

            hitSound.play();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        // Dispose of the sound to free resources
        if (hitSound != null) {
            hitSound.dispose();
        }
    }

}

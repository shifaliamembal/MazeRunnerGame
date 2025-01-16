package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class HealthOrb extends Entity {
    private boolean collected;

    public HealthOrb(int x, int y, Player player) {
        super(x, y, player);
        super.x -= GameScreen.tileSize / 2;
        super.y -= GameScreen.tileSize / 2;
    }

    protected void loadAssets() {
        spriteSheets.add(new Texture(Gdx.files.internal("orb.png")));

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 22; i++) {
            frames.add(new TextureRegion(spriteSheets.get(0), 192 * (i % 5), 192 * (i / 5), 192, 192));
        }
        animations.add(new Animation<>(0.1f, frames));
    }

    public void draw(SpriteBatch batch, float delta) {
        if (collected) {
            return;
        }

        frameCounter += delta;

        if (playerDistance() < GameScreen.tileSize / 2) {
            player.updateHealth(15);
            player.addPoints(25);
            collected = true;
        }

        batch.draw(animations.get(0).getKeyFrame(frameCounter, true), x, y, GameScreen.tileSize, GameScreen.tileSize);
    }
}

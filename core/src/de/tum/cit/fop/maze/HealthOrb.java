package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealthOrb extends Entity {
    private boolean collected;

    public HealthOrb(int x, int y, Player player) {
        super(x, y, player);
        super.x -= GameScreen.tileSize / 4;
        super.y -= GameScreen.tileSize / 4;
    }

    protected void loadAssets() {
        spriteSheets.add(new Texture(Gdx.files.internal("objects.png")));
        textures.add(new TextureRegion(spriteSheets.get(0), 0, 48, 16, 16));
    }

    public void draw(SpriteBatch batch, float delta) {
        if (collected) {
            return;
        }
        if (playerDistance() < GameScreen.tileSize / 2) {
            player.updateHealth(20);
            player.addPoints(25);
            collected = true;
        }
        batch.draw(textures.get(0), x, y, GameScreen.tileSize / 2, GameScreen.tileSize / 2);
    }
}

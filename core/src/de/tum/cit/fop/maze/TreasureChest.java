package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TreasureChest extends Entity {
    public TreasureChest(int x, int y) {
        super(x, y);
    }

    public void loadAssets() {
        Texture walkSheet = new Texture(Gdx.files.internal("objects.png"));

        int frameWidth = 16;
        int frameHeight = 16;

        super.textures.add(new TextureRegion(walkSheet, 0, 0, frameWidth, frameHeight));
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textures.get(0), x - (float) GameScreen.tileSize / 2 + 4, y - (float) GameScreen.tileSize / 2, GameScreen.tileSize, GameScreen.tileSize);
    }
}

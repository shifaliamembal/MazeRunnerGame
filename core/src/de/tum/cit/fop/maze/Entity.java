package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    protected int x;
    protected int y;
    protected List<Animation<TextureRegion>> animations;
    protected List<Texture> spriteSheets;
    protected List<TextureRegion> textures;
    protected Player player;
    protected float frameCounter = 0;

    public Entity(int x, int y, Player player) {
        this.x = x * GameScreen.tileSize + GameScreen.tileSize / 2;
        this.y = y * GameScreen.tileSize + GameScreen.tileSize / 2;
        this.player = player;
        animations = new ArrayList<>();
        spriteSheets = new ArrayList<>();
        textures = new ArrayList<>();
        loadAssets();
    }

    protected abstract void loadAssets();

    public abstract void draw(SpriteBatch batch, float delta);

    public List<Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    protected int playerDistance() {
        return Math.abs(x - player.getX()) + Math.abs(y - player.getY());
    }

    public void dispose() {
        for (Texture t : spriteSheets) {
            t.dispose();
        }
    }
}

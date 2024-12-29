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
    protected List<TextureRegion> textures;

    public Entity(int x, int y) {
        this.x = x * GameScreen.tileSize + GameScreen.tileSize / 2;
        this.y = y * GameScreen.tileSize + GameScreen.tileSize / 2;
        animations = new ArrayList<>();
        textures = new ArrayList<>();
        loadAssets();
    }

    public abstract void loadAssets();

    public abstract void draw(SpriteBatch batch);
}

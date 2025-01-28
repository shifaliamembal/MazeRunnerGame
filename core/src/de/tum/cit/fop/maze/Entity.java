package de.tum.cit.fop.maze;
;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class that serves as the base for enemies and traps.
 */
public abstract class Entity {
    protected int x;
    protected int y;
    protected List<Animation<TextureRegion>> animations;
    protected List<Texture> spriteSheets;
    protected List<TextureRegion> textures;
    protected Player player;
    protected float frameCounter = 0;

    /**
     * Constructor for Entity. Sets the x and y position of the entity and a reference to the player.
     * @param x The x position of the entity.
     * @param y The y position of the entity.
     * @param player The player with which to interact.
     */
    public Entity(int x, int y, Player player) {
        this.x = x * GameScreen.tileSize + GameScreen.tileSize / 2;
        this.y = y * GameScreen.tileSize + GameScreen.tileSize / 2;
        this.player = player;
        animations = new ArrayList<>();
        spriteSheets = new ArrayList<>();
        textures = new ArrayList<>();
        loadAssets();
    }

    /**
     * Loads all requires assets.
     */
    protected abstract void loadAssets();

    /**
     * Renders the entity and serves as a main loop for handling player interaction.
     * @param batch The SpriteBatch on which to render the entity.
     * @param delta The time in seconds since the last render.
     */
    public abstract void draw(SpriteBatch batch, float delta);

    public List<Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    /**
     * Calculates and returns the distance of the entity to the player.
     * @return The distance to the player.
     */
    protected int playerDistance() {
        return Math.abs(x - player.getX()) + Math.abs(y - player.getY());
    }

    /**
     * Disposes all assets.
     */
    public void dispose() {
        for (Texture t : spriteSheets) {
            t.dispose();
        }
    }
}

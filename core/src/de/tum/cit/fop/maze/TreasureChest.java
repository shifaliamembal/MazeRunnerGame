package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.*;

/**
 * A treasure chest that can be opened by the player to receive an item.
 */
public class TreasureChest extends Entity {
    private TextureRegion currentTexture;
    private Item content;
    private boolean open;
    private float itemDisplayTime = 3;
    private Point keyLocation;
    private float keyDirection;

    /**
     * Constructor for TreasureChest. Sets the position, reference to the player and the item contained in the chest.
     * @param x The x position of the treasure chest.
     * @param y The y position of the treasure chest.
     * @param player The player character to interact with.
     * @param item The item to be contained in the chest.
     */
    public TreasureChest(int x, int y, Player player, Item.types item) {
        super(x, y, player);
        content = new Item(item);
    }

    public void loadAssets() {
        spriteSheets.add(new Texture(Gdx.files.internal("objects.png")));

        int frameWidth = 16;
        int frameHeight = 16;

        super.textures.add(new TextureRegion(spriteSheets.get(0), 0, 0, frameWidth, frameHeight));
        super.textures.add(new TextureRegion(spriteSheets.get(0), frameWidth, 0, frameWidth, frameHeight));
        currentTexture = textures.get(0);
    }

    public void draw(SpriteBatch batch, float delta) {
        if (!open && Gdx.input.isKeyPressed(Input.Keys.E) && Math.abs(x - player.getX()) < GameScreen.tileSize && Math.abs(y - player.getY()) < GameScreen.tileSize) {
            open();
        }

        batch.draw(currentTexture, x - (float) GameScreen.tileSize / 2 + 4, y - (float) GameScreen.tileSize / 2, GameScreen.tileSize, GameScreen.tileSize);
        if (open && itemDisplayTime > 0) {
            itemDisplayTime -= delta;
            if (content.getType().equals(Item.types.ARROW)) {
                batch.draw(content.getTextureRegion(), x - (float) GameScreen.tileSize / 3f, y + GameScreen.tileSize,
                        0, 0,
                        GameScreen.tileSize, GameScreen.tileSize, 1, 1, keyDirection);
            } else {
                batch.draw(content.getTexture(), x - (float) GameScreen.tileSize / 3f, y + GameScreen.tileSize, GameScreen.tileSize * 0.7f, GameScreen.tileSize * 0.7f);
            }
        }
    }

    /**
     * Calculates the direction of the arrow item which points to the chest containing the key.
     * @return The rotation value for the arrow item.
     */
    private float getKeyDirection() {
        double deltaX = keyLocation.x - x / GameScreen.tileSize;
        double deltaY = keyLocation.y - y / GameScreen.tileSize;

        return (float) Math.toDegrees(Math.atan2(deltaY, deltaX)) + 90;
    }

    /**
     * Called when the player opens the chest. Given the item contained in the chest and points to the player.
     */
    public void open() {
        currentTexture = textures.get(1);
        if (!content.getType().equals(Item.types.ARROW)) {
            player.receiveItem(content);
        } else {
            keyDirection = getKeyDirection();
        }
        player.addPoints(100);
        open = true;
    }

    /**
     * Sets the item contained in the chest.
     * @param item The item to put in the chest.
     */
    public void setContent(Item item) {
        content = item;
    }

    /**
     * Sets the location of the chest that contains the key, used by the arrow item.
     * @param keyLocation The location of the key.
     */
    public void setKeyLocation(Point keyLocation) {
        this.keyLocation = keyLocation;
    }
}

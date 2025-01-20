package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An item that can be found in treasure chests.
 */
public class Item {
    private Texture texture;
    private TextureRegion textureRegion;
    private types type;

    public enum types {
        KEY, BOOST, BOMB, ARROW, SHIELD
    }

    /**
     * Constructor for Item. Sets its type.
     * @param type The item type.
     */
    Item(types type) {
        this.type = type;

        switch (type) {
            case KEY:
                texture = new Texture("key-card.png");
                break;
            case BOOST:
                texture = new Texture("sandwich.png");
                break;
            case BOMB:
                texture = new Texture("bomb.png");
                break;
            case ARROW:
                texture = new Texture("arrow.png");
                break;
            case SHIELD:
                texture = new Texture("shield.png");
        }
        textureRegion = new TextureRegion(texture);
    }

    /**
     * Returns the type of the item from the types enum.
     * @return The item type.
     */
    public types getType() {
        return type;
    }

    /**
     * Returns the texture of the item for disposal.
     * @return The texture of the item.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Returns the entire texture of the item in the form of a TextureRegion used for rendering.
     * @return The TextureRegion of the item texture.
     */
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}

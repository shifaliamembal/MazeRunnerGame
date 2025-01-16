package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Item {
    private Texture texture;
    private TextureRegion textureRegion;
    private types type;

    public enum types {
        KEY, BOOST, BOMB, ARROW
    }

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
        }
        textureRegion = new TextureRegion(texture);
    }

    public types getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
}

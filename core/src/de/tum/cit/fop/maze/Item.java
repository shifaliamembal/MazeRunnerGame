package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.Texture;

public class Item {
    private Texture texture;
    private types type;

    public enum types {
        KEY
    }

    Item(types type) {
        this.type = type;
        texture = new Texture("key-card.png");
    }

    public types getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }
}

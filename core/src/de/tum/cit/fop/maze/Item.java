package de.tum.cit.fop.maze;

import com.badlogic.gdx.graphics.Texture;

public class Item {
    private Texture texture;
    private types type;

    public enum types {
        KEY, BOOST, BOMB
    }

    Item(types type) {
        this.type = type;

        switch (type) {
            case KEY:
                texture = new Texture("key-card.png");
                break;
            case BOOST:
                texture = new Texture("arrow.png");
                break;
            case BOMB:
                texture = new Texture("bomb.png");
        }
    }

    public types getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }
}

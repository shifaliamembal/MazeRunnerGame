package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TreasureChest extends Entity {
    private TextureRegion currentTexture;
    private Item content;
    private boolean open;
    private float itemDisplayTime = 3;

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
            batch.draw(content.getTexture(), x - (float) GameScreen.tileSize / 2, y + GameScreen.tileSize, GameScreen.tileSize, (float) GameScreen.tileSize / 2);
        }
    }

    public void open() {
        currentTexture = textures.get(1);
        player.receiveItem(content);
        player.addPoints(100);
        open = true;
    }

    public void setContent(Item item) {
        content = item;
    }
}

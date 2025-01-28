package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * An arrow that points to the exit if it is not on the screen.
 */
public class ExitPointer {
    private final int x, y;
    private int rotation;
    private final TextureRegion textureRegion;
    private final Texture texture;

    /**
     * Constructor for ExitPointer. Loads its texture and sets the position of the exit it should point towards.
     * @param x The x position of the exit.
     * @param y The y position of the exit.
     */
    public ExitPointer(int x, int y) {
        texture = new Texture(Gdx.files.internal("arrow.png"));
        textureRegion = new TextureRegion(texture);
        this.x = x;
        this.y = y;
    }

    /**
     * Renders the ExitPointer, with its position and rotation corresponding to the location of the exit.
     * @param batch The SpriteBatch on which to render the ExitPointer.
     * @param camera The game camera.
     * @param viewport The game viewport.
     */
    public void draw(SpriteBatch batch, OrthographicCamera camera, Viewport viewport) {
        float renderX;
        float renderY;

        Vector3 worldPos = new Vector3(x, y, 0);
        Vector3 screenPos = new Vector3();


        screenPos.set(worldPos);
        camera.project(screenPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

        renderX = screenPos.x;
        renderX = Math.max(renderX, texture.getHeight());
        renderX = Math.min(renderX, Gdx.graphics.getWidth() - texture.getWidth());
        renderY = screenPos.y;
        renderY = Math.max(renderY, texture.getHeight());
        renderY = Math.min(renderY, Gdx.graphics.getHeight() - texture.getHeight());
        rotation = (renderX < screenPos.x ? 90 : 0) + (renderX > screenPos.x ? -90 : 0)
                + (renderY < screenPos.y ? 180 : 0);
        if (rotation < 0) {

        }
        int xOffset = 0;
        int yOffset = 0;

        switch (rotation) {
            case -90:
                xOffset = texture.getWidth();
                if (renderY > screenPos.y) {
                    rotation = -45;
                }
                if (renderY == Gdx.graphics.getHeight() - texture.getHeight()) {
                    rotation = 135;
                    yOffset -= texture.getHeight();
                    xOffset += texture.getWidth();
                }
                break;
            case 0:
                yOffset = texture.getHeight();
                break;
            case 90:
                xOffset = -texture.getWidth();
                if (renderY > screenPos.y) {
                    rotation = 45;
                }
                if (renderY >= Gdx.graphics.getHeight() - texture.getHeight() - 1) {
                    rotation = -135;
                    xOffset += texture.getWidth();
                    yOffset -= texture.getHeight();
                }
                break;
            case 180:
                yOffset = -texture.getHeight();
                break;
            case 270:
                xOffset = -texture.getWidth();
                if (renderY > screenPos.y) {
                    rotation = 45;
                }
                if (renderY >= Gdx.graphics.getHeight() - texture.getHeight() - 1) {
                    rotation = 135;
                    yOffset -= texture.getHeight();
                }
                break;
        }

        if (renderX != screenPos.x || renderY != screenPos.y) {
            batch.draw(textureRegion,
                    renderX + xOffset,
                    -Gdx.graphics.getHeight() + renderY + yOffset,
                    texture.getWidth() / 2, texture.getHeight() / 2,
                    texture.getWidth(), texture.getHeight(),
                    4, 4, rotation);
        }

    }

    /**
     * Disposes the texture of the ExitPointer.
     */
    public void dispose() {
        texture.dispose();
    }
}

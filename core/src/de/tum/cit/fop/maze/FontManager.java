package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * The FontManager class is responsible for managing and providing fonts for the game UI.
 * It uses the FreeTypeFontGenerator to create fonts dynamically based on size and color.
 * This class ensures that title and body fonts are created and reused efficiently.
 */
public class FontManager {

    /** Cached font for titles, using the Orbitron font with a large size. */
    private static BitmapFont orbitronTitleFont;

    /** Cached font for body text, using the Orbitron font with a smaller size. */
    private static BitmapFont orbitronBodyFont;

    /**
     * Creates and returns a new Orbitron font with the specified size and color.
     *
     * @param size  The size of the font to be generated.
     * @param color The color of the font to be generated.
     * @return A BitmapFont instance with the specified size and color.
     */
    public static BitmapFont getOrbitronFont(int size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Orbitron-VariableFont_wght.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    /**
     * Returns the cached title font, creating it if it doesn't already exist.
     * The title font uses a large size and white color.
     *
     * @return A BitmapFont instance for titles.
     */
    public static BitmapFont getOrbitronTitleFont() {
        if (orbitronTitleFont == null) {
            orbitronTitleFont = getOrbitronFont(42, Color.WHITE);
        }
        return orbitronTitleFont;
    }

    /**
     * Returns the cached body font, creating it if it doesn't already exist.
     * The body font uses a smaller size and white color.
     *
     * @return A BitmapFont instance for body text.
     */
    public static BitmapFont getOrbitronBodyFont() {
        if (orbitronBodyFont == null) {
            orbitronBodyFont = getOrbitronFont(24, Color.WHITE);
        }
        return orbitronBodyFont;
    }

    /**
     * Disposes of the cached title and body fonts, freeing up resources.
     * Ensures that subsequent calls to getOrbitronTitleFont or getOrbitronBodyFont
     * create new instances.
     */
    public static void dispose() {
        if (orbitronTitleFont != null) {
            orbitronTitleFont.dispose();
            orbitronTitleFont = null;
        }
        if (orbitronBodyFont != null) {
            orbitronBodyFont.dispose();
            orbitronBodyFont = null;
        }
    }
}


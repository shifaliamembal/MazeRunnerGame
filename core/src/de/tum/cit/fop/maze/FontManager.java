package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * A utility class to manage fonts in the game.
 */
public class FontManager {

    // Static reference to store the Orbitron font
    private static BitmapFont orbitronFont;

    /**
     * Loads and returns the Orbitron font with the specified size and color.
     * If the font is already loaded, it returns the existing instance.
     *
     * @param size  The size of the font.
     * @param color The color of the font.
     * @return The generated BitmapFont.
     */
    public static BitmapFont getOrbitronFont(int size, Color color) {
        // Check if the font is already loaded, otherwise load it
        if (orbitronFont == null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Orbitron-VariableFont_wght.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;      // Set the font size
            parameter.color = color;    // Set the font color

            orbitronFont = generator.generateFont(parameter); // Generate the font
            generator.dispose(); // Clean up resources after generating the font
        }
        return orbitronFont;
    }

    /**
     * Frees up the resources used by the font.
     * Call this method during application shutdown.
     */
    public static void dispose() {
        if (orbitronFont != null) {
            orbitronFont.dispose();
            orbitronFont = null;
        }
    }
}

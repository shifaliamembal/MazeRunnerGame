package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontManager {

    private static BitmapFont orbitronTitleFont;
    private static BitmapFont orbitronBodyFont;

    public static BitmapFont getOrbitronFont(int size, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("craft/Orbitron-VariableFont_wght.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    public static BitmapFont getOrbitronTitleFont() {
        if (orbitronTitleFont == null) {
            orbitronTitleFont = getOrbitronFont(42, Color.WHITE);
        }
        return orbitronTitleFont;
    }

    public static BitmapFont getOrbitronBodyFont() {
        if (orbitronBodyFont == null) {
            orbitronBodyFont = getOrbitronFont(24, Color.WHITE);
        }
        return orbitronBodyFont;
    }

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


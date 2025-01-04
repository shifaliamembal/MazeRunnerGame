package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class FontManager {
    private BitmapFont orbitronFont;
    private Skin skin;

    public FontManager() {
        loadOrbitronFont();
        createSkin();
    }

    private void loadOrbitronFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Orbitron-VariableFont_wght.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 32;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;

        orbitronFont = generator.generateFont(parameter);
        generator.dispose();
    }

    private void createSkin(){
        skin = new Skin();
        skin.add("default-font", orbitronFont);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = orbitronFont;
        skin.add("default-label", labelStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = orbitronFont;
        textButtonStyle.fontColor = Color.WHITE;
        skin.add("default-button", textButtonStyle);
    }

    public BitmapFont getOrbitronFont() {
        return orbitronFont;
    }

    public Skin getSkin() {
        return skin;
    }

    public void dispose() {
        orbitronFont.dispose();
        skin.dispose();
    }
}

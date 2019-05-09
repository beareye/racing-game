package com.jajebr.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Rectangle;
import com.jajebr.game.game.world.track.TrackTexture;

/**
 * Contains the content for the game.
 * Should be called init() => isFinished() => load
 */
public class Content {
    // Making this static is bad practice, but it'll do
    private static AssetManager assetManager = new AssetManager();

    public static final int TEXTURE_SIZE = 128;
    public static final int TEXTURES_PER_ROW = 4;

    public static Model formulaStar;
    public static Model inwardCube;
    public static Texture materials;
    public static Texture white;
    public static BitmapFont normalFont;

    public static Model boxModel;

    public static void init() {
        assetManager.load("models/formulastar/formulastar.obj", Model.class);
        assetManager.load("models/inwardCube/inwardCube.obj", Model.class);
        assetManager.load("textures/materials.png", Texture.class);
        assetManager.load("textures/white.png", Texture.class);

        Content.createFonts();

        ModelBuilder modelBuilder = new ModelBuilder();
        boxModel = modelBuilder.createBox(10f, 10f, 10f, new Material(ColorAttribute.createDiffuse(Color.LIME)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    private static void createFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/orbitron-bold.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 2.5f;
        normalFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public static boolean isFinished() {
        return assetManager.update();
    }

    public static void load() {
        formulaStar = assetManager.get("models/formulastar/formulastar.obj", Model.class);
        inwardCube = assetManager.get("models/inwardCube/inwardCube.obj", Model.class);
        materials = assetManager.get("textures/materials.png", Texture.class);
        white = assetManager.get("textures/white.png", Texture.class);
    }

    public static Rectangle getMaterialTexture(TrackTexture trackTexture) {
        int value = trackTexture.getValue();
        int xi = value % TEXTURES_PER_ROW;
        int yi = value / TEXTURES_PER_ROW;
        return new Rectangle(xi * TEXTURE_SIZE, yi * TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    public static Rectangle getMaterialTextureUV(TrackTexture trackTexture) {
        int value = trackTexture.getValue();
        int xi = value % TEXTURES_PER_ROW;
        int yi = value / TEXTURES_PER_ROW;
        float xStep = (float) TEXTURE_SIZE / materials.getWidth();
        float yStep = (float) TEXTURE_SIZE / materials.getHeight();

        return new Rectangle(xi * xStep, yi * yStep, xStep, yStep);
    }

    public static void dispose() {
        boxModel.dispose();
        assetManager.dispose();
    }
}

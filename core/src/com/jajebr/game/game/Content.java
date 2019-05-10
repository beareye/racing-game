package com.jajebr.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

    public static Music jackson;
    public static Music game;
    public static Music night;

    public static Sound finish;
    public static Sound go;
    public static Sound perfect;
    public static Sound ready;
    public static Sound thanks;
    public static Sound toobad;
    public static Sound win;
    public static Sound hover;
    public static Sound boost;
    public static Sound enter;
    public static Sound blip;

    public static void init() {
        assetManager.load("models/formulastar/formulastar.obj", Model.class);
        assetManager.load("models/inwardCube/inwardCube.obj", Model.class);
        assetManager.load("textures/materials.png", Texture.class);
        assetManager.load("textures/white.png", Texture.class);
        assetManager.load("music/JACKSON.ogg", Music.class);
        assetManager.load("music/game.ogg", Music.class);
        assetManager.load("music/night.ogg", Music.class);
        assetManager.load("sounds/smb2/finish.wav", Sound.class);
        assetManager.load("sounds/smb2/go.wav", Sound.class);
        assetManager.load("sounds/smb2/perfect.wav", Sound.class);
        assetManager.load("sounds/smb2/ready.wav", Sound.class);
        assetManager.load("sounds/smb2/thanks.wav", Sound.class);
        assetManager.load("sounds/smb2/toobad.wav", Sound.class);
        assetManager.load("sounds/smb2/win.wav", Sound.class);
        assetManager.load("sounds/hover.wav", Sound.class);
        assetManager.load("sounds/boost.wav", Sound.class);
        assetManager.load("sounds/enter.wav", Sound.class);
        assetManager.load("sounds/blip.wav", Sound.class);

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
        jackson = assetManager.get("music/JACKSON.ogg", Music.class);
        game = assetManager.get("music/game.ogg", Music.class);
        night = assetManager.get("music/night.ogg", Music.class);
        finish = assetManager.get("sounds/smb2/finish.wav", Sound.class);
        go = assetManager.get("sounds/smb2/go.wav", Sound.class);
        perfect = assetManager.get("sounds/smb2/perfect.wav", Sound.class);
        ready = assetManager.get("sounds/smb2/ready.wav", Sound.class);
        thanks = assetManager.get("sounds/smb2/thanks.wav", Sound.class);
        toobad = assetManager.get("sounds/smb2/toobad.wav", Sound.class);
        win = assetManager.get("sounds/smb2/win.wav", Sound.class);
        hover = assetManager.get("sounds/hover.wav", Sound.class);
        enter = assetManager.get("sounds/enter.wav", Sound.class);
        boost = assetManager.get("sounds/boost.wav", Sound.class);
        blip = assetManager.get("sounds/blip.wav", Sound.class);
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

package com.jajebr.game.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Model;

/**
 * Contains the content for the game.
 * Should be called init() => isFinished() => load
 */
public class Content {
    // Making this static is bad practice, but it'll do
    private static AssetManager assetManager = new AssetManager();

    public static Model formulaStar;
    public static BitmapFont debugFont;
    public static Texture testTexture;

    public static void init() {
        assetManager.load("formulastar/formulastar.obj", Model.class);
        assetManager.load("testTexture.png", Texture.class);
        debugFont = new BitmapFont();
    }

    public static boolean isFinished() {
        return assetManager.update();
    }

    public static void load() {
        formulaStar = assetManager.get("formulastar/formulastar.obj", Model.class);
        testTexture = assetManager.get("testTexture.png", Texture.class);
    }

    public static void dispose() {
        assetManager.dispose();
    }
}

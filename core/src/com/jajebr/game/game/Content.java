package com.jajebr.game.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.Bullet;

import javax.swing.text.AttributeSet;

/**
 * Contains the content for the game.
 * Should be called init() => isFinished() => load
 */
public class Content {
    // Making this static is bad practice, but it'll do
    private static AssetManager assetManager = new AssetManager();

    public static Model formulaStar;
    public static Model inwardCube;
    public static BitmapFont debugFont;
    public static Texture testTexture;

    public static Model boxModel;

    public static void init() {
        assetManager.load("formulastar/formulastar.obj", Model.class);
        assetManager.load("inwardCube.obj", Model.class);
        assetManager.load("testTexture.png", Texture.class);
        debugFont = new BitmapFont();

        ModelBuilder modelBuilder = new ModelBuilder();
        boxModel = modelBuilder.createBox(10f, 10f, 10f, new Material(ColorAttribute.createDiffuse(Color.LIME)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    public static boolean isFinished() {
        return assetManager.update();
    }

    public static void load() {
        formulaStar = assetManager.get("formulastar/formulastar.obj", Model.class);
        inwardCube = assetManager.get("inwardCube.obj", Model.class);
        testTexture = assetManager.get("testTexture.png", Texture.class);
    }

    public static void dispose() {
        boxModel.dispose();
        assetManager.dispose();
    }
}

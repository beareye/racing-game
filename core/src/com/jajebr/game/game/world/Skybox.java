package com.jajebr.game.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.jajebr.game.game.Content;

/**
 * The skybox for the world.
 */
public class Skybox extends ModelInstance {
    private Color color;
    private Material skyboxMaterial;
    private float scale;

    /**
     * Returns the color of the skybox.
     * @return the color of the skybox
     */
    public Color getColor() {
        return color;
    }

    /**
     * Initializes a skybox.
     * @param skyboxColor the color of the skybox
     */
    public Skybox(Color skyboxColor, float scale) {
        super(Content.inwardCube);
        this.skyboxMaterial = this.materials.get(0);

        this.changeColor(skyboxColor);
        this.transform.setToScaling(scale, scale, scale);
    }

    public Skybox() {
        this(Color.SKY, 250f);
    }

    /**
     * Changes the color of the skybox.
     * @param newColor the new color
     */
    public void changeColor(Color newColor) {
        skyboxMaterial.set(ColorAttribute.createDiffuse(newColor));
    }
}

package com.jajebr.game.game.world.track;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.utils.BufferUtils;
import com.jajebr.game.game.entity.EntityMotionState;
import com.jajebr.game.game.world.World;

import java.nio.FloatBuffer;

/**
 * A heightmap for the track.
 */
public class TrackHeightmap {
    public static final int GROUND_USERVALUE = -2;

    private FloatBuffer floatBuffer;
    private float[] heightmap;
    private TrackTexture[] textures;
    private boolean[] valid;
    private int width;
    private int height;
    private Vector3 scaling;
    private float minHeight;
    private float maxHeight;

    private TrackCreator trackCreator;

    private btHeightfieldTerrainShape terrainShape;
    private btMotionState motionState;
    private btRigidBody rigidBody;

    public btRigidBody getRigidBody() {
        return rigidBody;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector3 getScaling() {
        return scaling;
    }

    public float[] getHeightmap() {
        return heightmap;
    }

    public TrackTexture[] getTextures() {
        return textures;
    }

    public boolean[] getValidSpace() {
        return valid;
    }

    public btHeightfieldTerrainShape getTerrainShape() {
        return terrainShape;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public TrackCreator getTrackCreator() {
        return trackCreator;
    }

    public TrackHeightmap() {
        width = 64;
        height = 64;
        minHeight = 0f;
        maxHeight = 1f;
        scaling = new Vector3(200f, 500f, 200f);
        trackCreator = new TrackCreator(this);

        createHeightmap();
        createShape();
    }

    public void createHeightmap() {
        heightmap = new float[width * height];
        valid = new boolean[width * height];

        textures = new TrackTexture[width * height];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = TrackTexture.NONE;
        }

        this.trackCreator.create();

        floatBuffer = BufferUtils.newFloatBuffer(heightmap.length);
        floatBuffer.put(heightmap);
    }

    public void createShape() {
        terrainShape = new btHeightfieldTerrainShape(width, height, floatBuffer, 1f, this.minHeight, this.maxHeight, 1, false);
        terrainShape.setLocalScaling(scaling);
        motionState = new btDefaultMotionState();
        rigidBody = new btRigidBody(0f, motionState, terrainShape);

        rigidBody.setUserValue(TrackHeightmap.GROUND_USERVALUE);
    }

    /**
     * Returns whether the heightmap is in bounds or not.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return whether in bounds or not
     */
    public boolean inBounds(int x, int y) {
        return !(x < 0 || x >= this.width || y < 0 || y >= this.height);
    }

    /**
     * Sets a place in the heightmap to a height, and then marks it as valid.
     */
    public void setValidHeight(int x, int y, float height) {
        if (this.inBounds(x, y)) {
            this.heightmap[x + y * this.width] = height;
            this.valid[x + y * this.width] = true;
        }
    }

    /**
     * Sets a piece in the heightmap to a height, and then marks it invalid.
     * @param x the x-coordintae
     * @param y the y-coordinate
     * @param height the height
     */
    public void setInvalidHeight(int x, int y, float height) {
        if (this.inBounds(x, y)) {
            this.heightmap[x + y * this.width] = height;
            this.valid[x + y * this.width] = true;
        }
    }

    /**
     * Sets the texture at the particular point of the heightmap.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param trackTexture the height
     */
    public void setTexture(int x, int y, TrackTexture trackTexture) {
        if (this.inBounds(x, y)) {
            this.textures[x + y * this.width] = trackTexture;
        }
    }

    public TrackTexture getTextureAt(int x, int y) {
        if (!this.inBounds(x, y)) {
            return null;
        }
        return this.textures[x + y * this.getWidth()];
    }

    public void dispose() {
        terrainShape.dispose();
        rigidBody.dispose();
        motionState.dispose();
    }
}

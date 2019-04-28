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
    private int width;
    private int height;
    private Vector3 scaling;
    private float minHeight;
    private float maxHeight;

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

    public btHeightfieldTerrainShape getTerrainShape() {
        return terrainShape;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public TrackHeightmap() {
        width = 32;
        height = 32;
        minHeight = 0f;
        maxHeight = 1f;
        scaling = new Vector3(50f, 50f, 50f);

        createHeightmap();
        createShape();
    }

    public void createHeightmap() {
        heightmap = new float[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                heightmap[x + y * width] = MathUtils.sin(x) / 2 + 0.5f;
            }
        }
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

    public void dispose() {
        terrainShape.dispose();
        rigidBody.dispose();
        motionState.dispose();
    }
}

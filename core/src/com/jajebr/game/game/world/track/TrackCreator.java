package com.jajebr.game.game.world.track;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jajebr.game.engine.Director;

/**
 * Height map operations to create a track.
 */
public class TrackCreator {
    private TrackHeightmap trackHeightmap;
    private TrackTexture trackTexture;

    private float randomSinFactor;
    private float randomCosFactor;
    private float randomHillFactor;

    private Vector2 startingPosition;
    private Vector3 lapDirection;

    private float startingTheta;

    /**
     * Returns the starting position for the car in heightmap coordinates.
     * @return the starting position for the car
     */
    public Vector2 getStartingPosition() {
        return startingPosition;
    }

    /**
     * Returns the direction to pass a lap.
     * @return the lap direction
     */
    public Vector3 getLapDirection() {
        return lapDirection;
    }

    /**
     * Initialize a TrackCreator.
     * @param existingTrackHeightmap the heightmap
     */
    public TrackCreator(TrackHeightmap existingTrackHeightmap) {
        this.trackHeightmap = existingTrackHeightmap;
        this.trackTexture = TrackTexture.SAND;
        if (MathUtils.randomBoolean()) {
            this.trackTexture = TrackTexture.GRASS;
        }
        this.startingPosition = new Vector2();
        this.startingTheta = 0f;
        this.lapDirection = new Vector3();

        this.randomSinFactor = MathUtils.random(1, 5);
        this.randomCosFactor = MathUtils.random(1f, 5f);
        this.randomHillFactor = MathUtils.random(0.25f, 5f);
    }

    /**
     * Modifies the heightmap.
     */
    public void create() {
        // Create the hills of the track
        for (int y = 0; y < trackHeightmap.getHeight(); y++) {
            for (int x = 0; x < trackHeightmap.getWidth(); x++) {
                float longHill = MathUtils.cos( 0.6f * x * this.randomHillFactor) * 0.15f
                        + MathUtils.sin(0.4f * y * this.randomHillFactor) * 0.15f + 0.8f;
                trackHeightmap.setInvalidHeight(x, y, longHill);
                trackHeightmap.setTexture(x, y, this.trackTexture);
            }
        }

        // Create the track as a polar equation.
        float thetaStep = 1f;
        float amplitude = ((this.trackHeightmap.getWidth() / 2f) - 1) / 3.2f;

        int centerX = this.trackHeightmap.getWidth() / 2;
        int centerY = this.trackHeightmap.getHeight() / 2;

        for (float theta = 0f; theta <= 360f; theta += thetaStep) {
            float r = this.evaluatePolarEquation(theta);
            float x = r * MathUtils.cosDeg(theta) * amplitude;
            float y = r * MathUtils.sinDeg(theta) * amplitude;

            int finalX = (int) x + centerX;
            int finalY = (int) y + centerY;

            this.plotWithDiameter(TrackTexture.ROAD, finalX, finalY, 2, 0.2f + MathUtils.cosDeg(0.1f * theta) * 0.1f);
        }

        // Get a random starting position.
        assignStartingPositionAndDirection(centerX, centerY, amplitude);
    }
    public Vector2 getStartingPosition(int playerID) {
        int centerX = this.trackHeightmap.getWidth() / 2;
        int centerY = this.trackHeightmap.getHeight() / 2;
        float amplitude = ((this.trackHeightmap.getWidth() / 2f) - 1) / 3.2f;

        float playerTheta = this.startingTheta - (playerID * 2);
        float r = this.evaluatePolarEquation(playerTheta);
        return this.getPositionFromR(r, playerTheta, centerX, centerY, amplitude);
    }

    private void assignStartingPositionAndDirection(int centerX, int centerY, float amplitude) {
        float theta = 0f;
        while (this.lapDirection.isZero()) {
            theta = MathUtils.random(0f, 360f);
            float r = this.evaluatePolarEquation(theta);
            this.startingPosition.set(this.getPositionFromR(r, theta, centerX, centerY, amplitude));
            this.createLapDirection(theta, centerX, centerY, amplitude);
        }

        this.startingTheta = theta;

        // Plot the checkerboard
        this.plotWithDiameter(TrackTexture.CHECKERBOARD, (int) startingPosition.x, (int) startingPosition.y, 2, 0.2f + MathUtils.cosDeg(0.01f * theta) * 0.1f);
    }

    private Vector2 getPositionFromR(float r, float theta, int centerX, int centerY, float amplitude) {
        float x = r * MathUtils.cosDeg(theta) * amplitude;
        float y = r * MathUtils.sinDeg(theta) * amplitude;

        int finalX = (int) x + centerX;
        int finalY = (int) y + centerY;
        return new Vector2(finalX, finalY);
    }

    private void createLapDirection(float theta, int centerX, int centerY, float amplitude) {
        float nextTheta = (theta + 1) % 360;
        float r = this.evaluatePolarEquation(theta);
        float rNext = this.evaluatePolarEquation(nextTheta);

        Vector2 start = this.getPositionFromR(r, theta, centerX, centerY, amplitude);
        Vector2 end = this.getPositionFromR(rNext, nextTheta, centerX, centerY, amplitude);
        Vector2 displacement = end.cpy().sub(start);
        Vector3 worldDisplacement = new Vector3(displacement.x, 0f, displacement.y);
        worldDisplacement.nor();

        this.lapDirection.set(worldDisplacement);
    }

    private float evaluatePolarEquation(float theta) {
        return 2 - MathUtils.cosDeg(this.randomCosFactor * theta) * MathUtils.sinDeg(this.randomSinFactor * theta);
    }

    private void plotWithDiameter(TrackTexture texture, int x, int y, int diameter, float height) {
        for (int iy = y - diameter; iy <= y + diameter; iy++) {
            for (int ix = x - diameter; ix <= x + diameter; ix++) {
                this.trackHeightmap.setValidHeight(ix, iy, height);
                this.trackHeightmap.setTexture(ix, iy, texture);
            }
        }
    }
}

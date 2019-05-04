package com.jajebr.game.game.world.track;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.Director;

/**
 * Height map operations to create a track.
 */
public class TrackCreator {
    private TrackHeightmap trackHeightmap;
    
    /**
     * Initialize a TrackCreator.
     * @param existingTrackHeightmap the heightmap
     */
    public TrackCreator(TrackHeightmap existingTrackHeightmap) {
        this.trackHeightmap = existingTrackHeightmap;
    }

    /**
     * Modifies the heightmap.
     */
    public void create() {
        // Create the hills of the track
        for (int y = 0; y < trackHeightmap.getHeight(); y++) {
            for (int x = 0; x < trackHeightmap.getWidth(); x++) {
                float longHill = MathUtils.cos( 0.1f * x) * 0.2f + MathUtils.sin(0.1f * y) * 0.2f + 0.6f;
                trackHeightmap.setInvalidHeight(x, y, longHill);
            }
        }

        // Create the track as a polar equation.
        float thetaStep = 1f;
        float amplitude = (this.trackHeightmap.getWidth() / 2f) / 4f;

        int centerX = this.trackHeightmap.getWidth() / 2;
        int centerY = this.trackHeightmap.getHeight() / 2;

        for (float theta = 0f; theta <= 360f; theta += thetaStep) {
            float r = 2 - MathUtils.cosDeg(theta) * MathUtils.sinDeg(3f * theta);
            float x = r * MathUtils.cosDeg(theta) * amplitude;
            float y = r * MathUtils.sinDeg(theta) * amplitude;

            int finalX = (int) x + centerX;
            int finalY = (int) y + centerY;

            this.plotWithDiameter(finalX, finalY, 2, 0.1f);
        }
    }

    private void plotWithDiameter(int x, int y, int diameter, float height) {
        for (int iy = y - diameter; iy <= y + diameter; iy++) {
            for (int ix = x - diameter; ix <= x + diameter; ix++) {
                this.trackHeightmap.setValidHeight(ix, iy, height);
            }
        }
    }
}

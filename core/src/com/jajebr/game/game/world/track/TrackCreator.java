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

    private Array<Vector2> waypoints;

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
                float longHill = MathUtils.sin(0.01f * x * y) * 0.3f + 0.7f;
                trackHeightmap.setInvalidHeight(x, y, longHill);
            }
        }
    }
}

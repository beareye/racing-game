package com.jajebr.game.game.world;

import com.jajebr.game.game.world.track.Track;

/**
 * The game world.
 */
public class World {
    private float gravity;
    private float dragCoefficient;
    private com.jajebr.game.game.world.track.Track track;

    /**
     * Returns the gravity of the world.
     * @return the gravity of the world
     */
    public float getGravity() {
        return gravity;
    }

    /**
     * Returns the drag coefficient associated with the world.
     * @return the drag coefficient of the world
     */
    public float getDragCoefficient() {
        return dragCoefficient;
    }

    /**
     * Returns the track of the world.
     * @return the track
     */
    public com.jajebr.game.game.world.track.Track getTrack() {
        return track;
    }

    public World() {
        gravity = 9.8f;
        dragCoefficient = 0.24f;
        track = new Track(this, "test track");
    }
}

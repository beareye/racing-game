package com.jajebr.game.game.world;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 * Track for the game.
 */
public class Track {
    private String name;

    /**
     * Returns the name of the track.
     * @return the name of the track
     */
    public String getName() {
        return name;
    }

    /**
     * Initializes an empty track.
     * @param newName the name of the track
     */
    public Track(String newName) {
        this.name = newName;
        
    }

    public void draw(ModelBatch modelBatch) {

    }
}

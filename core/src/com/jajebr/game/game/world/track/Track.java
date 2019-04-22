package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.world.World;

/**
 * Track for the game.
 */
public class Track {
    private String name;
    private TrackMeshCreator trackMeshCreator;
    private TrackCollider trackCollider;
    private World world;

    /**
     * Returns the name of the track.
     * @return the name of the track
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the world associated by the track.
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Initializes an empty track.
     * @param newName the name of the track
     */
    public Track(World newWorld, String newName) {
        this.name = newName;
        this.trackMeshCreator = new TrackMeshCreator();
        this.trackCollider = new TrackCollider(this, trackMeshCreator);
        this.world = newWorld;
    }

    public void draw(ModelBatch modelBatch) {
        modelBatch.render(this.trackMeshCreator);
    }

    public boolean testCarCollision(EntityCar car) {
        return trackCollider.testCarCollision(car);
    }
}

package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.world.World;

/**
 * Track for the game.
 */
public class Track {
    private String name;
    private TrackMeshCreator trackMeshCreator;
    private World world;
    private float restitutionCoefficient;

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
     * Returns the restitution coefficient of the track.
     * Any collisions (not on the y-axis) are bounced back, multiplied by this coefficient.
     * @return the restitution coefficient
     */
    public float getRestitutionCoefficient() {
        return restitutionCoefficient;
    }

    /**
     * Initializes an empty track.
     * @param newName the name of the track
     */
    public Track(World newWorld, String newName) {
        this.name = newName;
        this.trackMeshCreator = new TrackMeshCreator();
        this.world = newWorld;
        this.restitutionCoefficient = 0.2f;
    }

    public void draw(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(this.trackMeshCreator, environment);
    }
}

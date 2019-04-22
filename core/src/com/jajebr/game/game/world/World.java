package com.jajebr.game.game.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.jajebr.game.game.world.track.Track;

/**
 * The game world.
 */
public class World {
    private float gravity;
    private float dragCoefficient;
    private float restitutionCoefficient;
    private Environment environment;
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
    public Track getTrack() {
        return track;
    }

    /**
     * Returns the environment of the world.
     * @return the environment of the world
     */
    public Environment getEnvironment() {
        return environment;
    }

    public World() {
        gravity = 9.8f;
        dragCoefficient = 0.24f;
        track = new Track(this, "test track");
        environment = new Environment();
        environment.set(ColorAttribute.createAmbient(0.7f, 0.7f, 0.7f, 1.0f));
        environment.set(ColorAttribute.createDiffuse(0.6f, 0.6f, 0.6f, 1.0f));
        environment.set(ColorAttribute.createSpecular(0.8f, 0.8f, 0.8f, 1.0f));
    }
}

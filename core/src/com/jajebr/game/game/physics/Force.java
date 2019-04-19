package com.jajebr.game.game.physics;

import com.badlogic.gdx.math.Vector3;

/**
 * Describes a force on the object.
 */
public class Force extends Vector3 {
    private String name;

    /**
     * Returns the name of the force.
     * @return the name of the force
     */
    public String getName() {
        return name;
    }

    public Force(String newName, Vector3 force) {
        super(force);
        this.name = newName;
    }

    @Override
    public String toString() {
        return name + ": " + super.toString() + " N";
    }
}

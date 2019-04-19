package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.physics.Force;
import com.jajebr.game.game.world.World;

public class EntityCar extends EntityRenderable {
    private float rotationAngle;

    /**
     * Initializes an EntityCar.
     *
     * @param world the world
     * @param model the model
     */
    public EntityCar(World world, Model model) {
        super(world, model);
        rotationAngle = 0f;
    }

    public void update(float dt) {
        super.update(dt);
        // Hover force counteracts weight
        this.addForce(new Force("Hover", new Vector3(0, getMass() * getWorld().getGravity(), 0)));
        // TODO: remove
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            this.addForce(new Force("Player-Controlled Hover", new Vector3(0, 200, 0)));
        }

        // TODO: remove
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            rotationAngle += 25f * dt;
            this.getRotation().set(Vector3.Y, rotationAngle);
        }
    }

    /**
     * Pulls the camera to the back of the car.
     * @param cam the camera to pull
     */
    public void pullCameraToCar(PerspectiveCamera cam) {
        Vector3 newZ = Vector3.Z.cpy();
        Vector3 newY = Vector3.Y.cpy();
        this.getRotation().transform(newZ);
        this.getRotation().transform(newY);
        Vector3 behind = newZ.cpy().scl(-95f);
        cam.position.set(this.getPosition()).add(behind).add(0f, 50f, 0f);
        cam.direction.set(newZ);
        cam.up.set(newY);
    }
}

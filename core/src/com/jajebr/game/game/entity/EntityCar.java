package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.physics.Force;
import com.jajebr.game.game.world.World;

public class EntityCar extends EntityRenderable {
    private float rotationAngle;
    private float tilt;

    private float TILT_SPEED = 25f;
    private float MAX_TILT_DEGREES = 12f;
    private float ROTATION_SPEED = 75f;

    private float THRUST_FORCE = 500f;
    private float REVERSE_FORCE = 50f;
    private float BRAKE_FORCE = 25f;

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

        // TODO: reorganize
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rotationAngle += ROTATION_SPEED * dt;
            tilt = Math.max(tilt - TILT_SPEED * dt, -MAX_TILT_DEGREES);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rotationAngle -= ROTATION_SPEED * dt;
            tilt = Math.min(tilt + TILT_SPEED * dt, MAX_TILT_DEGREES);
        } else {
            tilt -= tilt * TILT_SPEED * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector3 z = this.getRotation().transform(Vector3.Z.cpy());
            this.addForce(new Force("Thrust", z.cpy().scl(THRUST_FORCE)));
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            Vector3 z = this.getRotation().transform(Vector3.Z.cpy());
            this.addForce(new Force("Reverse", z.cpy().scl(-REVERSE_FORCE)));
        } else {
            Vector3 againstVelocity = getVelocity().cpy().scl(-1).nor();
            this.addForce(new Force("Brake", againstVelocity.scl(BRAKE_FORCE)));
        }

        this.getRotation().setEulerAngles(rotationAngle, 0f, tilt);
    }

    /**
     * Pulls the camera to the back of the car.
     * @param cam the camera to pull
     */
    public void pullCameraToCar(PerspectiveCamera cam) {
        Vector3 newZ = Vector3.Z.cpy();
        this.getRotation().transform(newZ);
        float behindScale = -95f;
        behindScale -= (15f) * Math.min(this.getVelocity().len2(), 3000f) / 3000f;
        Vector3 behind = newZ.cpy().scl(behindScale);
        cam.position.set(this.getPosition()).add(behind).add(0f, 50f, 0f);
        cam.direction.set(newZ);
        cam.up.set(Vector3.Y);
    }
}
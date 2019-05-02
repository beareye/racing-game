package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.world.World;

public class EntityCar extends Entity {
    private float thrustForce;
    private float brakeForce;

    private float turnTorque;
    private float zTilt;
    private boolean tiltLeft;
    private boolean tiltRight;

    private float dragDamping;
    private float angularDamping;

    private float resetTimer;
    private float resetAngle;

    public EntityCar(World world) {
        super(world, Content.formulaStar, 1000f);

        this.getRigidBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        this.dragDamping = 0.4f;
        this.angularDamping = 0.8f;
        this.getRigidBody().setDamping(dragDamping, this.angularDamping);

        // Zero-gravity
        this.getRigidBody().setGravity(new Vector3());

        this.turnTorque = 2500000f;
        this.zTilt = 10000f;

        // TODO: remove
        this.getRigidBody().translate(new Vector3(0f, 100f, 0f));

        thrustForce = 120000f;
        brakeForce = 100000f;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.applyForce(this.getRelativeZ().cpy().scl(thrustForce));
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.applyForce(this.getRelativeZ().cpy().scl(-brakeForce));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.setTiltLeft(true);
            this.setTiltRight(false);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(turnTorque));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.setTiltLeft(false);
            this.setTiltRight(true);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(-turnTorque));
        } else {
            this.setTiltLeft(false);
            this.setTiltRight(false);
        }
    }

    private void setTiltLeft(boolean newTiltLeft) {
        if (!tiltLeft && newTiltLeft) {
            tiltLeft = true;
            // TODO: start tilting vehicle left
        } else if(tiltLeft && !newTiltLeft) {
            tiltLeft = false;
            // TODO: end tilting vehicle left
        }
    }

    private void setTiltRight(boolean newTiltRight) {
        if (!tiltRight && newTiltRight) {
            tiltRight = true;
            // TODO: start tilting vehicle right

        } else if(tiltRight && !newTiltRight) {
            tiltRight = false;
            // TODO: end tilting vehicle right
        }
    }

    private void resetOrientation(float dt) {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

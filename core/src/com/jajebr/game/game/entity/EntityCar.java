package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.world.World;

public class EntityCar extends Entity {
    private float thrustForce;
    private float brakeForce;

    private float turnTorque;
    private boolean tiltLeft;
    private boolean tiltRight;
    private float tiltDeg;
    private float tiltTime;

    private float dragDamping;
    private float angularDamping;

    private float resetThreshold;
    private float desiredAltitude;
    private float resetFeedbackFactor;

    private Quaternion extraRotation;
    private Quaternion extraRotationReverse;

    private ClosestRayResultCallback rayResultCallback;

    public EntityCar(World world) {
        super(world, Content.formulaStar, 1000f);

        this.getRigidBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        this.dragDamping = 0.4f;
        this.angularDamping = 0.8f;
        this.desiredAltitude = 66f;
        this.getRigidBody().setDamping(dragDamping, this.angularDamping);

        // Zero-gravity
        this.getRigidBody().setGravity(new Vector3());

        this.turnTorque = 2500000f;
        this.tiltDeg = 25f;
        this.tiltTime = 0.25f;
        this.resetThreshold = 0.05f;
        this.resetFeedbackFactor = 1.75f;
        this.extraRotation = new Quaternion();
        this.extraRotationReverse = new Quaternion();

        // TODO: remove
        this.getRigidBody().translate(new Vector3(900f, -100f, 0f));

        this.rayResultCallback = new ClosestRayResultCallback(Vector3.Zero, Vector3.Y);

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

            float roll = extraRotation.getRoll();
            float newRoll = roll - this.tiltDeg * dt / this.tiltTime;
            newRoll = Math.max(newRoll, -this.tiltDeg);
            extraRotation.setEulerAngles(0f, 0f, newRoll);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(turnTorque));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.setTiltLeft(false);
            this.setTiltRight(true);

            float roll = extraRotation.getRoll();
            float newRoll = roll + this.tiltDeg * dt / this.tiltTime;
            newRoll = Math.min(newRoll, this.tiltDeg);
            extraRotation.setEulerAngles(0f, 0f, newRoll);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(-turnTorque));
        } else {
            this.setTiltLeft(false);
            this.setTiltRight(false);

            float roll = extraRotation.getRoll();
            float newRoll = roll - roll * dt / this.tiltTime;
            extraRotation.setEulerAngles(0f, 0f, newRoll);
        }

        this.recenterOrientation();
        this.putCarAbove(this.getWorld());
    }

    @Override
    public void render(ModelBatch batch, Environment environment) {
        extraRotationReverse.set(extraRotation).conjugate();
        this.getModelInstance().transform.rotate(extraRotation);
        super.render(batch, environment);
        this.getModelInstance().transform.rotate(extraRotationReverse);
    }

    private void putCarAbove(World world) {
        Vector3 down = this.getPosition().cpy();
        down.y -= 500f;

        this.rayResultCallback.setCollisionObject(null);
        this.rayResultCallback.setClosestHitFraction(1f);
        this.rayResultCallback.setRayFromWorld(this.getPosition());
        this.rayResultCallback.setRayToWorld(down);

        world.getDynamicsWorld().rayTest(this.getPosition(), down, this.rayResultCallback);

        if (this.rayResultCallback.hasHit()) {
            Vector3 point = new Vector3();
            this.rayResultCallback.getHitPointWorld(point);
            float altitude = this.getPosition().y - point.y;

            Vector3 linearVelocity = this.getRigidBody().getLinearVelocity();
            linearVelocity.y = (this.desiredAltitude - altitude) / 2f;
            this.getRigidBody().setLinearVelocity(linearVelocity);
        }
    }

    /**
     * Resets the orientation of the car, so that the x-axis and z-axis are gradually resetted to 0.
     */
    private void recenterOrientation() {
        float pitch = this.getRotationQuaternion().getPitchRad();
        float roll = this.getRotationQuaternion().getRollRad();


        Vector3 angularVelocity = this.getRigidBody().getAngularVelocity();
        // TODO: retune for finer movement (and less jarring motions)
        if (Math.abs(pitch) > this.resetThreshold) {
            angularVelocity.x = -pitch * resetFeedbackFactor;
        }
        if (Math.abs(roll) > this.resetThreshold) {
            angularVelocity.z = -roll * resetFeedbackFactor;
        }
        this.getRigidBody().setAngularVelocity(angularVelocity);
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

    @Override
    public void dispose() {
        super.dispose();
        this.rayResultCallback.dispose();
    }
}

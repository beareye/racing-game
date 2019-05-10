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
import com.jajebr.game.game.player.PlayerInput;
import com.jajebr.game.game.world.World;
import com.jajebr.game.game.world.track.Track;

public class EntityCar extends Entity {
    private float thrustForce;
    private float brakeForce;

    private float turnTorque;
    private float tiltDeg;
    private float tiltTime;

    private float driftDeg;
    private float driftTime;
    private boolean drifting;
    private float maxDriftBoost;
    private float driftBoost;
    private float driftScale;

    private float dragDamping;
    private float angularDamping;

    private float dotWithStartingPosition;

    private float resetThreshold;
    private float desiredAltitude;
    private float resetFeedbackFactor;

    private Quaternion extraRotation;
    private Quaternion extraRotationReverse;

    private ClosestRayResultCallback rayResultCallback;

    private PlayerInput input;
    private boolean active;

    public void setActive(boolean status) {
        active = status;
    }

    public boolean isActive() {
        return active;
    }

    public EntityCar(World world, PlayerInput playerInput) {
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
        this.driftDeg = 42f;
        this.tiltTime = 0.25f;
        this.driftTime = 0.25f;
        this.resetThreshold = 0.05f;
        this.resetFeedbackFactor = 1.75f;
        this.extraRotation = new Quaternion();
        this.extraRotationReverse = new Quaternion();
        this.drifting = false;
        this.maxDriftBoost = 700000f;
        this.driftBoost = 0f;
        this.driftScale = 100000f;
        this.dotWithStartingPosition = Float.NEGATIVE_INFINITY;
        this.active = false;

        this.rayResultCallback = new ClosestRayResultCallback(Vector3.Zero, Vector3.Y);

        this.input = playerInput;

        thrustForce = 400000f;
        brakeForce = 375000f;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (!active) {
            return;
        }

        if (this.input.accelerate) {
            this.applyForce(this.getRelativeZ().cpy().scl(thrustForce));
        } else if (this.input.reverse) {
            this.applyForce(this.getRelativeZ().cpy().scl(-brakeForce));
        }

        if (this.input.leftRight < -this.input.leftRightThreshold) {
            float roll = extraRotation.getRoll();
            float newRoll = roll - this.tiltDeg * dt / this.tiltTime;
            newRoll = Math.max(newRoll, -this.tiltDeg - this.driftDeg);
            extraRotation.setEulerAngles(0f, 0f, newRoll);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(turnTorque));
        } else if (this.input.leftRight > this.input.leftRightThreshold) {
            float roll = extraRotation.getRoll();
            float newRoll = roll + this.tiltDeg * dt / this.tiltTime;
            newRoll = Math.min(newRoll, this.tiltDeg + this.driftDeg);
            extraRotation.setEulerAngles(0f, 0f, newRoll);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(-turnTorque));
        } else {
            float roll = extraRotation.getRoll();
            float newRoll = roll - roll * dt / this.tiltTime;
            extraRotation.setEulerAngles(0f, 0f, newRoll);
        }

        if (this.input.driftingLeft) {
            this.updateDrifting(dt, true, true);

            float roll = extraRotation.getRoll();
            float newRoll = roll - this.driftDeg * dt / this.driftTime;
            newRoll = Math.max(newRoll, -this.driftDeg - this.tiltDeg);
            extraRotation.setEulerAngles(0f, 0f, newRoll);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(turnTorque));
        } else if (this.input.driftingRight) {
            this.updateDrifting(dt, false, true);

            float roll = extraRotation.getRoll();
            float newRoll = roll + this.driftDeg * dt / this.driftTime;
            newRoll = Math.min(newRoll, this.driftDeg + this.tiltDeg);
            extraRotation.setEulerAngles(0f, 0f, newRoll);

            this.getRigidBody().applyTorque(this.getRelativeY().cpy().scl(-turnTorque));
        } else {
            this.updateDrifting(dt, false, false);

            float roll = extraRotation.getRoll();
            float newRoll = roll - roll * dt / this.driftTime;
            extraRotation.setEulerAngles(0f, 0f, newRoll);
        }

        this.recenterOrientation();
        this.putCarAbove(this.getWorld());
    }

    private void updateDrifting(float dt, boolean driftLeft, boolean willDrift) {
        if (this.drifting) {
            if (willDrift) {
                // Continue drifting
                float velocityDot = (this.getRigidBody().getAngularVelocity().len2())
                    * dt * driftScale;
                this.driftBoost += velocityDot;
            } else {
                // Stop drifting
                this.drifting = false;
                float driftBoost = Math.min(this.driftBoost, maxDriftBoost);

                // Apply drift force
                this.getRigidBody().applyCentralImpulse(new Vector3(this.getRelativeZ()).scl(driftBoost));
                this.driftBoost = 0f;
            }
        } else {
            if(willDrift) {
                // Start drifting
                this.drifting = true;
            }
        }
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

        float altitude = this.desiredAltitude + 200;
        if (this.rayResultCallback.hasHit()) {
            Vector3 point = new Vector3();
            this.rayResultCallback.getHitPointWorld(point);
            altitude = this.getPosition().y - point.y;
        } else {
            this.getRigidBody().applyTorque(new Vector3(-10f, 0f, 0f));
        }

        Vector3 linearVelocity = this.getRigidBody().getLinearVelocity();
        linearVelocity.y = (this.desiredAltitude - altitude);
        this.getRigidBody().setLinearVelocity(linearVelocity);
    }

    /**
     * Resets the orientation of the car, so that the x-axis and z-axis are gradually resetted to 0.
     */
    private void recenterOrientation() {
        float pitch = this.getRotationQuaternion().getPitchRad();
        float roll = this.getRotationQuaternion().getRollRad();

        Vector3 angularVelocity = this.getRigidBody().getAngularVelocity();
        // TODO: retune; this systems feedback is unstable
        if (Math.abs(pitch) > this.resetThreshold) {
            angularVelocity.x = -pitch * resetFeedbackFactor;
        }
        if (Math.abs(roll) > this.resetThreshold) {
            angularVelocity.z = -roll * resetFeedbackFactor;
        }
        this.getRigidBody().setAngularVelocity(angularVelocity);
    }

    public boolean isRecentering() {
        float pitch = this.getRotationQuaternion().getPitchRad();
        float roll = this.getRotationQuaternion().getRollRad();

        return Math.abs(pitch) > this.resetThreshold || Math.abs(roll) > this.resetThreshold;
    }

    /**
     * Tests if the car passed through the goal.
     * @param track the track
     * @return -1 if passing backwards, 0 if no passing was made, 1 if passing through
     */
    public int testIfPassedGoal(Track track) {
        Vector3 startingPosition = track.getStartingPosition();
        float distanceSquaredToStart = startingPosition.dst2(this.getPosition());
        Vector3 difference = new Vector3(this.getPosition()).sub(startingPosition);
        float newDotStartingPosition = difference.dot(track.getTrackMesh().getTrackHeightmap().getTrackCreator().getLapDirection());

        int returnValue = 0;
        if (distanceSquaredToStart <= track.getAccceptableDistanceSquaredToGoal()) {
            if (this.dotWithStartingPosition < 0f && newDotStartingPosition >= 0f) {
                returnValue = 1;
            } else if (this.dotWithStartingPosition > 0f && newDotStartingPosition <= 0f) {
                returnValue = -1;
            }
        }
        this.dotWithStartingPosition = newDotStartingPosition;

        return returnValue;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.rayResultCallback.dispose();
    }
}

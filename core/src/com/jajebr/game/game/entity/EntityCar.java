package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.RayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDefaultVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleRaycaster;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.world.World;

public class EntityCar extends Entity {
    private float thrustForce;
    private float brakeForce;

    private float friction;

    public EntityCar(World world) {
        super(world, Content.formulaStar, 1000f);
        this.friction = 0.05f;

        this.getRigidBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        // Zero-gravity
        this.getRigidBody().setGravity(new Vector3());

        thrustForce = 10000f;
        brakeForce = 5000f;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        this.recenter(dt);
    }

    public void recenter(float dt) {

        this.getRigidBody().setAngularVelocity(new Vector3(0, 0, 60f * dt));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

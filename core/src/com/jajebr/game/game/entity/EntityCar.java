package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.world.World;

public class EntityCar extends Entity {
    private float thrustForce;
    private float brakeForce;

    private float dragDamping;

    public EntityCar(World world) {
        super(world, Content.formulaStar, 1000f);

        this.getRigidBody().setActivationState(Collision.DISABLE_DEACTIVATION);

        this.dragDamping = 0.4f;
        this.getRigidBody().setDamping(dragDamping, 0f);

        // Zero-gravity
        this.getRigidBody().setGravity(new Vector3());

        // TODO: remove
        this.getRigidBody().translate(new Vector3(0f, 100f, 0f));

        thrustForce = 100000f;
        brakeForce = 50000f;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.applyForce(this.getFront().cpy().scl(thrustForce));
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.applyForce(this.getFront().cpy().scl(-brakeForce));
        }

        Vector3 currentAngularVelocity = this.getRigidBody().getAngularVelocity();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.getRigidBody().setAngularVelocity(new Vector3(0f, 2.5f, 0f));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.getRigidBody().setAngularVelocity(new Vector3(0f, -2.5f, 0f));
        } else {
            this.getRigidBody().setAngularVelocity(currentAngularVelocity.sub(currentAngularVelocity.cpy().scl(dt * 5f)));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

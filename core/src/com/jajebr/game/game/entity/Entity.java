package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.physics.Force;
import com.jajebr.game.game.world.World;

/**
 * An entity in the world.
 * Should be subclassed.
 */
public abstract class Entity {
    private Array<Force> forces;
    private float mass;
    private World world;

    private Vector3 velocity;
    private Vector3 position;
    private Vector3 displacement;
    private Vector3 nextAttemptedPosition;
    private Quaternion rotation;

    /**
     * Returns the mass of the object.
     * @return the mass of the object
     */
    public float getMass() {
        return mass;
    }

    /**
     * Returns the velocity of the entity.
     * @return the velocity of the entity
     */
    public Vector3 getVelocity() {
        return velocity;
    }

    /**
     * Returns the position of the entity.
     * @return the position of the entity
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Returns the next attempted position of the entity.
     * @return the next attempted position
     */
    public Vector3 getNextAttemptedPosition() {
        return nextAttemptedPosition;
    }

    /**
     * Returns the displacement from the current position to the next attempted position.
     * @return the next attempted displacement
     */
    public Vector3 getDisplacement() {
        return displacement;
    }

    /**
     * Returns the quaternion that holds the rotation for the entity.
     * @return the rotation of the entity
     */
    public Quaternion getRotation() {
        return rotation;
    }

    /**
     * Returns the world that the entity is on
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Sets the mass of the entity.
     * @param newMass the new mass of the entity
     * @return the entity, for chaining
     */
    public Entity setMass(float newMass) {
        this.mass = newMass;
        return this;
    }

    /**
     * Initializes a blank entity.
     */
    public Entity(World newWorld) {
        forces = new Array<Force>();
        mass = 1f;
        velocity = new Vector3();
        position = new Vector3();
        nextAttemptedPosition = new Vector3();
        displacement = new Vector3();
        rotation = new Quaternion();
        world = newWorld;
    }

    /**
     * Called upon updating the entity.
     * Applies motion ot the entity. Should not be overridden; use
     * @param dt the delta time for update
     */
    public final void updateInWorld(float dt) {
        forces.clear();
        this.update(dt);
    }

    /**
     * Applies the motion onto the entity in the world.
     * Called after applying updates + collisions.
     * @param dt the delta time for update
     */
    public void applyMotion(float dt) {
        // Compute the net force.
        Force sumForce = new Force("Net force", new Vector3());
        for (Force force : forces) {
            sumForce.add(force);
        }

        // TODO: consider Verlet integration? This is Modified Euler

        // Compute the net acceleration.
        Vector3 accel = new Vector3(sumForce);
        accel.scl(1 / mass);

        Vector3 initialVelocity = velocity.cpy();
        velocity.add(accel.scl(dt));

        Vector3 averageVelocity = new Vector3();
        averageVelocity.add(initialVelocity);
        averageVelocity.add(velocity);
        averageVelocity.scl(0.5f);

        // Integrate for displacement
        displacement.setZero();
        displacement.add(averageVelocity.scl(dt));
        // Accel is already scaled by dt, we need to scale it again
        displacement.add(accel.scl(dt / 2));

        // Don't add the displacement to the position yet; calculate the next attempted position for collisions.
        nextAttemptedPosition.set(displacement).add(position);
    }

    /**
     * Finalizes the motion that was calculated by applyMotion.
     */
    public void finalizeMotion() {
        this.position.set(this.nextAttemptedPosition);
    }

    /**
     * Updates the entity.
     * Should be overridden.
     * @param dt the delta time for update
     */
    public void update(float dt) {
        // Apply the weight force.
        this.addForce(new Force("Weight", new Vector3(0, this.mass * -world.getGravity() , 0)));
        // TODO: possibly make drag proportional to area covered?
        Vector3 velocitySquared = velocity.cpy();
        velocitySquared.scl(-0.5f * world.getDragCoefficient() * 5f);
        this.addForce(new Force("Drag", velocitySquared));
    }

    /**
     * Adds a new force onto the entity.
     * @param newForce the new force
     */
    public void addForce(Force newForce) {
        this.forces.add(newForce);
    }

    /**
     * Renders the entity.
     *
     * @param modelBatch the model batch to render with
     */
    public void render(ModelBatch modelBatch) {

    }

    /**
     * Draws a free body of the entity.
     * Assumes the shapeRenderer has already began in Filled mode.
     * @param shapeRenderer
     * @param spriteBatch
     */
    public void drawFreeBody(Camera cam, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        ShapeRenderer.ShapeType existingShapeType = shapeRenderer.getCurrentType();

        float largestMagnitude = 1f;
        float largestLength = 50f;
        for (Force force : forces) {
            largestMagnitude = Math.max(largestMagnitude, force.len2());
        }
        float scale = largestLength / largestMagnitude;
        Vector3 end = new Vector3();

        shapeRenderer.setColor(Color.RED);
        // Origin for full-body
        shapeRenderer.box(
                this.position.x - 0.5f,
                this.position.y - 0.5f,
                this.position.z + 0.5f,
                1f,
                1f,
                1f
        );

        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Force force : forces) {
            end.set(force).nor().scl(force.len2() * scale).add(this.position);
            shapeRenderer.setColor(Color.ORANGE);
            shapeRenderer.line(
                    this.position.x,
                    this.position.y,
                    this.position.z,
                    end.x,
                    end.y,
                    end.z
            );
        }

        shapeRenderer.end();

        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();
            Vector3 screenSpace = new Vector3();
            GlyphLayout glyphLayout = new GlyphLayout();
            for (Force force : forces) {
                glyphLayout.setText(Content.debugFont, force.getName());
                end.set(force).nor().scl(force.len2() * scale).add(this.position);
                screenSpace.set(end);
                screenSpace = cam.project(screenSpace);
                Content.debugFont.draw(
                        spriteBatch,
                        force.getName(),
                        screenSpace.x - glyphLayout.width / 2f,
                        screenSpace.y - glyphLayout.height / 2f
                );
            }
        spriteBatch.end();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(existingShapeType);
    }
}

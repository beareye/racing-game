package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.SWIGTYPE_p_f_p_q_const__btCollisionShape_p_q_const__btCollisionShape__bool;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.physics.Force;
import com.jajebr.game.game.world.World;

/**
 * An entity in the world, defined by a model.
 * Should be subclassed.
 */
public abstract class Entity {
    private ModelInstance modelInstance;
    private float mass;
    private World world;

    private EntityMotionState entityMotionState;

    private btCollisionShape shape;
    private btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    private btRigidBody rigidBody;
    private Vector3 localInertia;

    /**
     * Returns the mass of the object.
     * @return the mass of the object
     */
    public float getMass() {
        return mass;
    }

    /**
     * Returns the model instance of the entity.
     * @return the model instance of the entity
     */
    public ModelInstance getModelInstance() {
        return this.modelInstance;
    }

    /**
     * Returns the rigid body associated with the entity.
     * @return the rigid body of the entity
     */
    public btRigidBody getRigidBody() {
        return rigidBody;
    }

    public Entity(World newWorld, Model model, float newMass) {
        this.modelInstance = new ModelInstance(model);
        this.entityMotionState = new EntityMotionState(this);
        this.world = newWorld;

        createShape();

        this.mass = newMass;
        this.localInertia = new Vector3();
        if (this.mass > 0f) {
            this.shape.calculateLocalInertia(this.mass, this.localInertia);
        }
        this.constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(
                this.mass,
                entityMotionState,
                shape,
                localInertia
        );

        this.rigidBody = new btRigidBody(constructionInfo);
        this.rigidBody.proceedToTransform(this.modelInstance.transform);
        this.rigidBody.setCollisionFlags(
                this.rigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK
        );
        this.rigidBody.setUserValue(this.world.getEntityIndex());

        world.addEntity(this);
    }

    /**
     * Creates the shape associated with Bullet.
     * By default, calculates the AABB.
     */
    protected void createShape() {
        // Calculate a bounding box of the model.
        BoundingBox box = new BoundingBox();
        modelInstance.calculateBoundingBox(box);

        Vector3 dimensions = new Vector3();
        box.getDimensions(dimensions);
        // Defining a box in Bullet => half of the dimensions must be specified
        dimensions.scl(0.5f);
        // Create an AABB about the model
        shape = new btBoxShape(dimensions);
    }

    /**
     * Updates the entity.
     * @param dt the delta-time
     */
    public void update(float dt) {

    }

    /**
     * Renders the entity.
     * @param modelBatch the model batch
     * @param environment the environment
     */
    public void render(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(this.modelInstance, environment);
    }

    /**
     * Disposes the entity.
     */
    public void dispose() {
        rigidBody.dispose();
        constructionInfo.dispose();
        shape.dispose();
    }
}

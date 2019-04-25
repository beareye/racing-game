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
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;
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

    private Vector3 position;
    private Quaternion rotationQuaternion;
    private Vector3 front;

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

    /**
     * Returns the position of the entity.
     * Modifying this vector will <b>not</b> modify the position of the entity.
     * @return the position of the entity
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Returns the rotation of the entity.
     * Modifying this quaternion will <b>not</b> modify the position of the entity.
     * @return the rotation of the entity
     */
    public Quaternion getRotationQuaternion() {
        return rotationQuaternion;
    }

    /**
     * Returns the world of the entity.
     * @return the world of the entity
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the entity motion state.
     * @return the motion state of the enetity
     */
    public EntityMotionState getEntityMotionState() {
        return this.entityMotionState;
    }

    /**
     * Returns a vector that points out of the entity.
     * @return the front vector that points out of the entity's direction
     */
    public Vector3 getFront() {
        return front;
    }

    public Entity(World newWorld, Model model, float newMass) {
        this.world = newWorld;
        this.modelInstance = new ModelInstance(model);
        this.entityMotionState = new EntityMotionState(this);
        this.mass = newMass;
        this.position = new Vector3();
        this.rotationQuaternion = new Quaternion();
        this.front = new Vector3();

        rotationQuaternion = new Quaternion();

        createShape();

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

        // TODO: change if we want to test interactions in the ground
        this.rigidBody.setContactCallbackFlag(Constants.ENTITY_BITFLAG);
        this.rigidBody.setContactCallbackFilter(Constants.ENTITY_BITFLAG);

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
        this.modelInstance.transform.getRotation(rotationQuaternion);
        this.modelInstance.transform.getTranslation(position);

        this.front.set(Vector3.Z);
        this.rotationQuaternion.transform(this.front);
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
     * Applies a force to the center of the rigid body.
     * @param force the force
     */
    public void applyForce(Vector3 force) {
        this.getRigidBody().applyCentralForce(force);
    }

    /**
     * Pulls the camera behind the entity.
     * @param cam the camera
     */
    public void pullCameraBehind(Camera cam) {
        cam.position.set(Vector3.Z).scl(-200f).add(this.getPosition());
        cam.direction.set(Vector3.Z);
        cam.up.set(Vector3.Y);
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

package com.jajebr.game.game.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Utilities;
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

    private Vector3 relativeX;
    private Vector3 relativeY;
    private Vector3 relativeZ;

    private Vector3 maxAngularVelocity;

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
     * Returns the X-axis relative to the object
     * @return the x-axis in object space
     */
    public Vector3 getRelativeX() {
        return relativeX;
    }

    /**
     * Returns the Y-axis relative to the object.
     * @return the y-axis in object space
     */
    public Vector3 getRelativeY() {
        return relativeY;
    }

    /**
     * Returns a vector that points out of the entity.
     * @return the front vector that points out of the entity's direction
     */
    public Vector3 getRelativeZ() {
        return relativeZ;
    }

    public Entity(World newWorld, Model model, float newMass) {
        this.world = newWorld;
        this.modelInstance = new ModelInstance(model);
        this.entityMotionState = new EntityMotionState(this);
        this.mass = newMass;
        this.position = new Vector3();
        this.rotationQuaternion = new Quaternion();
        this.relativeX = new Vector3();
        this.relativeY = new Vector3();
        this.relativeZ = new Vector3();

        this.maxAngularVelocity = new Vector3(5f, 5f, 5f);

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

        this.rigidBody.setUserValue(this.world.getNextEntityIndex());

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

        this.relativeX.set(Vector3.X);
        this.relativeY.set(Vector3.Y);
        this.relativeZ.set(Vector3.Z);
        this.rotationQuaternion.transform(this.relativeX);
        this.rotationQuaternion.transform(this.relativeY);
        this.rotationQuaternion.transform(this.relativeZ);
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
     * TODO: move to a different class
     * @param cam the camera
     */
    public void pullCameraBehind(Camera cam) {
        Vector3 relativeZ = new Vector3(this.relativeZ);
        relativeZ.y = 0f;
        relativeZ.nor();

        float pullback = 150f;
        float yPullback = 66f;
        float t = this.getRigidBody().getLinearVelocity().len2() / 10000;
        t = MathUtils.clamp(t, 0, 1);
        pullback += Utilities.quadraticEasing(0f, 100f, t);
        cam.position.set(relativeZ).scl(-pullback).add(this.getPosition());
        cam.position.add(0f, yPullback, 0f);
        cam.direction.set(relativeZ);
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

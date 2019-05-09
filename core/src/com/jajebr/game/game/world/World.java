package com.jajebr.game.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Timer;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.entity.Entity;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.player.Player;
import com.jajebr.game.game.world.track.Track;

/**
 * The game world.
 */
public class World {
    private float gravity;
    private float dragCoefficient;
    private Environment environment;
    private Track track;
    private Skybox skybox;
    private boolean nighttime;
    private Array<Player> rankings;
    private ObjectSet<Player> finishedPlayers;

    private Array<Entity> entities;

    private WorldContactListener worldContactListener;

    private btCollisionConfiguration collisionConfiguration;
    private btCollisionDispatcher collisionDispatcher;
    private btDbvtBroadphase broadphase;
    private btConstraintSolver constraintSolver;
    private btDiscreteDynamicsWorld dynamicsWorld;

    private DebugDrawer debugDrawer;

    private DirectionalLight sun;
    private Timer sunTimer;
    private float sunTime;

    /**
     * Returns the gravity of the world.
     * @return the gravity of the world
     */
    public float getGravity() {
        return gravity;
    }

    /**
     * Returns the drag coefficient associated with the world.
     * @return the drag coefficient of the world
     */
    public float getDragCoefficient() {
        return dragCoefficient;
    }

    /**
     * Returns the track of the world.
     * @return the track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Returns the environment of the world.
     * @return the environment of the world
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Returns the world used by Bullet.
     * @return the Bullet world
     */
    public btDiscreteDynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }

    public Array<Player> getRankings() {
        return rankings;
    }

    public World() {
        gravity = 9.8f;
        dragCoefficient = 0.5f;
        track = new Track(this, "test track");
        nighttime = MathUtils.randomBoolean();

        entities = new Array<Entity>();

        // Basic lights
        environment = new Environment();
        if (nighttime) {
            environment.set(ColorAttribute.createAmbient(0.6f, 0.6f, 0.6f, 1.0f));
        } else {
            environment.set(ColorAttribute.createAmbient(1f, 1f, 1f, 1.0f));
        }
        environment.set(ColorAttribute.createDiffuse(0.7f, 0.7f, 0.7f, 1.0f));
        environment.set(ColorAttribute.createSpecular(0.8f, 0.8f, 0.8f, 1.0f));

        skybox = new Skybox(nighttime);

        collisionConfiguration = new btDefaultCollisionConfiguration();
        collisionDispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(collisionDispatcher, broadphase, constraintSolver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3(0, -this.getGravity(), 0f));

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        dynamicsWorld.setDebugDrawer(debugDrawer);

        this.sun = new DirectionalLight();
        this.sun.setColor(Color.WHITE);
        this.sunTimer = new Timer(true);
        this.environment.add(this.sun);
        this.sunTime = 10f;

        worldContactListener = new WorldContactListener(this);

        addTrackBody();

        this.rankings = new Array<Player>();
        this.finishedPlayers = new ObjectSet<Player>();
    }

    public void addFinishedPlayer(Player player) {
        this.finishedPlayers.add(player);
    }

    public boolean finished(int numPlayers) {
        return this.finishedPlayers.size == numPlayers;
    }

    /**
     * Adds an entity into the world.
     * @param entity the entity to add
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
        dynamicsWorld.addRigidBody(entity.getRigidBody());
    }

    public void addTrackBody() {
        dynamicsWorld.addRigidBody(
                track.getTrackMesh().getTrackHeightmap().getRigidBody(),
                Constants.TRACK_BITFLAG,
                Constants.ALL_BITFLAG
        );
    }

    /**
     * Returns an index of the entity in the entity array.
     */
    public int getNextEntityIndex() {
        return entities.size;
    }

    /**
     * Returns the entity with the index.
     * @param index the index of the entity
     * @return the entity with the index
     */
    public Entity getEntityWithIndex(int index) {
        return entities.get(index);
    }

    /**
     * Updates all entities in the world.
     * @param dt the delta-time
     */
    public void update(float dt) {
        for (Entity entity : entities) {
            entity.update(dt);
        }

        sunTimer.update(dt);

        sun.setDirection(
                MathUtils.cos(sunTimer.getTimeElapsed() / sunTime),
                -1f,
                MathUtils.sin(sunTimer.getTimeElapsed() / sunTime)
        );

        dynamicsWorld.stepSimulation(dt, Constants.PHYSICS_MAX_SUBSTEP, Constants.PHYSICS_TIMESTEP);
    }

    /**
     * Renders the world and all of its entities.
     * @param modelBatch the model batch
     */
    public void render(ModelBatch modelBatch) {
        modelBatch.render(skybox);

        if (!Gdx.input.isKeyPressed(Input.Keys.O) && entities.size > 0) {
            entities.get(0).pullCameraBehind(modelBatch.getCamera());
        }
        track.draw(modelBatch, this.environment);
        for (Entity entity : entities) {
            entity.getRigidBody().getWorldTransform(entity.getModelInstance().transform);
            entity.render(modelBatch, this.environment);
        }

        if (Director.DEBUG) {
            debugDrawer.begin(modelBatch.getCamera());
                dynamicsWorld.debugDrawWorld();
            debugDrawer.end();
        }
    }

    public int assignRankingToPlayer(Player player) {
        if (!this.rankings.contains(player, true)) {
            this.rankings.add(player);
            return this.rankings.size;
        }
        return -1;
    }

    /**
     * Disposes all entities and the world itself.
     */
    public void dispose() {
        for (Entity entity : entities) {
            entity.dispose();
        }

        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        collisionDispatcher.dispose();
        collisionConfiguration.dispose();
        worldContactListener.dispose();
        debugDrawer.dispose();

        track.dispose();
    }
}

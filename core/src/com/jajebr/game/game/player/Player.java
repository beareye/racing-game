package com.jajebr.game.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Timer;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.world.World;

public class Player {
    private EntityCar car;
    private World world;
    private int id;
    private int playerCount;

    private PerspectiveCamera cam;
    private PlayerViewport viewport;
    private PlayerHUD playerHUD;
    private PlayerInput playerInput;

    private int lapCount;
    private Timer lapTimer;

    /**
     * Returns the ID of the player.
     * e.g. 0 => P1, 1 => P2, ...
     * @return the player ID
     */
    public int getID() {
        return id;
    }

    public int getLapCount() {
        return lapCount;
    }

    public PlayerViewport getViewport() {
        return viewport;
    }

    public EntityCar getCar() {
        return car;
    }

    public Timer getLapTimer() {
        return lapTimer;
    }

    public World getWorld() {
        return world;
    }

    public PlayerInput getPlayerInput() {
        return playerInput;
    }

    public Player(int newId, int numPlayers, World existingWorld) {
        this.id = newId;
        this.playerCount = numPlayers;
        this.world = existingWorld;

        this.initializeCamera();
        this.playerHUD = new PlayerHUD(this);
        this.playerInput = new PlayerInput(this);

        this.lapCount = 0;
        this.lapTimer = new Timer(false);
        this.car = new EntityCar(this.world, this.playerInput);
        this.assignCarToStartingPosition();
    }

    public void update(float dt) {
        this.lapTimer.update(dt);
        int lapsPassed = car.testIfPassedGoal(world.getTrack());
        if (lapsPassed > 0) {
            this.lapTimer.reset();
        }
        this.lapCount += lapsPassed;
    }

    public void draw(ModelBatch modelBatch) {
        viewport.apply();

        car.pullCameraBehind(this.cam);

        cam.update();
        modelBatch.begin(this.cam);
            world.render(modelBatch);
        modelBatch.end();


    }

    public void drawHUD(SpriteBatch spriteBatch) {
        playerHUD.draw(spriteBatch);
    }

    private void initializeCamera() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.near = 1f;
        cam.far = 50000f;
        cam.update();

        viewport = new PlayerViewport(this.id, this.playerCount, cam);
        viewport.update();
    }

    private void assignCarToStartingPosition() {
        Vector3 startingPosition = this.world.getTrack().getStartingPosition();
        Vector3 lapDirection = this.world.getTrack().getTrackMesh().getTrackHeightmap().getTrackCreator().getLapDirection();

        Vector3 newPosition = new Vector3(lapDirection).scl(-1f).add(startingPosition);
        Matrix4 transform = this.car.getRigidBody().getCenterOfMassTransform();
        transform.idt();
        transform.setToRotation(lapDirection, 0f);
        Director.log(lapDirection.toString());
        this.car.getRigidBody().setCenterOfMassTransform(transform);
        this.car.getRigidBody().translate(newPosition);
        this.lapTimer.activate();
        this.lapCount = 0;
    }

    public void resize(int w, int h) {
        viewport.update();
        cam.viewportWidth = w;
        cam.viewportHeight = h;
        cam.update();

        playerHUD.resize();
    }
}

package com.jajebr.game.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Timer;
import com.jajebr.game.game.Content;
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

    private Vector2 minimapPosition;

    /*
    private ModelInstance test;
    private ModelInstance test2;
    */

    private Timer beginTimer;
    private Timer elapsedTimer;
    private boolean retired;

    private int rank;

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
    public Timer getElapsedTimer() {
        return elapsedTimer;
    }
    public Timer getBeginTimer() {
        return beginTimer;
    }

    public World getWorld() {
        return world;
    }

    public PlayerInput getPlayerInput() {
        return playerInput;
    }

    public Vector2 getMinimapPosition() {
        return minimapPosition;
    }

    public boolean isRetired() {
        return retired;
    }

    public int getRank() {
        return rank;
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
        this.beginTimer = new Timer(true);
        this.elapsedTimer = new Timer(false);
        this.car = new EntityCar(this.world, this.playerInput);
        this.assignCarToStartingPosition();
        this.minimapPosition = new Vector2();
        this.rank = -1;

        /*
        test = new ModelInstance(Content.boxModel);
        test2 = new ModelInstance(Content.boxModel);
        */
    }

    public boolean finished() {
        return this.lapCount > this.getWorld().getTrack().getAmountOfLaps();
    }

    private void finish() {
        this.car.setActive(false);
        this.elapsedTimer.deactivate();
        this.rank = this.world.assignRankingToPlayer(this);
        this.world.addFinishedPlayer(this);
    }

    public void update(float dt) {
        this.lapTimer.update(dt);
        this.elapsedTimer.update(dt);
        this.beginTimer.update(dt);
        if (this.beginTimer.isActive() && this.beginTimer.getTimeElapsed() >= Constants.COUNTDOWN) {
            this.beginTimer.deactivate();
            this.car.setActive(true);
            this.lapTimer.activate();
            this.elapsedTimer.activate();
            this.lapCount = 0;
        }

        if (this.car.isActive() && this.car.getPosition().y < -300f) {
            this.retired = true;
            this.car.setActive(false);
            this.world.addFinishedPlayer(this);
        }

        int lapsPassed = car.testIfPassedGoal(world.getTrack());
        this.lapCount += lapsPassed;
        if (lapsPassed > 0) {
            this.lapTimer.reset();

            if (this.finished()) {
                this.finish();
            }
        }

        this.world.getTrack().getTrackMesh().getCoordinatesFromVertex(this.minimapPosition, this.car.getPosition());
    }

    public void draw(ModelBatch modelBatch) {
        viewport.apply();

        car.pullCameraBehind(this.cam);

        cam.update();
        modelBatch.begin(this.cam);
            world.render(modelBatch);
            /*
            Vector3 testPos = this.world.getTrack().getStartingPosition().cpy().add(0f, 100f, 0f);
            test.transform.setToTranslation(testPos);
            test.transform.scale(5f, 5f, 5f);
            modelBatch.render(test);
            Vector3 test2Pos = this.world.getTrack().getTrackMesh().getTrackHeightmap().getTrackCreator().getLapDirection().cpy().scl(100).add(testPos);
            test2.transform.setToTranslation(
                    test2Pos
            );
            modelBatch.render(test2);
            */
        modelBatch.end();
    }

    public void drawHUD(SpriteBatch spriteBatch, Array<Player> players) {
        playerHUD.draw(spriteBatch, players);
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
        //Vector3 startingPosition = this.world.getTrack().getStartingPosition();
        Vector2 startingCoords = this.world.getTrack().getTrackHeightmap().getTrackCreator().getStartingPosition(id);
        Vector3 startingPosition = this.world.getTrack().getStartingPositionFrom2DCoordinates(startingCoords);
        Vector3 lapDirection = this.world.getTrack().getTrackMesh().getTrackHeightmap().getTrackCreator().getLapDirection();

        Vector3 newPosition = new Vector3(lapDirection).scl(-10f).add(startingPosition);

        this.car.getRigidBody().translate(newPosition);

        Matrix4 transform = this.car.getRigidBody().getCenterOfMassTransform();
        float angle = MathUtils.atan2(lapDirection.x, lapDirection.z);
        Quaternion quaternion = new Quaternion(
                0f,
                MathUtils.sin(angle / 2),
                0,
                MathUtils.cos(angle / 2)
        );
        transform.rotate(quaternion);
        this.car.getRigidBody().setCenterOfMassTransform(transform);

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

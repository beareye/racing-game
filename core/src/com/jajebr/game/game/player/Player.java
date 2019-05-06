package com.jajebr.game.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.world.World;

public class Player {
    private EntityCar car;
    private World world;
    private int id;

    private PerspectiveCamera cam;
    private ScreenViewport viewport;
    private Rectangle screenRectangleFactor;

    private int lapCount;

    /**
     * Returns the ID of the player.
     * e.g. 1 => P1, 2 => P2, ...
     * @return the player ID
     */
    public int getID() {
        return id;
    }

    public Player(int newId, Rectangle screenRect, World existingWorld) {
        this.id = newId;
        this.world = existingWorld;
        this.screenRectangleFactor = screenRect;

        this.initializeCamera();

        this.lapCount = 0;
        this.car = new EntityCar(this.world);
        this.assignCarToStartingPosition();
    }

    public void update(float dt) {

    }

    public void draw(ModelBatch modelBatch) {
        viewport.apply();

        car.pullCameraBehind(this.cam);

        cam.update();
        modelBatch.begin(this.cam);
            world.render(modelBatch);
        modelBatch.end();
    }

    private void initializeCamera() {
        float width = this.screenRectangleFactor.width * Gdx.graphics.getWidth();
        float height = this.screenRectangleFactor.height * Gdx.graphics.getHeight();
        cam = new PerspectiveCamera(67, width, height);
        cam.near = 1f;
        cam.far = 10000f;
        cam.update();

        viewport = new ScreenViewport(cam);
        viewport.update((int) width, (int) height);
    }

    private void assignCarToStartingPosition() {
        Vector3 startingPosition = this.world.getTrack().getStartingPosition();
        Vector3 lapDirection = this.world.getTrack().getTrackMesh().getTrackHeightmap().getTrackCreator().getLapDirection();

        Vector3 newPosition = new Vector3(lapDirection).scl(-5f).add(startingPosition);
        this.car.getRigidBody().translate(newPosition);
    }

    public void resize(int w, int h) {
        float width = this.screenRectangleFactor.width * w;
        float height = this.screenRectangleFactor.height * h;
        viewport.update((int) width, (int) height);
        cam.viewportWidth = width;
        cam.viewportHeight = height;
        cam.update();
    }
}

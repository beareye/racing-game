package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.world.World;
import com.jajebr.game.game.world.track.TrackHeightmap;

public class HeightMapTestScreen extends Screen {
    private OrthographicCamera cam;
    private World world;
    private SpecialShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    public HeightMapTestScreen() {
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer = new SpecialShapeRenderer();
        spriteBatch = new SpriteBatch();
        world = new World();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.position.x -= 100 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.position.x += 100 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.position.y += 100 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.position.y -= 100 * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            cam.zoom -= dt;
            cam.zoom = Math.max(cam.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            cam.zoom += dt;
        }
    }

    @Override
    public void draw(float alpha) {
        cam.update();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        TrackHeightmap heightmap = this.world.getTrack().getTrackMesh().getTrackHeightmap();
            for (int y = 0; y < heightmap.getHeight(); y++) {
                for (int x = 0; x < heightmap.getWidth(); x++) {
                    float height = heightmap.getHeightmap()[x + y * heightmap.getWidth()];
                    shapeRenderer.setColor(new Color(height, height, height, 0f));
                    shapeRenderer.rect(x * 2, y * 2, 2, 2);
                }
            }
        shapeRenderer.end();
    }

    @Override
    public void resize(int w, int h) {
        cam.viewportWidth = w;
        cam.viewportHeight = h;
        cam.update();
    }

    @Override
    public void dispose() {
        world.dispose();
        shapeRenderer.dispose();
        spriteBatch.dispose();
    }
}

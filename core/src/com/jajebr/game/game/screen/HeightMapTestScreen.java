package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.engine.screen.Screen;

public class HeightMapTestScreen extends Screen {
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private SpecialShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private FirstPersonCameraController cameraInputController;

    public HeightMapTestScreen() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20, 20, 20);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 10000f;
        cam.update();

        modelBatch = new ModelBatch();
        shapeRenderer = new SpecialShapeRenderer();
        spriteBatch = new SpriteBatch();

        cameraInputController = new FirstPersonCameraController(cam);
        Gdx.input.setInputProcessor(cameraInputController);
    }

    @Override
    public void update(float dt) {
        cameraInputController.update();
    }

    @Override
    public void draw(float alpha) {
        cam.update();

        modelBatch.begin(cam);

        modelBatch.end();
    }

    @Override
    public void resize(int w, int h) {
        cam.viewportWidth = w;
        cam.viewportHeight = h;
        cam.update();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}

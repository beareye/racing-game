package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.world.World;

public class MainGameScreen extends Screen {
    private PerspectiveCamera cam;
    private ScreenViewport screenViewport;

    private ModelBatch modelBatch;
    private SpecialShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private EntityCar car;

    private World world;

    private CameraInputController cameraInputController;

    public MainGameScreen() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20, 20, 20);
        cam.up.set(0f, 1f, 0f);
        cam.near = 0.1f;
        cam.far = 10000f;
        cam.update();
        screenViewport = new ScreenViewport(cam);

        modelBatch = new ModelBatch();
        shapeRenderer = new SpecialShapeRenderer();
        spriteBatch = new SpriteBatch();

        world = new World();

        car = new EntityCar(world);
        world.getEnvironment().add(new DirectionalLight().set(0.7f, 0.7f, 0.7f, -1.0f, -0.8f, 0.2f));
        world.getEnvironment().add(new PointLight().set(0.4f, 0.8f, 0.4f, 100f, 100f, 100f, 100f));

        cameraInputController = new CameraInputController(cam);
        cameraInputController.translateUnits = 100f;
        Gdx.input.setInputProcessor(cameraInputController);
    }

    @Override
    public void update(float dt) {
        cameraInputController.update();
        world.update(dt);
    }

    @Override
    public void draw(float alpha) {
        cam.update();

        modelBatch.begin(cam);
            world.render(modelBatch);
        modelBatch.end();

        Gdx.graphics.setTitle(Constants.APP_ID + " [FPS: " + Gdx.graphics.getFramesPerSecond() + "]");
    }

    @Override
    public void resize(int w, int h) {
        screenViewport.update(w, h);
        cam.viewportWidth = w;
        cam.viewportHeight = h;
        cam.update();
    }

    @Override
    public void dispose() {
        world.dispose();
        modelBatch.dispose();
    }
}

package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.entity.Entity;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.entity.EntityGround;
import com.jajebr.game.game.world.World;

public class MainGameScreen extends Screen {
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private SpecialShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private EntityCar car;

    private World world;

    private FirstPersonCameraController cameraInputController;

    public MainGameScreen() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20, 20, 20);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 10000f;
        cam.update();

        modelBatch = new ModelBatch();
        shapeRenderer = new SpecialShapeRenderer();
        spriteBatch = new SpriteBatch();

        world = new World();

        car = new EntityCar(world);
        world.addEntity(new EntityGround(world));
        world.getEnvironment().add(new DirectionalLight().set(0.7f, 0.7f, 0.7f, -1.0f, -0.8f, 0.2f));
        world.getEnvironment().add(new PointLight().set(0.4f, 0.8f, 0.4f, 100f, 100f, 100f, 100f));

        cameraInputController = new FirstPersonCameraController(cam);
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
        modelBatch.dispose();
    }
}

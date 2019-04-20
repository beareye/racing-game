package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.entity.Entity;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.entity.EntityRenderable;
import com.jajebr.game.game.world.World;

public class MainGameScreen extends Screen {
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private SpecialShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;

    private Array<Entity> entities;
    private EntityCar car;

    private World world;

    public MainGameScreen() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(20, 20, 20);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 1000f;
        cam.update();

        modelBatch = new ModelBatch();
        shapeRenderer = new SpecialShapeRenderer();
        spriteBatch = new SpriteBatch();

        world = new World();

        entities = new Array<Entity>();
        car = new EntityCar(world, Content.formulaStar);
        entities.add(car);
    }

    @Override
    public void update(float dt) {
        for (Entity entity : entities) {
            entity.updateInWorld(dt);
            // TODO: change so that we apply motion after checking for collisions
            entity.applyMotion(dt);

        }

        // TODO: remove
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            car.pullCameraToCar(cam);
        }
    }

    @Override
    public void draw(float alpha) {
        cam.update();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.line(Vector3.Zero, new Vector3(200, 0, 0));
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.line(Vector3.Zero, new Vector3(0, 200, 0));
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.line(Vector3.Zero, new Vector3(0, 0, 200));
        shapeRenderer.end();

        modelBatch.begin(cam);
        modelBatch.setCamera(cam);
            for (Entity entity : entities) {
                entity.render(modelBatch);
            }
        modelBatch.end();

        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GOLD);
            car.renderDebug(shapeRenderer);
            car.drawFreeBody(cam, shapeRenderer, spriteBatch);
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
        modelBatch.dispose();
    }
}

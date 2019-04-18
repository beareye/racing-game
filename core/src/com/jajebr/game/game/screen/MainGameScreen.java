package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.jajebr.game.engine.Screen;

public class MainGameScreen extends Screen {
    private PerspectiveCamera cam;
    private ModelBatch modelBatch;
    private Model testModel;
    private ModelInstance testModelInstance;
    private CameraInputController cameraInputController;

    public MainGameScreen() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(100, 100, 100);
        cam.lookAt(0, 0, 0);
        cam.near = 0.1f;
        cam.far = 1000f;
        cam.update();

        modelBatch = new ModelBatch();

        ObjLoader objLoader = new ObjLoader();
        testModel = objLoader.loadModel(Gdx.files.internal("formulastar/formulastar.obj"));
        testModelInstance = new ModelInstance(testModel);

        cameraInputController = new CameraInputController(cam);
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
        modelBatch.setCamera(cam);
            modelBatch.render(testModelInstance);
        modelBatch.end();
    }
}

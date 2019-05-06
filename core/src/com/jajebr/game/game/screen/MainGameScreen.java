package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.player.Player;
import com.jajebr.game.game.world.World;

public class MainGameScreen extends Screen {
    private ModelBatch modelBatch;

    private EntityCar car;

    private World world;
    private Player player;

    public MainGameScreen() {
        modelBatch = new ModelBatch();

        world = new World();
        world.getEnvironment().add(new DirectionalLight().set(0.7f, 0.7f, 0.7f, -1.0f, -0.8f, 0.2f));
        world.getEnvironment().add(new PointLight().set(0.4f, 0.8f, 0.4f, 100f, 100f, 100f, 100f));

        player = new Player(1, new Rectangle(0f, 0f, 1f, 1f), world);
    }

    @Override
    public void update(float dt) {
        player.update(dt);
        world.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F10)) {
            Director.changeScreen(new HeightMapTestScreen());
        }
    }

    @Override
    public void draw(float alpha) {
        player.draw(modelBatch);

        Gdx.graphics.setTitle(Constants.APP_ID + " [FPS: " + Gdx.graphics.getFramesPerSecond() + "]");
    }

    @Override
    public void resize(int w, int h) {
        player.resize(w, h);
    }

    @Override
    public void dispose() {
        world.dispose();
        modelBatch.dispose();
    }
}

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
import com.badlogic.gdx.utils.Array;
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
    private SpriteBatch spriteBatch;

    private EntityCar car;

    private World world;
    private Array<Player> players;

    public MainGameScreen(int numPlayers) {
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();

        world = new World();
        world.getEnvironment().add(new DirectionalLight().set(0.7f, 0.7f, 0.7f, -1.0f, -0.8f, 0.2f));
        world.getEnvironment().add(new PointLight().set(0.4f, 0.8f, 0.4f, 100f, 100f, 100f, 100f));

        this.players = new Array<Player>();
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i, numPlayers, this.world);
            players.add(player);
            Director.getPlayerInputController().assignControllerToPlayer(player);
        }
    }

    @Override
    public void update(float dt) {
        for (Player player : players) {
            player.update(dt);
        }
        world.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F10)) {
            Director.changeScreen(new HeightMapTestScreen());
        }
    }

    @Override
    public void draw(float alpha) {
        for (Player player : players) {
            player.draw(modelBatch);
            player.drawHUD(spriteBatch);
        }

        Gdx.graphics.setTitle(Constants.APP_ID + " [FPS: " + Gdx.graphics.getFramesPerSecond() + "]");
    }

    @Override
    public void resize(int w, int h) {
        for (Player player : players) {
            player.resize(w, h);
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        modelBatch.dispose();
        spriteBatch.dispose();
    }
}

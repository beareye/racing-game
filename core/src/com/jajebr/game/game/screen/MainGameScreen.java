package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Timer;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.player.Player;
import com.jajebr.game.game.world.World;

public class MainGameScreen extends Screen {
    private ModelBatch modelBatch;
    private SpriteBatch spriteBatch;

    private World world;
    private Array<Player> players;

    private Timer fadeOutTimer;

    public MainGameScreen(int numPlayers) {
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();

        world = new World();

        this.players = new Array<Player>();
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i, numPlayers, this.world);
            players.add(player);
            world.getEnvironment().add(player.getPlayerLight());
            Director.getPlayerInputController().assignControllerToPlayer(player);
        }

        this.fadeOutTimer = new Timer(true);
    }

    @Override
    public void update(float dt) {
        for (Player player : players) {
            player.update(dt);
        }
        world.update(dt);

        if (world.finished(players.size)) {
            this.fadeOutTimer.update(dt);

            if (this.fadeOutTimer.getTimeElapsed() > 10f) {
                Director.changeScreen(new ControllerAssignScreen());
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F10)) {
            Director.changeScreen(new HeightMapTestScreen());
        }
    }

    @Override
    public void draw(float alpha) {
        for (Player player : players) {
            player.draw(modelBatch);
            player.drawHUD(spriteBatch, players);
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

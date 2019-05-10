package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.player.PlayerInputController;

public class LoadingScreen extends Screen {
    private SpriteBatch spriteBatch;
    private Matrix4 transform;
    private GlyphLayout glyphLayout;

    public LoadingScreen() {
        Content.init();
        spriteBatch = new SpriteBatch();
        transform = new Matrix4();
        transform.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void update(float dt) {
        if (Content.isFinished()) {
            Content.load();

            Director.setPlayerInputController(new PlayerInputController());
            Controllers.addListener(Director.getPlayerInputController());
            Gdx.input.setInputProcessor(Director.getPlayerInputController());

            Director.changeScreen(new ControllerAssignScreen());
        }
    }

    @Override
    public void draw(float alpha) {
        spriteBatch.setProjectionMatrix(transform);
        spriteBatch.begin();
        glyphLayout.setText(Content.normalFont, "LOADING");
        Content.normalFont.draw(
                spriteBatch,
                "LOADING",
                Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                Gdx.graphics.getHeight() / 2f - glyphLayout.height / 2f
        );
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}

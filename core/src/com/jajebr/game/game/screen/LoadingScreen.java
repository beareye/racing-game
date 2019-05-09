package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;

public class LoadingScreen extends Screen {
    SpriteBatch spriteBatch;
    GlyphLayout glyphLayout;
    public LoadingScreen() {
        Content.init();
        spriteBatch = new SpriteBatch();
        spriteBatch.getTransformMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void update(float dt) {
        if (Content.isFinished()) {
            Content.load();
            Director.log("Loading");
            Director.changeScreen(new ControllerAssignScreen());
        }
    }

    @Override
    public void draw(float alpha) {
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

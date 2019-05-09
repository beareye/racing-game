package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ObjectSet;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Timer;
import com.jajebr.game.engine.Utilities;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;

public class ControllerAssignScreen extends Screen {
    private SpriteBatch spriteBatch;
    private Matrix4 ortho;
    private Timer flashTimer;
    private GlyphLayout glyphLayout;

    public ControllerAssignScreen() {
        spriteBatch = new SpriteBatch();
        ortho = new Matrix4();
        ortho.setToOrtho2D(
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );

        glyphLayout = new GlyphLayout();
        flashTimer = new Timer(true);

        Director.getPlayerInputController().startListening();
    }

    @Override
    public void update(float dt) {
        flashTimer.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && Director.getPlayerInputController().getNumPlayers() > 0) {
            Director.getPlayerInputController().stopListening();
            Director.changeScreen(new MainGameScreen(Director.getPlayerInputController().getNumPlayers()));
        }
    }

    @Override
    public void draw(float alpha) {
        spriteBatch.setProjectionMatrix(ortho);
        spriteBatch.begin();
            ObjectSet.ObjectSetIterator<Controller> it = Director.getPlayerInputController().getAvailableControllers().iterator();
            float y = Gdx.graphics.getHeight() - 100f;
            while (it.hasNext) {
                Controller controller = it.next();
                glyphLayout.setText(Content.normalFont, controller.getName());

                Content.normalFont.setColor(Utilities.getColorFromHashcode(controller));
                Content.normalFont.draw(
                        spriteBatch,
                        controller.getName(),
                        Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                        y
                );

                y -= glyphLayout.height + 33;
            }

            if (Director.getPlayerInputController().usingKeyboard()) {
                Content.normalFont.setColor(Color.WHITE);
                glyphLayout.setText(Content.normalFont, "Keyboard");
                Content.normalFont.draw(
                        spriteBatch,
                        "Keyboard",
                        Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                        y
                );
            }

            glyphLayout.setText(Content.normalFont, "PRESS ANY BUTTON TO JOIN");
            Content.normalFont.setColor(
                    1f,
                    1f,
                    1f,
                    0.5f * MathUtils.sin(flashTimer.getTimeElapsed()) + 0.5f
            );
            Content.normalFont.draw(
                    spriteBatch,
                    "PRESS ANY BUTTON TO JOIN",
                    Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                    100
            );
        spriteBatch.end();
    }

    @Override
    public void resize(int w, int h) {
        ortho.setToOrtho2D(
                0,
                0,
                w,
                h
        );
    }
}

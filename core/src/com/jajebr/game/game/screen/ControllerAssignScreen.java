package com.jajebr.game.game.screen;

import com.badlogic.gdx.Application;
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

        Director.getPlayerInputController().reset();
        Director.getPlayerInputController().startListening();

        Content.normalFont.getData().setScale(1f);

        Content.jackson.setLooping(true);
        Content.jackson.play();
    }

    @Override
    public void update(float dt) {
        flashTimer.update(dt);

        int players = Director.getPlayerInputController().getNumPlayers();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            players += 1;
        }
        if (
                (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isTouched())
                && players > 0) {
            Director.getPlayerInputController().stopListening();
            Director.changeScreen(new MainGameScreen(players));

            Content.jackson.stop();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            Director.getPlayerInputController().reset();
            Director.getPlayerInputController().stopListening();
            Director.changeScreen(new OptionsScreen());
            Content.jackson.stop();
        }
    }

    @Override
    public void draw(float alpha) {
        spriteBatch.setProjectionMatrix(ortho);
        spriteBatch.begin();
            ObjectSet.ObjectSetIterator<Controller> it = Director.getPlayerInputController().getAvailableControllers().iterator();
            float y = Gdx.graphics.getHeight() - 100f;
            glyphLayout.setText(Content.normalFont, "TURBO BLAST");
            Content.normalFont.setColor(Color.CYAN);
            Content.normalFont.draw(
                    spriteBatch,
                    "TURBO BLAST",
                    Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                    Gdx.graphics.getHeight() - glyphLayout.height - 20f
            );
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

            if (Director.getPlayerInputController().getNumPlayers() > 0) {
                String text = "PRESS ENTER TO START";
                if (Gdx.app.getType() == Application.ApplicationType.Android) {
                    text = "TAP TO START";
                }
                glyphLayout.setText(Content.normalFont, text);
                Content.normalFont.setColor(Color.GOLD);
                Content.normalFont.draw(
                        spriteBatch,
                        text,
                        Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                        133
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

            glyphLayout.setText(Content.normalFont, "PRESS TAB TO SET OPTIONS");
            Content.normalFont.setColor(Color.SALMON);
            Content.normalFont.draw(
                    spriteBatch,
                    "PRESS TAB TO SET OPTIONS",
                    Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                    100 - glyphLayout.height * 2f
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

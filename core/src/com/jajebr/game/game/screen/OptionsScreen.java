package com.jajebr.game.game.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Timer;
import com.jajebr.game.engine.Utilities;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;

import java.awt.Menu;

public class OptionsScreen extends Screen {
    class MenuItem {
        private String name;

        public String getName() {
            return name;
        }

        public MenuItem(String newName) {
            this.name = newName;
        }

        public String getNameWithOption(boolean selected, boolean activated) {
            StringBuilder builder = new StringBuilder();
            if (selected) {
                builder.append("=> ");
            }
            builder.append(this.name);
            builder.append(": ");
            if (activated) {
                builder.append("Yes");
            } else {
                builder.append("No");
            }
            return builder.toString();
        }
    }

    private SpriteBatch spriteBatch;
    private Matrix4 ortho;
    private GlyphLayout glyphLayout;

    private int currentChoice;
    private Array<MenuItem> menu;

    public OptionsScreen() {
        spriteBatch = new SpriteBatch();
        ortho = new Matrix4();
        ortho.setToOrtho2D(
                0,
                0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );


        glyphLayout = new GlyphLayout();

        menu = new Array<MenuItem>();
        menu.add(new MenuItem("Fullscreen"));
        menu.add(new MenuItem("Show Foliage"));
        menu.add(new MenuItem("Debug"));
        this.currentChoice = 0;

        Content.normalFont.getData().setScale(1f);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            Director.getPlayerInputController().reset();
            Content.enter.play();
            Director.changeScreen(new ControllerAssignScreen());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            int prevChoice = this.currentChoice;
            this.currentChoice = Math.max(0, this.currentChoice - 1);
            if (prevChoice != this.currentChoice) {
                Content.blip.play();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            int prevChoice = this.currentChoice;
            this.currentChoice = Math.min(menu.size - 1, this.currentChoice + 1);
            if (prevChoice != this.currentChoice) {
                Content.blip.play();
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (this.currentChoice) {
                case 0:
                    Director.swapFullscreen();
                    break;
                case 1:
                    Director.LOW_DETAIL = !Director.LOW_DETAIL;
                    break;
                case 2:
                    Director.DEBUG = !Director.DEBUG;
                    break;
                default:
                    break;
            }
        }
    }

    private boolean getOption(int id) {
        switch (id) {
            case 0:
                return Director.isFullscreen();
            case 1:
                return !Director.LOW_DETAIL;
            case 2:
                return Director.DEBUG;
            default:
                return false;
        }
    }

    @Override
    public void draw(float alpha) {
        spriteBatch.setProjectionMatrix(ortho);
        spriteBatch.begin();
            drawAtCenter("PRESS TAB TO RETURN", Color.SALMON, 100);

            float y = Gdx.graphics.getHeight() - 100f;
            for (int i = 0; i < menu.size; i++) {
                MenuItem menuItem = menu.get(i);
                drawAtCenter(
                        menuItem.getNameWithOption(this.currentChoice == i, getOption(i)),
                        Utilities.getColorFromBoolean(getOption(i)),
                        y
                );
                y -= glyphLayout.height + 33f;
            }
        spriteBatch.end();
    }

    private void drawAtCenter(String text, Color color, float y) {
        glyphLayout.setText(Content.normalFont, text);
        Content.normalFont.setColor(color);
        Content.normalFont.draw(
                spriteBatch,
                text,
                Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2f,
                y - glyphLayout.height / 2f
        );
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

package com.jajebr.game.game.screen;

import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;

public class LoadingScreen extends Screen {
    public LoadingScreen() {
        Content.init();
    }

    @Override
    public void update(float dt) {
        if (Content.isFinished()) {
            Content.load();
            Director.log("Loading");
            Director.changeScreen(new MainGameScreen());
        }
    }

    @Override
    public void draw(float alpha) {

    }
}

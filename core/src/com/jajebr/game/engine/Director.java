package com.jajebr.game.engine;

import com.badlogic.gdx.Gdx;
import com.jajebr.game.engine.screen.Screen;

/**
 * A director contains globals for the game, such as the screen.
 */
public class Director {
    public static boolean DEBUG = false;

    private static boolean fullscreen = false;

    private static Screen currentScreen;

    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    public static void setCurrentScreen(Screen newScreen) {
        currentScreen = newScreen;
    }

    public static void log(String message) {
        Gdx.app.log(Constants.APP_ID, message);
    }

    public static void swapFullscreen() {
        if (fullscreen) {
            Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        } else {
            Director.log(Gdx.graphics.getDisplayMode().toString());
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        fullscreen = !fullscreen;
    }
}

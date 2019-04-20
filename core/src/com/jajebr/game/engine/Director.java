package com.jajebr.game.engine;

import com.badlogic.gdx.Gdx;
import com.jajebr.game.engine.screen.Screen;

/**
 * A director contains globals for the game, such as the screen.
 */
public class Director {
    public static boolean DEBUG = true;

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
}

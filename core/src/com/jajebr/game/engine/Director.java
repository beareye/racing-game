package com.jajebr.game.engine;

import com.badlogic.gdx.Gdx;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.player.PlayerInputController;

/**
 * A director contains globals for the game, such as the screen.
 */
public class Director {
    public static boolean DEBUG = false;
    public static boolean LOW_DETAIL = false;

    private static boolean fullscreen = false;

    private static Screen currentScreen;
    private static Screen nextScreen;
    private static PlayerInputController playerInputController;

    public static Screen getCurrentScreen() {
        return currentScreen;
    }
    public static Screen getNextScreen() {
        return nextScreen;
    }
    public static PlayerInputController getPlayerInputController() {
        return playerInputController;
    }

    public static void changeScreen(Screen newScreen) {
        nextScreen = newScreen;
    }

    public static void setPlayerInputController(PlayerInputController newPlayerInputController) {
        playerInputController = newPlayerInputController;
    }

    public static void changeScreens() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = nextScreen;
        nextScreen = null;
    }

    public static void log(String message) {
        Gdx.app.log(Constants.APP_ID, message);
    }

    public static void swapFullscreen() {
        if (fullscreen) {
            Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            if (Director.getCurrentScreen() != null) {
                Director.getCurrentScreen().resize(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
            }
        }
        fullscreen = !fullscreen;
    }
}

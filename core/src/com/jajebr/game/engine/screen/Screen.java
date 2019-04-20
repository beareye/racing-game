package com.jajebr.game.engine.screen;

/**
 * A screen represents some part of the game.
 */
public abstract class Screen {
    /**
     * Initializes a blank screen.
     */
    public Screen() {

    }

    /**
     * Updates the screen.
     * @param dt the delta time step
     */
    public abstract void update(float dt);

    /**
     * Draws the screen.
     * @param alpha linear interpolation factor
     */
    public abstract void draw(float alpha);

    /**
     * Called on a resize.
     * @param w the width of the screen
     * @param h the height of the screen
     */
    public void resize(int w, int h) {}

    /**
     * Called when changing screens.
     * Should be overridden to dispose any resources.
     */
    public void dispose() {}
}

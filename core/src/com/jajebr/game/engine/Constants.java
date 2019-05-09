package com.jajebr.game.engine;

/**
 * Contains some important constants for the (e.g. FPS)
 */
public class Constants {
    public static final int FPS = 60;
    public static final float SPF = 1.0f / FPS;
    public static final float PHYSICS_TIMESTEP = 1.0f / 60;
    public static final int PHYSICS_MAX_SUBSTEP = 5;
    public static final String APP_ID = "Turbo Boost";

    public static final int COUNTDOWN = 5;

    public static final short TRACK_BITFLAG = 1 << 8;
    public static final short ENTITY_BITFLAG = 1 << 9;
    public static final short ALL_BITFLAG = -1;
}

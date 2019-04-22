package com.jajebr.game.engine;

public class Utilities {
    /**
     * Interpolation through a quadratic equation.
     * @param val1 the initial value
     * @param val2 the final value
     * @param t interpolation factor, from 0 to 1
     * @return the eased value
     */
    public static float quadraticEasing(float val1, float val2, float t) {
        return val1 + (val2 - val1) * t * t;
    }
}

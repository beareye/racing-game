package com.jajebr.game.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.jajebr.game.game.Content;

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

    /**
     * Centers the radian count from [0, PI2] to [-PI, PI].
     * @param rad the radians
     * @return the centered radian
     */
    public static float centerRadians(float rad) {
        if (rad > MathUtils.PI) {
            rad -= MathUtils.PI2;
        }
        return rad;
    }

    /**
     * Returns if the values are all the same.
     * @param values an array of objects
     * @return whether all the values are the same or not
     */
    public static boolean allSame(Object... values) {
        if (values.length == 0) {
            return false;
        }
        Object pivot = values[0];
        for (Object value : values) {
            if (!pivot.equals(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a color derived from the hashcode.
     * @param object the object
     * @return the derived color
     */
    public static Color getColorFromHashcode(Object object) {
        int hashCode = object.hashCode();
        int colorBits = hashCode % 0xFFFFFF;
        int finalColorBits = (colorBits << 8) | 0xFF;
        return new Color(finalColorBits);
    }

    public static String rankToString(int rank) {
        if (rank % 10 == 1) {
            return rank + "st";
        } else if (rank % 10 == 2) {
            return rank + "nd";
        } else if (rank % 10 == 3) {
            return rank + "rd";
        } else {
            return rank + "th";
        }
    }

    public static Color getRankColor(int rank) {
        switch(rank) {
            case 1:
                return Color.GOLD;
            case 2:
                return Color.LIGHT_GRAY;
            case 3:
                return Color.BROWN;
            default:
                return Color.GREEN;
        }
    }

    public static Color getColorFromBoolean(boolean value) {
        if (value) {
            return Color.GREEN;
        } else {
            return Color.RED;
        }
    }

    /**
     * Converts a value in kilometers per hour (KMH) to miles (MPH).
     * @param value the value in kilometers per hour
     * @return the value in miles per hour
     */
    public static float convertKMHToMPH(float value) {
        return value / 1.6f;
    }
}

package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.Color;

public enum TrackTexture {
    NONE(0, new Color(0f, 0f, 0f, 0f)),
    ROAD(1, new Color(0x58555BFF)),
    SAND(2, new Color(0xF8E0BAFF)),
    GRASS(3, new Color(0x499A1FFF)),
    CHECKERBOARD(4, Color.WHITE);

    private int value;
    private Color color;

    public Color getColor() {
        return color;
    }
    public int getValue() {
        return value;
    }

    TrackTexture(int newValue, Color newColor) {
        this.value = newValue;
        this.color = newColor;
    }
}

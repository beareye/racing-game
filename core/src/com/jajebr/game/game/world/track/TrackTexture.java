package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.Color;

public enum TrackTexture {
    NONE(0, new Color(0f, 0f, 0f, 0f)),
    ROAD(1, Color.GRAY),
    SAND(2, Color.YELLOW),
    GRASS(3, Color.GREEN),
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

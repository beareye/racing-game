package com.jajebr.game.game.world.track;

public enum TrackTexture {
    NONE(0), ROAD(1), SAND(2), GRASS(3);

    private int value;

    public int getValue() {
        return value;
    }

    TrackTexture(int newValue) {
        this.value = newValue;
    }
}

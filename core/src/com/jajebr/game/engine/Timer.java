package com.jajebr.game.engine;

public class Timer {
    private boolean active;
    private float dtTimer;

    public boolean isActive() {
        return active;
    }

    public float getTimeElapsed() {
        return this.dtTimer;
    }

    public Timer(boolean activate) {
        this.dtTimer = 0f;
        this.active = activate;
    }

    public Timer() {
        this(false);
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void update(float dt) {
        if (active) {
            this.dtTimer += dt;
        }
    }

    public void reset() {
        this.dtTimer = 0f;
    }

    @Override
    public String toString() {
        int minutes = (int) this.dtTimer / 60;
        int seconds = (int) this.dtTimer % 60;
        int milliseconds = (int) ((this.dtTimer % 1f) * 1000f);

        return String.format("%02d:%02d:%03d", minutes, seconds, milliseconds);
    }
}

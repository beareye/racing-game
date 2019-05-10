package com.jajebr.game.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.jajebr.game.engine.Director;

public class PlayerInput {
    private Player player;
    private String name;

    public float leftRight;
    public boolean accelerate;
    public boolean reverse;
    public boolean driftingLeft;
    public boolean driftingRight;

    public float leftRightThreshold;

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public PlayerInput(Player newPlayer) {
        this.player = newPlayer;
        this.name = "";
        leftRight = 0f;
        leftRightThreshold = 0.1f;
    }

    public void updateFromButton(Controller controller, int button, boolean pressed) {
        if (controller.getName().equals("\"PS Vita\"")) {
            switch(button) {
                // X
                case 1:
                    this.accelerate = pressed;
                    break;
                // O
                case 2:
                    this.reverse = pressed;
                    break;
                // Triangle
                case 3:
                    if (pressed) {
                        this.player.setFirstPerson(!this.player.isFirstPerson());
                    }
                    break;
                // L
                case 4:
                    this.driftingLeft = pressed;
                    break;
                // R
                case 5:
                    this.driftingRight = pressed;
                    break;
            }
        } else {
            switch (button) {
                // B
                case 1:
                    this.reverse = pressed;
                    break;
                // A
                case 2:
                    this.accelerate = pressed;
                    break;
                // X
                case 3:
                    if (pressed) {
                        this.player.setFirstPerson(!this.player.isFirstPerson());
                    }
                    break;
                // L/L Trigger
                case 4:
                case 6:
                    this.driftingLeft = pressed;
                    break;
                // R/R Trigger
                case 5:
                case 7:
                    this.driftingRight = pressed;
                    break;
            }
        }
    }

    public void updateFromAxis(Controller controller, int axis, float value) {
        if (controller.getName().equals("\"PS Vita\"")) {
            // Left stick
            if (axis == 3) {
                this.leftRight = value;
            }
        } else {
            if (axis == 3) {
                this.leftRight = value;
            }
            Director.log("Axis " + axis + " value" + value);
        }
    }

    public void updateKey(int keycode, boolean pressed) {
        switch(keycode) {
            case Input.Keys.UP:
                this.accelerate = pressed;
                break;
            case Input.Keys.DOWN:
                this.reverse = pressed;
                break;
            case Input.Keys.Q:
                this.driftingLeft = pressed;
                break;
            case Input.Keys.E:
                this.driftingRight = pressed;
                break;
            case Input.Keys.TAB:
                if (pressed) {
                    this.player.setFirstPerson(!this.player.isFirstPerson());
                }
                break;
            case Input.Keys.LEFT:
                if (pressed) {
                    this.leftRight = -1.0f;
                } else if (!Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    this.leftRight = 0f;
                }
                break;
            case Input.Keys.RIGHT:
                if (pressed) {
                    this.leftRight = 1.0f;
                } else if (!Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    this.leftRight = 0f;
                }
                break;
        }
    }

    public void mobileUpdate(float dt) {
        float accelX = Gdx.input.getAccelerometerX() / 10;
        float accelY = Gdx.input.getAccelerometerY() / 10;
        float accelZ = Gdx.input.getAccelerometerZ() / 10;

        if (Gdx.input.isTouched()) {
            this.accelerate = true;
        } else {
            this.accelerate = false;
        }

        this.leftRight = accelY;
        if (accelZ < 0.8) { // Reverse
            this.reverse = true;
            this.accelerate = false;
        }
    }
}

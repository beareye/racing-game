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
        }
    }

    public void updateFromAxis(Controller controller, int axis, float value) {
        if (controller.getName().equals("\"PS Vita\"")) {
            // Left stick
            if (axis == 3) {
                this.leftRight = value;
            }
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
}

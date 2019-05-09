package com.jajebr.game.game.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.jajebr.game.engine.Director;

import java.util.Map;

public class PlayerInputController implements ControllerListener, InputProcessor {
    private ObjectMap<Controller, Player> controllerPlayerMap;
    private ObjectSet<Controller> availableControllers;
    private boolean listeningForNewControllers;

    private boolean useKeyboard;
    private Player keyboardPlayer;

    public ObjectSet<Controller> getAvailableControllers() {
        return availableControllers;
    }

    public boolean usingKeyboard() {
        return useKeyboard;
    }

    public PlayerInputController() {
        controllerPlayerMap = new ObjectMap<Controller, Player>();
        availableControllers = new ObjectSet<Controller>();
        listeningForNewControllers = true;

        this.keyboardPlayer = null;
        this.useKeyboard = false;
    }

    public int getNumPlayers() {
        int controllers = availableControllers.size;
        if (useKeyboard) {
            controllers += 1;
        }
        return controllers;
    }

    public void reset() {
        controllerPlayerMap.clear();
        availableControllers.clear();
    }

    public void startListening() {
        this.listeningForNewControllers = true;
    }

    public void stopListening() {
        this.listeningForNewControllers = false;
    }

    public void assignControllerToPlayer(Player player) {
        ObjectSet.ObjectSetIterator<Controller> it = availableControllers.iterator();
        if (it.hasNext()) {
            Controller controller = it.next();

            Director.log("Assigned controller " + controller + " to player " + player.getID());

            controllerPlayerMap.put(controller, player);
            it.remove();
            return;
        } else if (keyboardPlayer == null) {
            keyboardPlayer = player;
        } else {
            Director.log("No controllers available for player.");
        }
    }

    @Override
    public void connected(Controller controller) {
        Director.log("Controller " + controller.getName() + " connected");
    }

    @Override
    public void disconnected(Controller controller) {
        Director.log("Controller " + controller.getName() + " disconnected");
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if (listeningForNewControllers) {
            if (!availableControllers.contains(controller)) {
                availableControllers.add(controller);
            }
        }
        Player controlledPlayer = controllerPlayerMap.get(controller);
        if (controlledPlayer != null) {
            controlledPlayer.getPlayerInput().updateFromButton(controller, buttonCode, true);
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Player controlledPlayer = controllerPlayerMap.get(controller);
        if (controlledPlayer != null) {
            controlledPlayer.getPlayerInput().updateFromButton(controller, buttonCode, false);
        }
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Player controlledPlayer = controllerPlayerMap.get(controller);
        if (controlledPlayer != null) {
            controlledPlayer.getPlayerInput().updateFromAxis(controller, axisCode, value);
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (listeningForNewControllers && keycode != Input.Keys.ENTER) {
            useKeyboard = true;
        }
        if (keyboardPlayer != null) {
            keyboardPlayer.getPlayerInput().updateKey(keycode, true);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keyboardPlayer != null) {
            keyboardPlayer.getPlayerInput().updateKey(keycode, false);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

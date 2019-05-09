package com.jajebr.game.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PlayerViewport extends Viewport {
    private int id;
    private int numPlayers;

    private Rectangle view;

    public Rectangle getView() {
        return view;
    }

    public PlayerViewport(int playerID, int playerCount, Camera cam) {
        super();
        this.setCamera(cam);
        this.id = playerID;
        this.numPlayers = playerCount;
        this.view = new Rectangle();
    }

    private static Rectangle getDimensions(int id, int players) {
        // Special case for two players: rows
        Rectangle screen = new Rectangle();
        if (players == 2) {
            screen.width = Gdx.graphics.getWidth();
            screen.height = Gdx.graphics.getHeight() / 2f;
            screen.x = 0;
            screen.y = 0;
            if (id == 0) {
                // Give top to 1st player
                screen.y = Gdx.graphics.getHeight() / 2f;
            }
        }
        else {
            int playersPerColumnAndRow = (int) Math.ceil(Math.sqrt(players));
            screen.width = Gdx.graphics.getWidth() / playersPerColumnAndRow;
            screen.height = Gdx.graphics.getHeight() / playersPerColumnAndRow;
            int x = id % playersPerColumnAndRow;
            int y = id / playersPerColumnAndRow;
            screen.x = x * (Gdx.graphics.getWidth() / playersPerColumnAndRow);
            screen.y = y * (Gdx.graphics.getHeight() / playersPerColumnAndRow);
        }
        return screen;
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        this.view = PlayerViewport.getDimensions(id, numPlayers);
        this.setScreenBounds(
                (int) this.view.x,
                (int) this.view.y,
                (int) this.view.width,
                (int) this.view.height
        );
        this.setWorldSize(this.view.width, this.view.height);
        this.apply(centerCamera);
    }

    public void update() {
        this.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }
}

package com.jajebr.game.game.world;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.entity.Entity;

public class WorldContactListener extends ContactListener {
    private World world;

    public WorldContactListener(World newWorld) {
        world = newWorld;
    }

    @Override
    public void onContactEnded(int userValue0, boolean match0, int userValue1, boolean match1) {
        if (match0) {
            Director.log("Possibly match0 for " + userValue0);
        }
        if (match1) {
            Director.log("Possibly match1 for " + userValue1);
        }
    }
}

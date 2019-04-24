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
    public boolean onContactAdded(btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1) {
        Director.log("Collision between " + colObj0Wrap.getCollisionObject().getUserValue() + " and " + colObj1Wrap.getCollisionObject().getUserValue() + "?");
        return true;
    }
}

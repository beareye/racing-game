package com.jajebr.game.game.entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.world.World;

public class EntityGround extends Entity {
    public EntityGround(World newWorld) {
        super(newWorld, Content.boxModel, 0f);
        this.getRigidBody().translate(new Vector3(10f, -20f, 0f));
    }
}

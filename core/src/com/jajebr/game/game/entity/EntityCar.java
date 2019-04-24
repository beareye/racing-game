package com.jajebr.game.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Utilities;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.physics.Force;
import com.jajebr.game.game.world.World;

public class EntityCar extends Entity {
    public EntityCar(World world) {
        super(world, Content.formulaStar, 100f);
    }
}

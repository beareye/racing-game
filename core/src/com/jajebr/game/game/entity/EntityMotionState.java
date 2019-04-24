package com.jajebr.game.game.entity;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class EntityMotionState extends btMotionState {
    private Matrix4 transform;

    /**
     * Returns the transform linked with the Bullet motion state.
     * @return the world trasnsform
     */
    public Matrix4 getTransform() {
        return transform;
    }

    public EntityMotionState(Entity entity) {
        transform = entity.getModelInstance().transform;
    }

    @Override
    public void getWorldTransform(Matrix4 worldTrans) {
        worldTrans.set(transform);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        transform.set(worldTrans);
    }
}

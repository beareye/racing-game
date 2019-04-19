package com.jajebr.game.game.entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.jajebr.game.game.world.World;

/**
 * A 3D renderable entity.
 */
public abstract class EntityRenderable extends Entity {
    private ModelInstance instance;

    /**
     * Initializes an EntityRenderable.
     * @param model the model to create an instance with
     */
    public EntityRenderable(World world, Model model) {
        super(world);
        instance = new ModelInstance(model);
    }

    @Override
    public void applyMotion(float dt) {
        super.applyMotion(dt);

        // TODO: implement scale (if needed)
        this.instance.transform.set(this.getRotation());
        this.instance.transform.translate(this.getPosition());
    }

    @Override
    public void render(ModelBatch modelBatch) {
        modelBatch.render(this.instance);
    }
}

package com.jajebr.game.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.jajebr.game.game.world.World;

/**
 * A 3D renderable entity.
 */
public abstract class EntityRenderable extends Entity {
    private ModelInstance instance;
    private BoundingBox boundingBox;

    /**
     * Initializes an EntityRenderable.
     * @param model the model to create an instance with
     */
    public EntityRenderable(World world, Model model) {
        super(world);
        instance = new ModelInstance(model);

        BoundingBox box = new BoundingBox();
        instance.calculateBoundingBox(box);
    }

    @Override
    public void applyMotion(float dt) {
        super.applyMotion(dt);

        // TODO: implement scale (if needed)
        this.instance.transform.set(
                getPosition().x,
                getPosition().y,
                getPosition().z,
                getRotation().x,
                getRotation().y,
                getRotation().z,
                getRotation().w
        );
    }

    @Override
    public void render(ModelBatch modelBatch) {
        modelBatch.render(this.instance);
    }

    public void renderDebug(ShapeRenderer shapeRenderer) {
        /*
        shapeRenderer.box(
                this.boundingBox.min.x,
                this.boundingBox.min.y,
                this.boundingBox.min.z,
                this.boundingBox.getWidth(),
                this.boundingBox.getHeight(),
                this.boundingBox.getDepth()
        );
        */
    }
}
package com.jajebr.game.engine.renderer;

import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Sphere;

/**
 * ShapeRenderer with some additional 3D drawing operations.
 */
public class SpecialShapeRenderer extends ShapeRenderer {
    /**
     * Draws a 3D triangle.
     * @param point1
     * @param point2
     * @param point3
     */
    public void triangle(Vector3 point1, Vector3 point2, Vector3 point3) {
        this.check(ShapeType.Line, ShapeType.Filled, 6);
        float colorBits = this.getColor().toFloatBits();
        ImmediateModeRenderer renderer = this.getRenderer();

        if (this.getCurrentType() == ShapeType.Line) {
            renderer.color(colorBits);
            renderer.vertex(point1.x, point1.y, point1.z);
            renderer.color(colorBits);
            renderer.vertex(point2.x, point2.y, point2.z);

            renderer.color(colorBits);
            renderer.vertex(point2.x, point2.y, point2.z);
            renderer.color(colorBits);
            renderer.vertex(point3.x, point3.y, point3.z);

            renderer.color(colorBits);
            renderer.vertex(point3.x, point3.y, point3.z);
            renderer.color(colorBits);
            renderer.vertex(point1.x, point1.y, point1.z);
        } else {
            renderer.color(colorBits);
            renderer.vertex(point1.x, point1.y, point1.z);
            renderer.color(colorBits);
            renderer.vertex(point2.x, point2.y, point2.z);
            renderer.color(colorBits);
            renderer.vertex(point3.x, point3.y, point3.z);
        }
    }

    /**
     * Helper method copied from the ShapeRenderer.
     */
    /** @param other May be null. */
    private void check (ShapeType preferred, ShapeType other, int newVertices) {
        ShapeType shapeType = this.getCurrentType();
        ImmediateModeRenderer renderer = this.getRenderer();
        if (shapeType == null) throw new IllegalStateException("begin must be called first.");

        if (shapeType != preferred && shapeType != other) {
            // Shape type is not valid.
            if (other == null)
                throw new IllegalStateException("Must call begin(ShapeType." + preferred + ").");
            else
                throw new IllegalStateException("Must call begin(ShapeType." + preferred + ") or begin(ShapeType." + other + ").");
        } else if (renderer.getMaxVertices() - renderer.getNumVertices() < newVertices) {
            // Not enough space.
            ShapeType type = shapeType;
            end();
            begin(type);
        }
    }
}

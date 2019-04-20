package com.jajebr.game.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.jajebr.game.engine.screen.Screen;

/**
 * Playground.
 */
public class DebugScreen extends Screen {
    private ShapeRenderer shapeRenderer;
    private Bezier<Vector2> test;
    private Vector2 movingPoint;

    public DebugScreen() {
        shapeRenderer = new ShapeRenderer();
        movingPoint = new Vector2(300, 250);
        test = new Bezier<Vector2>(
                new Vector2(100, 100),
                new Vector2(200, 100),
                new Vector2(200, 150),
                movingPoint
        );
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movingPoint.y += 50 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movingPoint.y -= 50 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movingPoint.x -= 50 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movingPoint.x += 50 * dt;
        }
    }

    @Override
    public void draw(float alpha) {
        float tStep = 1 / 300f;
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Vector2 points : test.points) {
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.point(points.x, points.y, 0);
            }
            Vector2 point1 = new Vector2();
            Vector2 point2 = new Vector2();
            for (float t = 0; t <= 1 - tStep; t += tStep) {
                float nextT = t + tStep;
                test.valueAt(point1, t);
                test.valueAt(point2, nextT);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.line(point1, point2);
            }
        shapeRenderer.end();
    }
}

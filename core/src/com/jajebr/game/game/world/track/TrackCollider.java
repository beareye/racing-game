package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.renderer.SpecialShapeRenderer;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.physics.Force;

/**
 * Tests for collisions with the track.
 */
public class TrackCollider {
    private Track track;
    private TrackMeshCreator trackMeshCreator;

    // Private vectors to avoid garbage collection
    private Vector3 tmp, tmp2, tmp3, best, intersection, normal;

    public TrackCollider(Track existingTrack, TrackMeshCreator existingTrackMeshCreator) {
        track = existingTrack;
        trackMeshCreator = existingTrackMeshCreator;

        tmp = new Vector3();
        tmp2 = new Vector3();
        tmp3 = new Vector3();
        best = new Vector3();
        normal = new Vector3();
        intersection = new Vector3();
    }

    /**
     * Tests if the car collides with the track.
     * If so, resolves collisions and apply normals.
     * @param car
     * @return
     */
    public boolean testCarCollision(EntityCar car) {
        BoundingBox box = car.getBoundingBox();
        Vector3 origin = new Vector3();
        Vector3 xDisplacement = new Vector3(Math.signum(car.getDisplacement().x), 0, 0);
        Vector3 yDisplacement = new Vector3(0, Math.signum(car.getDisplacement().y), 0);
        Vector3 zDisplacement = new Vector3(0, 0, Math.signum(car.getDisplacement().z));

        // The distances between the ray and attempted position.
        // Used for limiting intersections.
        float distXSquared = 0f;
        float distYSquared = 0f;
        float distZSquared = 0f;

        Ray ray = new Ray();

        float shift = 0f;
        // Start on X-direction.
        if (!xDisplacement.isZero()) {
            origin.set(car.getPosition());
            shift = Math.signum(car.getDisplacement().x) * box.getWidth() / 2f;
            origin.add(shift, 0, 0);
            ray.set(origin, xDisplacement);
            distXSquared = car.getDisplacement().x * car.getDisplacement().x;
            testAndResolveCollision(car, ray, Vector3.X, shift, distXSquared);
        }
        // Y-direction
        if (!yDisplacement.isZero()) {
            origin.set(car.getPosition());
            shift = Math.signum(car.getDisplacement().y) * box.getHeight() / 2f;
            origin.add(0, shift, 0);
            ray.set(origin, yDisplacement);
            distYSquared = car.getDisplacement().y * car.getDisplacement().y;
            testAndResolveCollision(car, ray, Vector3.Y, shift, distYSquared);
        }
        // Z-direction
        if (!zDisplacement.isZero()) {
            origin.set(car.getPosition());
            shift = Math.signum(car.getDisplacement().z) * box.getDepth() / 2f;
            origin.add(0, 0, shift);
            ray.set(origin, zDisplacement);
            distZSquared = car.getDisplacement().z * car.getDisplacement().z;
            testAndResolveCollision(car, ray, Vector3.Z, shift, distZSquared);
        }

        return true;
    }

    private void testAndResolveCollision(EntityCar car, Ray ray, Vector3 axis, float shift, float min_dist) {
        if (this.intersectRayWithTrack(ray, trackMeshCreator, intersection, normal, min_dist)) {
            if (axis == Vector3.X) {
                car.getNextAttemptedPosition().x = intersection.x - shift;
                car.getDisplacement().x = intersection.x - (car.getPosition().x + shift);
                car.getVelocity().x *= -track.getRestitutionCoefficient();
            } else if (axis == Vector3.Y) {
                car.getNextAttemptedPosition().y = intersection.y - shift;
                car.getDisplacement().y = intersection.y - (car.getPosition().y + shift);
                // Prevent bouncing on the y-axis.
                car.getVelocity().y = 0f;
            } else {
                car.getNextAttemptedPosition().z = intersection.z - shift;
                car.getDisplacement().z = intersection.z - (car.getPosition().z + shift);
                car.getVelocity().z *= -track.getRestitutionCoefficient();
            }
        }
    }
    public boolean testCarCollisionDebug(EntityCar car, SpecialShapeRenderer shapeRenderer) {
        BoundingBox box = car.getBoundingBox();
        Vector3 origin = new Vector3();
        Vector3 xDisplacement = new Vector3(Math.signum(car.getDisplacement().x), 0, 0);
        Vector3 yDisplacement = new Vector3(0, Math.signum(car.getDisplacement().y), 0);
        Vector3 zDisplacement = new Vector3(0, 0, Math.signum(car.getDisplacement().z));

        // The distances between the ray and attempted position.
        // Used for limiting intersections.
        float distXSquared = 0f;
        float distYSquared = 0f;
        float distZSquared = 0f;

        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.ray(new Ray(car.getPosition(), car.getDisplacement().nor()), 100);

        Ray ray = new Ray();

        float shift = 0f;
        // Start on X-direction.
        if (!xDisplacement.isZero()) {
            origin.set(car.getPosition());
            shift = Math.signum(car.getDisplacement().x) * box.getWidth() / 2f;
            origin.add(shift, 0, 0);
            ray.set(origin, xDisplacement);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.ray(ray);
            distXSquared = car.getDisplacement().x * car.getDisplacement().x;
            testAndResolveCollisionDebug(car, ray, Vector3.X, shift, distXSquared, shapeRenderer);
        }
        // Y-direction
        if (!yDisplacement.isZero()) {
            origin.set(car.getPosition());
            shift = Math.signum(car.getDisplacement().y) * box.getHeight() / 2f;
            origin.add(0, shift, 0);
            ray.set(origin, yDisplacement);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.ray(ray);
            distYSquared = car.getDisplacement().y * car.getDisplacement().y;
            testAndResolveCollisionDebug(car, ray, Vector3.Y, shift, distYSquared, shapeRenderer);
        }
        // Z-direction
        if (!zDisplacement.isZero()) {
            origin.set(car.getPosition());
            shift = Math.signum(car.getDisplacement().z) * box.getDepth() / 2f;
            origin.add(0, 0, shift);
            ray.set(origin, zDisplacement);
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.ray(ray);
            distZSquared = car.getDisplacement().z * car.getDisplacement().z;
            testAndResolveCollisionDebug(car, ray, Vector3.Z, shift, distZSquared, shapeRenderer);
        }

        return true;
    }

    private void testAndResolveCollisionDebug(EntityCar car, Ray ray, Vector3 axis, float shift, float min_dist, SpecialShapeRenderer shapeRenderer) {
        if (this.intersectRayWithTrack(ray, trackMeshCreator, intersection, normal, Float.POSITIVE_INFINITY)) {
            if (axis == Vector3.X) {
                shapeRenderer.setColor(Color.WHITE);
            } else if (axis == Vector3.Z) {
                shapeRenderer.setColor(Color.GRAY);
            } else {
                shapeRenderer.setColor(Color.LIGHT_GRAY);
            }
            shapeRenderer.point3D(intersection.x, intersection.y, intersection.z, 15f);
        }
        if (this.intersectRayWithTrack(ray, trackMeshCreator, intersection, normal, min_dist)) {
            if (axis == Vector3.X) {
                // car.getNextAttemptedPosition().x = intersection.x - shift;
                // car.getVelocity().x *= -track.getRestitutionCoefficient();
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.point3D(intersection.x, intersection.y, intersection.z, 20f);
            } else if (axis == Vector3.Y) {
                // car.getNextAttemptedPosition().y = intersection.y - shift;
                // Prevent bouncing on the y-axis.
                // car.getVelocity().y = 0f;
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.point3D(intersection.x, intersection.y, intersection.z, 20f);
            } else {
                // car.getNextAttemptedPosition().z = intersection.z - shift;
                // car.getVelocity().z *= -track.getRestitutionCoefficient();
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.point3D(intersection.x, intersection.y, intersection.z, 20f);
            }
        }
    }

    /**
     * Intersects the ray with the track.
     * @param ray The ray
     * @param trackMeshCreator the track mesh
     * @param intersectionPoint the intersection point with the ray and track
     * @param normals the normal point with the ray and the best intersected triangle
     * @param minDistSquared the minimum distance, squared
     */
    public boolean intersectRayWithTrack(Ray ray, TrackMeshCreator trackMeshCreator, Vector3 intersectionPoint, Vector3 normals, float minDistSquared) {
        boolean hit = false;

        short[] indices = trackMeshCreator.getIndices();
        float[] vertices = trackMeshCreator.getVertices();

        if (indices.length % 3 != 0) throw new RuntimeException("triangle list size is not a multiple of 3");

        for (int i = 0; i < indices.length; i += 3) {
            int i1 = indices[i] * trackMeshCreator.getComponentsPerVertex();
            int i2 = indices[i + 1] * trackMeshCreator.getComponentsPerVertex();
            int i3 = indices[i + 2] * trackMeshCreator.getComponentsPerVertex();

            boolean result = Intersector.intersectRayTriangle(
                    ray,
                    tmp.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]),
                    tmp2.set(vertices[i2], vertices[i2 + 1], vertices[i2 + 2]),
                    tmp3.set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]),
                    intersection);

            if (result) {
                float dist = ray.origin.dst2(intersection);
                if (dist < minDistSquared) {
                    minDistSquared = dist;
                    best.set(intersection);
                    hit = true;
                    normals.set(
                            vertices[i1 + 3],
                            vertices[i1 + 4],
                            vertices[i1 + 5]
                    );
                }
            }
        }

        if (!hit) {
            return false;
        } else {
            intersectionPoint.set(best);
            return true;
        }
    }
}

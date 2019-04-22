package com.jajebr.game.game.world.track;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.physics.Force;

/**
 * Tests for collisions with the track.
 */
public class TrackCollider {
    private Track track;
    private TrackMeshCreator trackMeshCreator;

    private float CAR_TOLERANCE = 2f;

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
        float distX = 0f;
        float distY = 0f;
        float distZ = 0f;

        Ray ray = new Ray();
        float shift = 0f;

        // Start on X-direction.
        if (!xDisplacement.isZero()) {
            origin = box.getCenter(origin);
            shift = Math.signum(car.getDisplacement().x) * box.getWidth() / 2f;
            origin.add(shift, 0, 0);
            ray.set(origin, xDisplacement);
            distX = car.getDisplacement().x * car.getDisplacement().x;
            testAndResolveCollision(car, ray, Vector3.X, shift, distX);
        }
        // Y-direction
        if (!yDisplacement.isZero()) {
            origin = box.getCenter(origin);
            shift = Math.signum(car.getDisplacement().y) * box.getHeight() / 2f;
            origin.add(0, shift, 0);
            ray.set(origin, yDisplacement);
            distY = car.getDisplacement().y * car.getDisplacement().y;
            testAndResolveCollision(car, ray, Vector3.Y, shift, distY);
        }
        // Z-direction
        if (!zDisplacement.isZero()) {
            origin = box.getCenter(origin);
            shift = Math.signum(car.getDisplacement().z) * box.getDepth() / 2f;
            origin.add(0, 0, shift);
            ray.set(origin, zDisplacement);
            distZ = car.getDisplacement().z * car.getDisplacement().z;
            testAndResolveCollision(car, ray, Vector3.Z, shift, distZ);
        }

        return true;
    }

    private void testAndResolveCollision(EntityCar car, Ray ray, Vector3 axis, float shift, float min_dist) {
        if (this.intersectRayWithTrack(ray, trackMeshCreator, intersection, normal, min_dist)) {
            // Get the displacement from the intersection to the attempted position.
            tmp.set(car.getNextAttemptedPosition()).sub(intersection);
            // Go the opposite way.
            tmp.scl(-1);
            // Add an extra in the shift so that the car will be comfortable with the surface.
            shift *= Constants.EPSILON;
            if (axis == Vector3.X) {
                tmp.x -= shift;
                car.getDisplacement().x += tmp.x;
                car.getNextAttemptedPosition().x += tmp.x;
                car.getVelocity().x = 0f;
            } else if (axis == Vector3.Y) {
                tmp.y -= shift;
                car.getDisplacement().y += tmp.y;
                car.getNextAttemptedPosition().y += tmp.y;
                car.getVelocity().y = 0f;
            } else {
                tmp.z -= shift;
                car.getDisplacement().z += tmp.z;
                car.getNextAttemptedPosition().z += tmp.z;
                car.getVelocity().z = 0f;
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

            boolean result = Intersector.intersectRayTriangle(ray, tmp.set(vertices[i1], vertices[i1 + 1], vertices[i1 + 2]),
                    tmp2.set(vertices[i2], vertices[i2 + 1], vertices[i2 + 2]),
                    tmp3.set(vertices[i3], vertices[i3 + 1], vertices[i3 + 2]), intersection);

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

        if (!hit)
            return false;
        else {
            intersectionPoint.set(best);
            return true;
        }
    }
}

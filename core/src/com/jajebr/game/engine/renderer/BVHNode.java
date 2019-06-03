package com.jajebr.game.engine.renderer;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.world.World;

public class BVHNode {
    private Array<ModelInstance> modelInstances;
    private BoundingBox boundingBox;
    private BVHNode left;
    private BVHNode right;

    public BVHNode(Array<ModelInstance> instances, int leafSize) {
        BoundingBox aabb = new BoundingBox();
        BoundingBox centroidBox = new BoundingBox();

        // Compute the AABB of all the primitives
        for (ModelInstance instance : instances) {
            BoundingBox primitive = (BoundingBox) instance.userData;
            aabb.ext(primitive);
            Vector3 center = new Vector3();
            primitive.getCenter(center);
            centroidBox.ext(center);
        }

        Director.log("Creating new BVH node with size " + instances.size);

        // Create the new node
        this.boundingBox = aabb;

        if (instances.size <= leafSize) {
            this.modelInstances = new Array<ModelInstance>(instances);
        } else {
            Array<ModelInstance> leftInstances = new Array<ModelInstance>();
            Array<ModelInstance> rightInstances = new Array<ModelInstance>();
            Vector3 midpoint = new Vector3();
            midpoint = centroidBox.getCenter(midpoint);

            Vector3 positiveInfinity = new Vector3(1, 1, 1).scl(100000000000f);
            Vector3 negativeInfinity = new Vector3(1, 1, 1).scl(-100000000000f);

            if (aabb.getWidth() > aabb.getHeight() && aabb.getWidth() > aabb.getDepth()) {
                while (leftInstances.isEmpty() || rightInstances.isEmpty()) {
                    leftInstances.clear();
                    rightInstances.clear();

                    BoundingBox leftPrimitiveBox = new BoundingBox();
                    BoundingBox rightPrimitiveBox = new BoundingBox();
                    leftPrimitiveBox.inf();
                    rightPrimitiveBox.inf();

                    // Largest dimension: x-axis
                    for (ModelInstance instance : instances) {
                        BoundingBox box = (BoundingBox) instance.userData;
                        Vector3 boxCenter = new Vector3();
                        box.getCenter(boxCenter);
                        if (boxCenter.x < midpoint.x) {
                            leftInstances.add(instance);
                            leftPrimitiveBox.ext(box);
                        } else {
                            rightInstances.add(instance);
                            rightPrimitiveBox.ext(box);
                        }
                    }

                    // Move split point
                    if (leftInstances.size > rightInstances.size) {
                        midpoint = leftPrimitiveBox.getCenter(midpoint);
                    } else {
                        midpoint = rightPrimitiveBox.getCenter(midpoint);
                    }

                    Director.log("Left box is " + leftPrimitiveBox);
                    Director.log("Right box is " + rightPrimitiveBox);

                    Director.log("MidpointX is " + midpoint);
                }
            } else if (aabb.getHeight() > aabb.getWidth() && aabb.getHeight() > aabb.getDepth()) {
                while (leftInstances.isEmpty() || rightInstances.isEmpty()) {
                    leftInstances.clear();
                    rightInstances.clear();

                    BoundingBox leftPrimitiveBox = new BoundingBox();
                    BoundingBox rightPrimitiveBox = new BoundingBox();
                    leftPrimitiveBox.inf();
                    rightPrimitiveBox.inf();

                    // Largest dimension: y-axis
                    for (ModelInstance instance : instances) {
                        BoundingBox box = (BoundingBox) instance.userData;
                        Vector3 boxCenter = new Vector3();
                        box.getCenter(boxCenter);
                        if (boxCenter.y < midpoint.y) {
                            leftInstances.add(instance);
                            leftPrimitiveBox.ext(box);
                        } else {
                            rightInstances.add(instance);
                            rightPrimitiveBox.ext(box);
                        }
                    }

                    // Move split point
                    if (leftInstances.size > rightInstances.size) {
                        midpoint = leftPrimitiveBox.getCenter(midpoint);
                    } else {
                        midpoint = rightPrimitiveBox.getCenter(midpoint);
                    }

                    Director.log("Left box is " + leftPrimitiveBox);
                    Director.log("Right box is " + rightPrimitiveBox);

                    Director.log("MidpointY is " + midpoint);
                }
            } else {
                while (leftInstances.isEmpty() || rightInstances.isEmpty()) {
                    leftInstances.clear();
                    rightInstances.clear();

                    BoundingBox leftPrimitiveBox = new BoundingBox();
                    BoundingBox rightPrimitiveBox = new BoundingBox();
                    leftPrimitiveBox.inf();
                    rightPrimitiveBox.inf();

                    // Largest dimension: z-axis
                    for (ModelInstance instance : instances) {
                        BoundingBox box = (BoundingBox) instance.userData;
                        Vector3 boxCenter = new Vector3();
                        box.getCenter(boxCenter);
                        if (boxCenter.z < midpoint.z) {
                            leftInstances.add(instance);
                            leftPrimitiveBox.ext(box);
                        } else {
                            rightInstances.add(instance);
                            rightPrimitiveBox.ext(box);
                        }
                    }

                    // Move split point
                    if (leftInstances.size > rightInstances.size) {
                        midpoint = leftPrimitiveBox.getCenter(midpoint);
                    } else {
                        midpoint = rightPrimitiveBox.getCenter(midpoint);
                    }

                    Director.log("Left box is " + leftPrimitiveBox);
                    Director.log("Right box is " + rightPrimitiveBox);

                    Director.log("MidpointZ is " + midpoint);
                }
            }

            Director.log("Splitting");
            this.left = new BVHNode(leftInstances, leafSize);
            this.right = new BVHNode(rightInstances, leafSize);
        }
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public void renderAll(ModelBatch modelBatch, World world) {
        Frustum frustum = modelBatch.getCamera().frustum;
        if (frustum.boundsInFrustum(this.boundingBox)) {
            if (this.isLeaf()) {
                for (ModelInstance instance : this.modelInstances) {
                    BoundingBox box = (BoundingBox) instance.userData;
                    if (frustum.boundsInFrustum(box)) {
                        modelBatch.render(instance, world.getEnvironment());
                    }
                }
            } else {
                this.left.renderAll(modelBatch, world);
                this.right.renderAll(modelBatch, world);
            }
        }
    }
}

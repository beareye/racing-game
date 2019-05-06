package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ShortArray;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.Utilities;
import com.jajebr.game.game.Content;

/**
 * Creates the mesh for the track.
 */
public class TrackMesh implements RenderableProvider {
    private TrackHeightmap trackHeightmap;
    private float[] vertices;
    private short[] indices;

    private Mesh trackMesh;
    private Material testMaterial;

    /**
     * Returns the track heightmap object.
     * @return the track heightmap
     */
    public TrackHeightmap getTrackHeightmap() {
        return trackHeightmap;
    }

    /**
     * Returns the components for each vertex.
     * In this case: position (3) + normal (3) + texture (2)
     * @return the amount of components for each vertex
     */
    public int getComponentsPerVertex() {
        return 8;
    }

    public TrackMesh() {
        trackHeightmap = new TrackHeightmap();
        testMaterial = new Material(TextureAttribute.createDiffuse(Content.materials));

        this.createVerticesFromHeightmap();
        compileVerticesAndIndices();

        trackMesh = new Mesh(true, this.getAmountOfVertices(), indices.length, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        trackMesh.setVertices(this.vertices);
        trackMesh.setIndices(this.indices);
    }

    /**
     * Creates vertices and indices from the heightmap.
     */
    private void createVerticesFromHeightmap() {
        // Each value of the heightmap needs 2 triangles => 6 vertices (with different normals)
        // Each value of the heightmap needs 2 triangles => 6 indices.
        this.vertices = new float[(trackHeightmap.getWidth() - 1) * (trackHeightmap.getHeight() - 1) * 6 * getComponentsPerVertex()];
        this.indices = new short[(trackHeightmap.getWidth() - 1) * (trackHeightmap.getHeight() - 1) * 6];

        int vertexI = 0;
        int indexI = 0;
        for (int y = 0; y < this.trackHeightmap.getHeight() - 1; y++) {
            for (int x = 0; x < this.trackHeightmap.getWidth() - 1; x++) {
                Vector3 topLeft = this.getVertex(x, y);
                Vector3 topRight = this.getVertex(x + 1, y);
                Vector3 bottomLeft = this.getVertex(x, y + 1);
                Vector3 bottomRight = this.getVertex(x + 1, y + 1);

                Vector3 normal1 = getNormal(topLeft, bottomLeft, topRight);
                Vector3 normal2 = getNormal(topRight, bottomLeft, bottomRight);

                TrackTexture topLeftTexture = this.trackHeightmap.getTextureAt(x, y);
                TrackTexture topRightTexture = this.trackHeightmap.getTextureAt(x + 1, y);
                TrackTexture bottomLeftTexture = this.trackHeightmap.getTextureAt(x, y + 1);
                TrackTexture bottomRightTexture = this.trackHeightmap.getTextureAt(x + 1, y + 1);

                // Fix textures.
                if (!Utilities.allSame(topLeftTexture, topRightTexture, bottomLeftTexture, bottomRightTexture)) {
                    TrackTexture reference = this.findNotRoad(topLeftTexture, topRightTexture, bottomLeftTexture, bottomRightTexture);
                    // TODO: not all textures are properly fixed (check the corners of the road)
                    if (this.fixTexturesIfNeeded(normal1)) {
                        topLeftTexture = reference;
                        topRightTexture = reference;
                        bottomLeftTexture = reference;
                    }
                    if (this.fixTexturesIfNeeded(normal2)) {
                        topRightTexture = reference;
                        bottomLeftTexture = reference;
                        bottomRightTexture = reference;
                    }
                }

                Vector2 topLeftTex = this.getTextureUV(topLeftTexture,true, true, x, y);
                Vector2 topRightTex = this.getTextureUV(topRightTexture, true, false, x + 1, y);
                Vector2 bottomLeftTex = this.getTextureUV(bottomLeftTexture, false, true, x, y + 1);
                Vector2 bottomRightTex = this.getTextureUV(bottomRightTexture, false, false, x + 1, y + 1);

                indexI = this.addIndices(indexI, indexI, indexI + 1, indexI + 2, indexI + 3, indexI + 4, indexI + 5);

                vertexI = this.addVertex(vertexI, topLeft, normal1, topLeftTex);
                vertexI = this.addVertex(vertexI, bottomLeft, normal1, bottomLeftTex);
                vertexI = this.addVertex(vertexI, topRight, normal1, topRightTex);
                vertexI = this.addVertex(vertexI, topRight, normal2, topRightTex);
                vertexI = this.addVertex(vertexI, bottomLeft, normal2, bottomLeftTex);
                vertexI = this.addVertex(vertexI, bottomRight, normal2, bottomRightTex);
            }
        }
    }

    /**
     * Gets the world-space vertex at that location.
     * @param x the x-coordinate of the heightmap
     * @param y the y-coordinate of the heightmap
     * @return a world-space representation
     */
    public Vector3 getVertex(int x, int y) {
        float height = this.getTrackHeightmap().getHeightmap()[x + y * this.getTrackHeightmap().getWidth()];

        float mWidth = this.getTrackHeightmap().getWidth() - 1;
        float mHeight = this.getTrackHeightmap().getHeight() - 1;

        // Compute the local origin
        Vector3 min = new Vector3(0f, this.getTrackHeightmap().getMinHeight(), 0);
        Vector3 max = new Vector3(mWidth, this.getTrackHeightmap().getMaxHeight(), mHeight);
        Vector3 center = new Vector3(min).add(max).scl(0.5f);
        Vector3 vertex = new Vector3(
                (-mWidth / 2f) + x,
                height - center.y,
                (-mHeight / 2f) + y
        );

        vertex.scl(this.getTrackHeightmap().getScaling());

        return vertex;
    }

    private Vector2 getTextureUV(TrackTexture texture, boolean top, boolean left, int x, int y) {
        Rectangle rectangle = Content.getMaterialTextureUV(texture);
        float u = rectangle.x;
        if (!left) {
            u = rectangle.x + rectangle.width;
        }
        float v = rectangle.y;
        if (!top) {
            v = rectangle.y + rectangle.height;
        }

        return new Vector2(u, v);
    }

    /**
     * Fixes the textures created from the heightmap to avoid conflicting textures in inclines.
     * @return a texture for all four vertices
     */
    private boolean fixTexturesIfNeeded(Vector3 normal) {
        return normal.dot(Vector3.Y) < 0.95;
    }

    private TrackTexture findNotRoad(TrackTexture... textures) {
        for (TrackTexture texture : textures) {
            if (texture != TrackTexture.ROAD) {
                return texture;
            }
        }
        return TrackTexture.NONE;
    }

    private Vector3 getNormal(Vector3 point1, Vector3 point2, Vector3 point3) {
        Vector3 diff1 = new Vector3(point2).sub(point1);
        Vector3 diff2 = new Vector3(point3).sub(point1);

        return diff1.crs(diff2).nor();
    }

    private int addVertex(int counter, Vector3 vertex, Vector3 normal, Vector2 uv) {
        this.vertices[counter] = vertex.x;
        this.vertices[counter + 1] = vertex.y;
        this.vertices[counter + 2] = vertex.z;
        this.vertices[counter + 3] = normal.x;
        this.vertices[counter + 4] = normal.y;
        this.vertices[counter + 5] = normal.z;
        this.vertices[counter + 6] = uv.x;
        this.vertices[counter + 7] = uv.y;
        return counter + this.getComponentsPerVertex();
    }

    private int addIndex(int counter, short vertexNumber) {
        this.indices[counter] = vertexNumber;
        return counter + 1;
    }

    private int addIndices(int counter, int... indices) {
        int newCounter = counter;
        for (int index : indices) {
            this.indices[newCounter] = (short) index;
            newCounter++;
        }

        return newCounter;
    }

    private void compileVerticesAndIndices() {

    }

    private int getAmountOfVertices() {
        return vertices.length / this.getComponentsPerVertex();
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        Renderable renderable = pool.obtain();
        renderable.worldTransform.idt();
        renderable.material = testMaterial;
        renderable.meshPart.mesh = trackMesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = this.indices.length;
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderables.add(renderable);
    }

    public void dispose() {
        trackHeightmap.dispose();
    }
}

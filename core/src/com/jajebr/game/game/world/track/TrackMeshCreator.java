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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ShortArray;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;

/**
 * Creates the mesh for the track.
 */
public class TrackMeshCreator implements RenderableProvider {
    private FloatArray vertexArray;
    private ShortArray indicesArray;
    private float[] vertices;
    private short[] indices;

    private Mesh trackMesh;
    private Material material;

    /**
     * Returns the array containing the track.
     * One vertex is defined as [PosX, PosY, PosZ, NX, NY, NZ]
     * @return the vertices array
     */
    public float[] getVertices() {
        return vertices;
    }
    /**
     * Returns an array of indices for the vertices
     * @return the index array
     */
    public short[] getIndices() {
        return indicesArray.toArray();
    }

    /**
     * Returns the number of floats that create a vertex.
     * @return the number of floats that create a vertex
     */
    public int getComponentsPerVertex() {
        return 6;
    }

    /**
     * Returns the amount of vertices.
     * @return the amount of vertices
     */
    public int getAmountOfVertices() {
        return vertices.length / getComponentsPerVertex();
    }

    public TrackMeshCreator() {
        material = new Material(
                ColorAttribute.createDiffuse(Color.CHARTREUSE)
        );
        vertexArray = new FloatArray();
        indicesArray = new ShortArray();
        createMesh();
    }

    /**
     * Creates the track mesh.
     */
    public void createMesh() {
        createVerticesAndIndices();
        compileVerticesAndIndices();

        trackMesh = new Mesh(
                true,
                this.getAmountOfVertices(),
                indices.length,
                VertexAttribute.Position(),
                VertexAttribute.Normal()
        );
        trackMesh.setVertices(getVertices());
        trackMesh.setIndices(getIndices());
    }

    /**
     * Creates the vertices and indices associated with the track.
     */
    public void createVerticesAndIndices() {
        createRandomLines();
    }

    public void createRandomLines() {
        Vector3 point1 = new Vector3(0, 0, 0);
        Vector3 point2 = new Vector3(MathUtils.random(500, 2000), 0, MathUtils.random(500, 2000));
        int lines = 10;
        for (int i = 0; i < lines; i++) {
            createLineWithWalls(
                    point1,
                    point2,
                    750f,
                    MathUtils.random(200f, 350f)
            );

            point1.set(point2);
            point2.add(MathUtils.random(-500, 500), 0, MathUtils.random(500, 2000));
        }
    }

    private void createLine(Vector3 point1, Vector3 point2, float width, float height) {
        Vector3 displacement = point2.cpy().sub(point1);
        Vector3 left = displacement.cpy().crs(Vector3.Y).nor().scl(width / 2f);
        Vector3 right = left.cpy().scl(-width / 2f);

        Vector3 temp = new Vector3();

        int currentIndex = vertexArray.size / getComponentsPerVertex();
        temp.set(point1).add(left);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);
        temp.set(point2).add(left);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);
        temp.set(point1).add(right);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);
        temp.set(point2).add(right);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);

        addIndexWithOffset(currentIndex, 0, 1, 2, 3, 2, 1);
    }

    private void createLineWithWalls(Vector3 point1, Vector3 point2, float width, float height) {
        Vector3 displacement = point2.cpy().sub(point1);
        Vector3 left = displacement.cpy().crs(Vector3.Y).nor();
        Vector3 right = left.cpy().scl(-1);
        Vector3 leftOfTrack = left.cpy().scl(width / 2f);
        Vector3 rightOfTrack = right.cpy().scl(width / 2f);

        Vector3 temp = new Vector3();

        int currentIndex = vertexArray.size / getComponentsPerVertex();
        // Ground track
        temp.set(point1).add(leftOfTrack);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);
        temp.set(point2).add(leftOfTrack);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);
        temp.set(point1).add(rightOfTrack);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);
        temp.set(point2).add(rightOfTrack);
        addVertex(temp.x, temp.y, temp.z, 0, 1, 0);

        temp.set(point1).add(leftOfTrack).add(0, height, 0);
        addVertex(temp.x, temp.y, temp.z, right.x, right.y, right.z);
        temp.set(point2).add(leftOfTrack).add(0, height, 0);
        addVertex(temp.x, temp.y, temp.z, right.x, right.y, right.z);
        temp.set(point1).add(rightOfTrack).add(0, height, 0);
        addVertex(temp.x, temp.y, temp.z, left.x, left.y, left.z);
        temp.set(point2).add(rightOfTrack).add(0, height, 0);
        addVertex(temp.x, temp.y, temp.z, left.x, left.y, left.z);

        // Ground track
        addIndexWithOffset(currentIndex, 0, 1, 2, 3, 2, 1);
        // Left track wall
        addIndexWithOffset(currentIndex, 4, 5, 0, 1, 0, 5);
        // Right track wall
        addIndexWithOffset(currentIndex, 3, 6, 2, 7, 6, 3);
    }

    /**
     * Creates a test track.
     */
    private void createTestVerticesAndIndices() {
        addVertex(0, 0, 0, 0, 1, 0);
        addVertex(0, 0, 500, 0, 1, 0);
        addVertex(500, 0, 0, 0, 1, 0);
        addVertex(500, 0, 500, 0, 1, 0);

        addVertex(200, 0, 200, 1, 0, 0);
        addVertex(200, 0, 300, 1, 0, 0);
        addVertex(200, 300, 200, 1, 0, 0);
        addVertex(200, 300, 300, 1, 0, 0);
        addIndex(0, 1, 2, 3, 2, 1, 4, 5, 6, 7, 6, 5);
    }

    /**
     * Adds a vertex onto the vertex array.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @param nx the normal x-coordinate
     * @param ny the normal y-coordinate
     * @param nz the normal z-coordinate
     */
    public void addVertex(float x, float y, float z, float nx, float ny, float nz) {
        vertexArray.addAll(x, y, z, nx, ny, nz);
    }

    /**
     * Adds an index onto the index array.
     * @param indices the indices to add
     */
    public void addIndex(short... indices) {
        indicesArray.addAll(indices);
    }

    /**
     * Adds an index onto the index array.
     * @param indices the indices to add
     */
    public void addIndex(int... indices) {
        for (int index : indices) {
            indicesArray.add(index);
        }
    }

    /**
     * Adds an index onto the index array, offseted by a constant.
     * @param offset the offset of the vertex
     * @param indices the indices to add, not offseted
     */
    public void addIndexWithOffset(int offset, int... indices) {
        for (int index : indices) {
            indicesArray.add(index + offset);
        }
    }

    /**
     * Creates the vertex and index arrays for caching.
     */
    public void compileVerticesAndIndices() {
        vertices = vertexArray.toArray();
        indices = indicesArray.toArray();
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        Renderable renderable = pool.obtain();
        renderable.worldTransform.idt();
        renderable.material = material;
        renderable.meshPart.mesh = trackMesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = this.indices.length;
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderables.add(renderable);
    }
}

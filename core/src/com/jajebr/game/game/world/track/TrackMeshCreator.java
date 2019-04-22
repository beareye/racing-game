package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ShortArray;

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
        material = new Material(new ColorAttribute(ColorAttribute.Diffuse, Color.FIREBRICK));
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
        addVertex(0, 0, 0, 0, 1, 0);
        addVertex(0, 0, 300, 0, 1, 0);
        addVertex(300, 0, 0, 0, 1, 0);
        addVertex(300, 0, 300, 0, 1, 0);
        addIndex(0, 1, 2, 3, 2, 1);
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

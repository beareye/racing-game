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
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ShortArray;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.Content;

/**
 * Creates the mesh for the track.
 */
public class TrackMesh implements RenderableProvider {
    private TrackHeightmap trackHeightmap;

    /**
     * Returns the track heightmap object.
     * @return the track heightmap
     */
    public TrackHeightmap getTrackHeightmap() {
        return trackHeightmap;
    }

    public TrackMesh() {
        trackHeightmap = new TrackHeightmap();
    }

    private void createVerticesFromHeightmap() {

    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        /*
        Renderable renderable = pool.obtain();
        renderable.worldTransform.idt();
        renderable.material = material;
        renderable.meshPart.mesh = trackMesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = this.indices.length;
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderables.add(renderable);
        */
    }

    public void dispose() {
        trackHeightmap.dispose();
    }
}

package com.jajebr.game.game.world.track;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.jajebr.game.engine.Director;
import com.jajebr.game.game.entity.EntityCar;
import com.jajebr.game.game.world.World;

/**
 * Track for the game.
 */
public class Track {
    private String name;
    private TrackMesh trackMesh;
    private World world;
    private Pixmap pixmap;
    private Texture pixmapTexture;
    private int amountOfLaps;

    private float accceptableDistanceSquaredToGoal;

    private Vector3 startingPosition;

    /**
     * Returns the name of the track.
     * @return the name of the track
     */
    public String getName() {
        return name;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public Texture getPixmapTexture() {
        return pixmapTexture;
    }

    /**
     * Returns the world associated by the track.
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    public TrackMesh getTrackMesh() {
        return trackMesh;
    }
    public TrackHeightmap getTrackHeightmap() {
        return this.trackMesh.getTrackHeightmap();
    }

    public Vector3 getStartingPosition() {
        return this.startingPosition;
    }

    /**
     * Returns an acceptable distance to the goal, squared.
     * Used for checking laps.
     * @return the acceptable distance to the goal, squared
     */
    public float getAccceptableDistanceSquaredToGoal() {
        return this.accceptableDistanceSquaredToGoal;
    }

    public int getAmountOfLaps() {
        return this.amountOfLaps;
    }

    /**
     * Initializes an empty track.
     * @param newName the name of the track
     */
    public Track(World newWorld, String newName) {
        this.name = newName;
        this.trackMesh = new TrackMesh();
        this.world = newWorld;

        accceptableDistanceSquaredToGoal = this.trackMesh.getTrackHeightmap().getScaling().x * this.trackMesh.getTrackHeightmap().getScaling().z * 100;

        this.startingPosition = new Vector3();
        this.assignStartingPosition();

        this.pixmap = this.createPixmap();
        this.pixmapTexture = new Texture(this.pixmap);

        this.amountOfLaps = 3;
    }

    private void assignStartingPosition() {
        Vector2 starting = this.getTrackHeightmap().getTrackCreator().getStartingPosition();
        Vector3 worldStarting = this.trackMesh.getVertex((int) starting.x, (int) starting.y);
        worldStarting.add(0f, 10f, 0f);
        this.startingPosition.set(worldStarting);
    }

    public Vector3 getStartingPositionFrom2DCoordinates(Vector2 coordinates) {
        Vector3 worldStarting = this.trackMesh.getVertex((int) coordinates.x, (int) coordinates.y);
        worldStarting.add(0f, 10f, 0f);
        return worldStarting;
    }

    private Pixmap createPixmap() {
        int width = this.getTrackHeightmap().getWidth();
        int height = this.getTrackHeightmap().getHeight();
        Pixmap minimap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TrackTexture texture = this.getTrackHeightmap().getTextureAt(x, y);
                minimap.drawPixel(x, y, Color.rgba8888(texture.getColor()));
            }
        }

        return minimap;
    }

    public void draw(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(this.trackMesh, environment);
    }

    public void dispose() {
        trackMesh.dispose();
        this.pixmap.dispose();;
    }
}

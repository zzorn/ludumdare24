package org.ludumdare24.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.gameflow.entity.Entity;

/**
 * Draws tile map
 */
public class TileMap extends Entity {

    private static final int WORLD_SIZE_X = 10;
    private static final int WORLD_SIZE_Y = 20;

    private TextureAtlas.AtlasRegion[] tileRegions;

    private final int worldSizeX;
    private final int worldSizeY;
    private float grassTileSizeX = 400;
    private float grassTileSizeY = 200;

    private int[] tiles;
    private int tileCount = 4;



    public TileMap() {
        this(WORLD_SIZE_X, WORLD_SIZE_Y);
    }

    public TileMap(int worldSizeX, int worldSizeY) {
        this.worldSizeX = worldSizeX;
        this.worldSizeY = worldSizeY;

        tiles = new int[worldSizeX * worldSizeY];
        for (int i = 0; i < worldSizeX * worldSizeY; i++) {
            tiles[i] = (int) ((tileCount-1) * Math.random());
        }

        tileRegions = new TextureAtlas.AtlasRegion[tileCount];
        for (int i = 0; i < tileCount; i++) {
            tileRegions[i] = null;
        }
    }

    @Override
    protected void onCreate(TextureAtlas atlas) {
    }

    public void update(float deltaTime) {

    }

    @Override
    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        for (int i = 0; i < tileCount; i++) {
            if (tileRegions[i] == null) tileRegions[i] = atlas.findRegion("grassterrain" + (i + 1));
        }

        // Grass
        for (int y = 0; y < worldSizeY; y++) {
            for (int x = 0; x < worldSizeX; x++) {

                float xpos = grassTileSizeX * (x - worldSizeX / 2);
                float ypos = grassTileSizeY * (y - worldSizeY / 2);

                TextureAtlas.AtlasRegion tileRegion = tileRegions[tiles[x + y * worldSizeX]];

                spriteBatch.draw(tileRegion, xpos, ypos, grassTileSizeX, grassTileSizeY);
            }
        }
    }

    private void drawSection(SpriteBatch spriteBatch, TextureAtlas.AtlasRegion region, int subX, int subY, float x, float y, float tileSizeX, float tileSizeY) {
        float subSectionSize = 0.25f;

        float u1 = region.getU();
        float v1 = region.getV();
        float su = region.getU2() - region.getU();
        float sv = region.getV2() - region.getV();

        float tu1 = u1 + su * subSectionSize * subX;
        float tv1 = v1 + sv * subSectionSize * subY;
        float tu2 = tu1 + su * subSectionSize;
        float tv2 = tv1 + sv * subSectionSize;

        spriteBatch.draw(region.getTexture(), x, y, tileSizeX, tileSizeY, tu1, tv1, tu2, tv2);
    }

    @Override
    public void onDispose() {
    }

    @Override
    public float getDrawOrder() {
        return Float.NEGATIVE_INFINITY;
    }
}


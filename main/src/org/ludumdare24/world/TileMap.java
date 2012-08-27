package org.ludumdare24.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.gameflow.entity.Entity;

/**
 * Draws tile map
 */
public class TileMap extends Entity {

    private TextureAtlas.AtlasRegion sand;
    private TextureAtlas.AtlasRegion grass;
    private TextureAtlas.AtlasRegion snow;

    private final int worldCellSizeX;
    private final int worldCellSizeY;
    private float tileSize = 256;




    public TileMap() {
        worldCellSizeX = 20;
        worldCellSizeY = 20;

    }

    public TileMap(int worldCellSizeX, int worldCellSizeY) {
        this.worldCellSizeX = worldCellSizeX;
        this.worldCellSizeY = worldCellSizeY;
    }

    @Override
    protected void onCreate(TextureAtlas atlas) {
    }

    public void update(float deltaTime) {

    }

    @Override
    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        if (sand == null) sand = atlas.findRegion("sand");
        if (grass == null) grass = atlas.findRegion("grass");
        if (snow == null) snow = atlas.findRegion("snow");

        // Sand
        for (int cy = -worldCellSizeY/2; cy < worldCellSizeY/2; cy++) {
            for (int cx = -worldCellSizeX/2; cx < worldCellSizeX/2; cx++) {

                float x = tileSize * cy;
                float y = tileSize * cx;
                spriteBatch.draw(sand, x, y, tileSize, tileSize);
            }
        }

        // Grass
        for (int cy = -worldCellSizeY/2; cy < worldCellSizeY/2; cy++) {
            for (int cx = -worldCellSizeX/2; cx < worldCellSizeX/2; cx++) {

                float x = tileSize * cy;
                float y = tileSize * cx;

                int subX = 1;
                int suby = 2;
                float subSectionSize = 0.25f;


                float u1 = grass.getU();
                float v1 = grass.getV();
                float u2 = grass.getU2();
                float v2 = grass.getV2();
                float su = u2 - u1;
                float sv = v2 - v1;

               // spriteBatch.draw(grass.getTexture(), x, y, tileSize, tileSize, u, v, u2, v2);
            }
        }


    }

    @Override
    public void onDispose() {
    }

    @Override
    public float getDrawOrder() {
        return Float.NEGATIVE_INFINITY;
    }
}


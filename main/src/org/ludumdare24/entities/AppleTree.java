package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.gameflow.entity.Entity;
import org.gameflow.utils.ImageRef;
import org.ludumdare24.world.FoodType;
import org.ludumdare24.world.GameWorld;

import java.util.Random;

/**
 *
 */
public class AppleTree extends SimpleWorldEntity {

    private final GameWorld gameWorld;
    private double timeUntilSpawn = 10;
    private double spawnInterval = 30;
    private FoodType foodType = FoodType.APPLE;
    private double energySpawnedAtOnce = foodType.getEnergyInOne() * 4.5;

    private ImageRef shadow;

    public AppleTree(GameWorld gameWorld) {
        this(gameWorld, new Random());
    }

    public AppleTree(GameWorld gameWorld, Random random) {
        super(createImage(random));
        this.gameWorld = gameWorld;

        timeUntilSpawn = spawnInterval*0.60 * random.nextDouble();
    }

    private static ImageRef createImage(Random random) {
        ImageRef image = new ImageRef("appleTree", random.nextInt(4) + 1);
        image.setMirrorX(random.nextBoolean());
        return image;
    }

    @Override
    protected void onCreate(TextureAtlas atlas) {
        super.onCreate(atlas);

        // Shadow
        shadow = new ImageRef("appleTreeShadow", 0, 1, 1);

    }

    @Override
    protected void onUpdate(float timeDelta) {
        timeUntilSpawn -= timeDelta;

        // Spawn apples now and again.
        if (timeUntilSpawn < 0) {
            double energy = energySpawnedAtOnce * (gameWorld.getRandom().nextGaussian() * 0.3 + 1.0);
            gameWorld.spawnFood(foodType, getWorldPos().x, getWorldPos().y, energy);

            timeUntilSpawn = spawnInterval * (gameWorld.getRandom().nextGaussian() * 0.3 + 1.0);
        }

    }

    @Override
    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        // Render shadow
        shadow.render(getX(), getY()-16, atlas, spriteBatch);

        super.render(atlas, spriteBatch);
    }
}

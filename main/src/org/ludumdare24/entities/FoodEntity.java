package org.ludumdare24.entities;

import org.gameflow.utils.ImageRef;
import org.ludumdare24.world.FoodType;
import org.ludumdare24.world.GameWorld;

import java.util.Random;

/**
 *
 */
public class FoodEntity extends SimpleWorldEntity {
    private static final double START_FADE_IN_TIME = 1;

    private final GameWorld gameWorld;
    private final FoodType foodType;
    private double energyLeft;
    private double timeLeft;
    private double fadeInTimeLeft = START_FADE_IN_TIME;
    private double baseScale = 1;



    public FoodEntity(GameWorld gameWorld, Random random, FoodType foodType) {
        this(gameWorld, random, foodType, 1);
    }


    public FoodEntity(GameWorld gameWorld, Random random, FoodType foodType, double partFull) {
        super(createImage(random, foodType, partFull));
        this.gameWorld = gameWorld;
        this.foodType = foodType;

        energyLeft = foodType.getEnergyInOne() * partFull;
        timeLeft = foodType.getDurationSeconds() * (random.nextGaussian() * 0.1 + 1.0);
        baseScale = (random.nextGaussian() * 0.1 + 1.0);

        updateAppearance();
    }

    private static ImageRef createImage(Random random, FoodType foodType, double partFull) {
        int imageIndex = random.nextInt(foodType.getImageCount()) + 1;
        ImageRef image = new ImageRef(foodType.getImage(), imageIndex);

        image.setMirrorX(random.nextBoolean());
        image.setAngleTurns(random.nextGaussian() * 0.05);

        return image;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    @Override
    protected void onUpdate(float timeDelta) {
        timeLeft -= timeDelta;
        fadeInTimeLeft -= timeDelta;

        updateAppearance();

        // Dissappear if expired
        if (timeLeft < 0) {
            gameWorld.removeEntity(this);
        }
    }

    public double getEnergyLeft() {
        return energyLeft;
    }

    /**
     * Eat on this food.
     * @param energyEaten how much energy to try to get out
     * @return energy that actually got out.
     */
    public double eat(double energyEaten) {
        if (energyLeft < energyEaten) {
            // Dissappear this food, it's eaten
            gameWorld.removeEntity(this);

            return energyLeft;
        }
        else {
            energyLeft -= energyEaten;
            return energyEaten;
        }
    }

    private void updateAppearance() {
        // Size size
        double energyLeftSizeFactor = this.energyLeft / this.foodType.getEnergyInOne();
        double timeLeftFactor = timeLeft > 5 ? 1 : timeLeft / 5.0;
        getImageRef().setScale(this.foodType.getImageScale() * energyLeftSizeFactor * timeLeftFactor * baseScale);

        // Adjust transparency
        double fadeInFactor = Math.min(1.0, 1.0 - fadeInTimeLeft / START_FADE_IN_TIME);
        getImageRef().setAlpha(fadeInFactor * timeLeftFactor);
    }

}

package org.ludumdare24.world;

/**
 * The different types of foods in the world.
 */
public enum FoodType {

    APPLE(10, "food", 1, 15, 0.3),
    MEAT(100, "meatFood", 1, 20, 0.5),
    ;

    private final double energyInOne;
    private final String image;
    private final int imageCount;
    private final double durationSeconds;
    private final double imageScale;

    private FoodType(double energyInOne, String image, int imageCount, double durationSeconds, double imageScale) {
        this.energyInOne = energyInOne;
        this.image = image;
        this.imageCount = imageCount;
        this.durationSeconds = durationSeconds;
        this.imageScale = imageScale;
    }

    public double getEnergyInOne() {
        return energyInOne;
    }

    public String getImage() {
        return image;
    }

    public int getImageCount() {
        return imageCount;
    }

    public double getDurationSeconds() {
        return durationSeconds;
    }

    public double getImageScale() {
        return imageScale;
    }
}

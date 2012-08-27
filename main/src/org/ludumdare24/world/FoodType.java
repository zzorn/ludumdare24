package org.ludumdare24.world;

/**
 * The different types of foods in the world.
 */
public enum FoodType {

    APPLE(40, "food", "particles/eaten.particle", 1, 40, 0.3),
    MEAT(100, "meatFood", "particles/eatenMeat.particle", 1, 20, 1),
    ;

    private final double energyInOne;
    private final String image;
    private final String particles;
    private final int imageCount;
    private final double durationSeconds;
    private final double imageScale;

    private FoodType(double energyInOne, String image, String particles, int imageCount, double durationSeconds, double imageScale) {
        this.energyInOne = energyInOne;
        this.image = image;
        this.particles = particles;
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

    public String getParticles() {
        return particles;
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

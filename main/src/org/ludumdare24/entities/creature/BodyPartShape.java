package org.ludumdare24.entities.creature;

import org.gameflow.utils.MathTools;

/**
 * Shapes that a creatures body part can have
 */
public enum BodyPartShape {

    HEAD("head",         48, 64, 0, 3, 3, 3),
    TORSO("torso",       40, 28, 0, 0, 0, 0),
    ABDOMEN("abdomen",   30, 28, 0, 0, 0, 0),
    ARM("arm",           16, 54, 0, 0, 0, 0),
    LEG("leg",           10, 58, 0, 0, 0, 0),
    EYE("eye",            8,  8, 6, 0, 0, 0),

    ;

    private final String imageName;
    private final int width;
    private final int height;
    private final int basicCount;
    private final int hairCount;
    private final int armorCount;
    private final int spikeCount;

    private BodyPartShape(String imageName, int width, int height, int basicCount, int hairCount, int armorCount, int spikeCount) {
        this.imageName = imageName;
        this.width = width;
        this.height = height;
        this.basicCount = basicCount;
        this.hairCount = hairCount;
        this.armorCount = armorCount;
        this.spikeCount = spikeCount;
    }

    public String getImageName() {
        return imageName;
    }

    public int getVisibleWidth() {
        return width;
    }

    public int getVisibleHeight() {
        return height;
    }

    public String getBasicImageName(double basic) {
        int image = (int) MathTools.mix(basic, 0, basicCount);
        if (image <= 0) return imageName;
        else return imageName + image;
    }

    public String getHairImageName(double hair) {
        int image = (int) MathTools.mix(hair, 0, hairCount);
        if (image <= 0) return null;
        else return imageName + "_hair" + image;
    }

    public String getArmorImageName(double armor) {
        int image = (int) MathTools.mix(armor, 0, armorCount);
        if (image <= 0) return null;
        else return imageName + "_armor" + image;
    }

    public String getSpikeImageName(double spikes) {
        int image = (int) MathTools.mix(spikes, 0, spikeCount);
        if (image <= 0) return null;
        else return imageName + "_spikes" + image;
    }
}

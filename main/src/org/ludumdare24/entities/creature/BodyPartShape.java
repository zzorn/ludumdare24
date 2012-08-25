package org.ludumdare24.entities.creature;

import org.gameflow.utils.MathUtils;

/**
 * Shapes that a creatures body part can have
 */
public enum BodyPartShape {

    POTATO("potato1", 3, 0),
    PEAR("pear", 3, 0),

    ;

    private final String imageName;
    private final int hairCount;
    private final int armorCount;

    private BodyPartShape(String imageName, int hairCount, int armorCount) {
        this.imageName = imageName;
        this.hairCount = hairCount;
        this.armorCount = armorCount;
    }

    public String getImageName() {
        return imageName;
    }

    public String getHairImageName(double hair) {
        int hairImage = (int) MathUtils.mix(hair, 0, hairCount);
        if (hairImage <= 0) return null;
        else return imageName + "_hair" + hairImage;
    }

    public String getArmorImageName(double armor) {
        int armorImage = (int) MathUtils.mix(armor, 0, armorCount);
        if (armorImage <= 0) return null;
        else return imageName + "_armor" + armorImage;
    }
}

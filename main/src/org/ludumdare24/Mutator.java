package org.ludumdare24;

import com.badlogic.gdx.graphics.Color;
import org.gameflow.utils.ColorUtils;
import org.gameflow.utils.MathTools;

import java.util.Random;

/**
 * Handles mutating values together.
 */
public class Mutator {
    private double randomMutationRate = 0.15;
    private double mixMutationRate = 0.3;
    private double mixMutationAmount = 0.1;
    private double motherInheritRate = 0.5;

    private final Random random;

    public Mutator(Random random) {
        this.random = random;
    }

    public Mutator(Random random, double randomMutationRate, double mixMutationRate, double mixMutationAmount) {
        this.random = random;
        this.randomMutationRate = randomMutationRate;
        this.mixMutationRate = mixMutationRate;
        this.mixMutationAmount = mixMutationAmount;
    }

    public Mutator(Random random, double randomMutationRate, double mixMutationRate, double mixMutationAmount, double motherInheritRate) {
        this.random = random;
        this.randomMutationRate = randomMutationRate;
        this.mixMutationRate = mixMutationRate;
        this.mixMutationAmount = mixMutationAmount;
        this.motherInheritRate = motherInheritRate;
    }

    public Random getRandom() {
        return random;
    }

    public double randomize() {
        return random.nextDouble();
    }

    public double randomize(double min, double max) {
        return MathTools.mix(random.nextDouble(), min, max);
    }

    public double randomizeMinusOneToOne() {
        return random.nextDouble() * 2 - 1.0;
    }

    public Color randomizeColor() {
        return ColorUtils.randomColor(random);
    }

    public double mutate(double mother) {
        return mix(mother, mother, false);
    }

    public double mutate(double mother, boolean rollInsteadOfClamp) {
        return mix(mother, mother, rollInsteadOfClamp);
    }

    public double mutate(double mother, boolean rollInsteadOfClamp, boolean allowNegative) {
        return mix(mother, mother, rollInsteadOfClamp, allowNegative);
    }

    public double mix(double mother, double father) {
        return mix(mother, father, false);
    }

    public double mix(double mother, double father, boolean rollInsteadOfClamp) {
        return mix(mother, father, rollInsteadOfClamp, false);

    }

    public double mix(double mother, double father, boolean rollInsteadOfClamp, boolean allowNegative) {
        if (random.nextFloat() < randomMutationRate) {
            if (allowNegative) return random.nextDouble() * 2.0 - 1.0;
            else return random.nextDouble();
        }
        else if (random.nextFloat() < mixMutationRate) {
            double mutationMix = random.nextDouble() + random.nextGaussian() * mixMutationAmount;
            if (rollInsteadOfClamp) {
                return MathTools.rollToZeroToOne(MathTools.mix(mutationMix, mother, father));
            }
            else {
                if (allowNegative) return MathTools.clampToMinusOneToOne(MathTools.mix(mutationMix, mother, father));
                else return MathTools.clampToZeroToOne(MathTools.mix(mutationMix, mother, father));
            }
        }
        else if (random.nextFloat() < motherInheritRate) {
            return mother;
        }
        else {
            return father;
        }
    }



}

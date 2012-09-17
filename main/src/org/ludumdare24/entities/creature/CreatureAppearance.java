package org.ludumdare24.entities.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import org.gameflow.utils.ColorUtils;
import org.gameflow.utils.ImageRef;
import org.ludumdare24.Mutator;

import static org.gameflow.utils.MathTools.*;
import static org.gameflow.utils.MathTools.clamp;


/**
 * Messy troll drawing code here.
 */
public class CreatureAppearance {
    private static final int BODY_IMAGE_SIZE = 64;
    private static final float OBSERVATION_SCALING_FACTOR = 2.0f;

    private static final float ADJUST_DAMPENING = 0.8f;
    private static final float MIN_LUM = 0.15f;
    private static final float MAX_LUM = 0.85f;

    private final Creature creature;

    private float scale = 0.5f;

    private double hatStyle;

    private double basicShape;

    private double hair;
    private double armor;
    private double spikes;
    private double fatness;
    private double length;

    private Color skinColor;
    private Color armorColor;
    private Color spikesColor;
    private Color hairColor;

    private CreaturePart rightLeg;
    private CreaturePart leftLeg;
    private CreaturePart rightArm;
    private CreaturePart leftArm;
    private CreaturePart abdomen;
    private CreaturePart torso;
    private CreaturePart head;
    private CreaturePart leftEye;
    private CreaturePart rightEye;
    private CreaturePart hatPart;

    private double armWaveOffset = 10 * Math.random();
    private double legWaveOffset = 10 * Math.random();

    private double totalTime = 0;

    private Array<CreaturePart> parts = new Array<CreaturePart>();
    private double armorHueAdjust;
    private double armorSatAdjust;
    private double armorLumAdjust;
    private double hairHueAdjust;
    private double hairSatAdjust;
    private double hairLumAdjust;
    private double spikeLum;
    private double baseHue;
    private double baseSat;
    private double baseLum;

    private ParticleEffect ownerGodGlowEffect = null;

    private ImageRef shadow;

    public CreatureAppearance(Creature creature,
                              Color baseColor,
                              Mutator mutator,
                              double hatStyle) {
        this.creature = creature;

        initCreatureProperties(creature);

        armorHueAdjust = mutator.getRandom().nextGaussian() * 0.1;
        armorSatAdjust = mutator.getRandom().nextGaussian() * 0.2;
        armorLumAdjust = mutator.getRandom().nextGaussian() * 0.1 - 0.2;
        hairHueAdjust = mutator.getRandom().nextGaussian() * 0.2;
        hairSatAdjust = mutator.getRandom().nextGaussian() * 0.1;
        hairLumAdjust = mutator.getRandom().nextGaussian() * 0.2;
        spikeLum = mutator.getRandom().nextDouble();
        baseHue = ColorUtils.hue(baseColor);
        baseSat = ColorUtils.sat(baseColor);
        baseLum = ColorUtils.lum(baseColor);
        this.hatStyle = hatStyle;

        buildColors();
    }

    public CreatureAppearance(Creature creature,
                              CreatureAppearance mother,
                              Mutator mutator) {
        this.creature = creature;

        initCreatureProperties(creature);

        baseHue = mutator.mutate(mother.baseHue, true);
        baseSat = mutator.mutate(mother.baseSat);
        baseLum = clamp(mutator.mutate(mother.baseLum), MIN_LUM, MAX_LUM);
        armorHueAdjust = mutator.mutate(mother.armorHueAdjust, true) * ADJUST_DAMPENING;
        armorSatAdjust = mutator.mutate(mother.armorSatAdjust, false, true) * ADJUST_DAMPENING;
        armorLumAdjust = mutator.mutate(mother.armorLumAdjust, false, true) * ADJUST_DAMPENING;
        hairHueAdjust = mutator.mutate(mother.hairHueAdjust, true) * ADJUST_DAMPENING;
        hairSatAdjust = mutator.mutate(mother.hairSatAdjust, false, true) * ADJUST_DAMPENING;
        hairLumAdjust = mutator.mutate(mother.hairLumAdjust, false, true) * ADJUST_DAMPENING;
        spikeLum = mutator.mutate(mother.spikeLum);
        this.hatStyle = mother.hatStyle;

        buildColors();
    }

    public CreatureAppearance(Creature creature,
                              CreatureAppearance mother,
                              CreatureAppearance father,
                              Mutator mutator) {
        this.creature = creature;

        initCreatureProperties(creature);

        baseHue = mutator.mix(mother.baseHue, father.baseHue, true);
        baseSat = mutator.mix(mother.baseSat, father.baseSat);
        baseLum = clamp(mutator.mix(mother.baseLum, father.baseLum), MIN_LUM, MAX_LUM);
        armorHueAdjust = mutator.mix(mother.armorHueAdjust, father.armorHueAdjust, true) * ADJUST_DAMPENING;
        armorSatAdjust = mutator.mix(mother.armorSatAdjust, father.armorSatAdjust, false, true) * ADJUST_DAMPENING;
        armorLumAdjust = mutator.mix(mother.armorLumAdjust, father.armorLumAdjust, false, true) * ADJUST_DAMPENING;
        hairHueAdjust = mutator.mix(mother.hairHueAdjust, father.hairHueAdjust, true) * ADJUST_DAMPENING;
        hairSatAdjust = mutator.mix(mother.hairSatAdjust, father.hairSatAdjust, false, true) * ADJUST_DAMPENING;
        hairLumAdjust = mutator.mix(mother.hairLumAdjust, father.hairLumAdjust, false, true) * ADJUST_DAMPENING;
        spikeLum = mutator.mix(mother.spikeLum, father.spikeLum);
        this.hatStyle = mother.hatStyle;

        buildColors();
    }

    /**
     * @return 0 not very similar at all, 1 very similar.
     */
    public double similarity(CreatureAppearance other) {
        return clampToZeroToOne(
                hueSimilarity(baseHue, other.baseHue) *
                        hueSimilarity(baseHue, other.baseHue) *
                        hueSimilarity(baseHue, other.baseHue) *
                        hueSimilarity(baseHue, other.baseHue) *
                        valueSimilarity(baseSat, other.baseSat) *
                        valueSimilarity(baseSat, other.baseSat) *
                        valueSimilarity(baseLum, other.baseLum) *
                        valueSimilarity(baseLum, other.baseLum) *
                        valueSimilarity(baseLum, other.baseLum) *
                        hueSimilarity(armorHueAdjust, other.armorHueAdjust) *
                        valueSimilarity(armorLumAdjust, other.armorLumAdjust) *
                        hueSimilarity(hairHueAdjust, other.hairHueAdjust) *
                        valueSimilarity(hairLumAdjust, other.hairLumAdjust) *
                        valueSimilarity(fatness, other.fatness) *
                        valueSimilarity(length, other.length) *
                        valueSimilarity(hatStyle, other.hatStyle) * // Having the same hat is important
                        valueSimilarity(hatStyle, other.hatStyle) *
                        valueSimilarity(hatStyle, other.hatStyle) *
                        valueSimilarity(hatStyle, other.hatStyle));

    }

    private double valueSimilarity(double a, double b) {
        return 1.5 - Math.abs(b - a);
    }

    private double hueSimilarity(double a, double b) {
        return 1.5 - Math.min(Math.abs(b - a), Math.abs(b - (a+1)));
    }

    private void initCreatureProperties(Creature creature) {
        this.basicShape = creature.getBasicShape();
        this.hair = creature.getHair();
        this.armor = creature.getArmor();
        this.spikes = creature.getSpikes();
        this.fatness = creature.getFatness();
        this.length = creature.getLength();
    }

    public void create(TextureAtlas atlas) {

        head = createBodyPart(BodyPartShape.HEAD, false, skinColor, basicShape, 0);
        torso = createBodyPart(BodyPartShape.TORSO, false, skinColor, basicShape, 0);
        abdomen = createBodyPart(BodyPartShape.ABDOMEN, false, skinColor, basicShape, 0);
        leftLeg = createBodyPart(BodyPartShape.LEG, false, skinColor, basicShape, 30);
        rightLeg = createBodyPart(BodyPartShape.LEG, true, skinColor, basicShape,  30);
        leftArm  = createBodyPart(BodyPartShape.ARM, false, skinColor, basicShape,  30);
        rightArm = createBodyPart(BodyPartShape.ARM, true, skinColor, basicShape,  30);
        leftEye = createBodyPart(BodyPartShape.EYE, false, Color.WHITE, basicShape, 0);
        rightEye = createBodyPart(BodyPartShape.EYE, true, Color.WHITE, basicShape, 0);
        hatPart = createBodyPart(BodyPartShape.HAT, true, Color.WHITE, hatStyle, 0);

        double armHeight   = -15 + torso.getVisibleH() / 2 - leftArm.getVisibleH() / 2;
        double armDistance = 1.08*torso.getVisibleW() / 2 + leftArm.getVisibleW() / 2;
        addPart(leftArm, -armDistance, armHeight);
        addPart(rightArm, armDistance, armHeight);
        leftArm.setRelativeOrigin(0.65f, 0.9f);
        rightArm.setRelativeOrigin(0.65f, 0.9f);

        float headCenterY = torso.getVisibleH() / 2 + head.getVisibleH() / 2;
        addPart(head, 0, headCenterY);


        double legHeight   = -15 -torso.getVisibleH() / 2 -abdomen.getVisibleH() / 2 -leftLeg.getVisibleH() / 2;
        double legDistance = abdomen.getVisibleW() / 2;// + leftLeg.getVisibleW() / 2;
        addPart(leftLeg, -legDistance, legHeight);
        addPart(rightLeg, legDistance, legHeight);
        leftLeg.setRelativeOrigin(0.5f, 0.9f);
        rightLeg.setRelativeOrigin(0.5f, 0.9f);

        addPart(torso);
        addPart(abdomen, 0, -torso.getVisibleH()/2 - abdomen.getVisibleH()/2);

        double eyeXOffs = head.getVisibleW() / 4;
        addPart(leftEye, -eyeXOffs, headCenterY);
        addPart(rightEye, eyeXOffs, headCenterY);

        addPart(hatPart, 0, torso.getVisibleH() / 2 + head.getVisibleH() * 0.9);

        // Owner god glow effect
        ownerGodGlowEffect = new ParticleEffect();
        if (creature.getGod() != null) {
            ownerGodGlowEffect.load(Gdx.files.internal(creature.getGod().getGlowEffectName()), atlas);
            ownerGodGlowEffect.start();
        }
        else {
            ownerGodGlowEffect.load(Gdx.files.internal("particles/enemyGlow.particles"), atlas);
            ownerGodGlowEffect.start();
        }

        // Shadow
        shadow = new ImageRef("trollShadow", 0, 1, 1);
    }

    public void onUpdate(float timeDelta) {
        totalTime += timeDelta;

        // Reflect behavior
        double waveSpeed = 0;
        waveSpeed += creature.isAttacking() ? 4 : 0;
        waveSpeed += creature.getMovementSpeedFactor();

        double waveSize = creature.getEnergyStatus() * creature.getMovementSpeedFactor();
        waveSize += creature.isAttacking() ? 0.5 : 0;

        double legFlipSpeed = 3 * creature.getMovementSpeedFactor();
        double legFlipSize = 0.2;

        // Flap your arms if you are a crazy creature
        double armPos = (Math.sin(armWaveOffset + totalTime * waveSpeed * Tau) * 0.5 + 0.5) * clampToZeroToOne(waveSize);
        double armAngle = mix(armPos, (0.25 + 0.75) * Tau, (0.25 + 0.35) * Tau);
        leftArm.setAngle(armAngle);
        rightArm.setAngle(Tau - armAngle);

        // Flip your legs when you are a strange creature
        double legPos = (Math.sin(12.3*legWaveOffset + totalTime * legFlipSpeed * Tau) * 0.5 + 0.5) * clampToZeroToOne(legFlipSize);
        double legAngle = mix(legPos, 1 * Tau, 0.8 * Tau);
        leftLeg.setAngle(legAngle);
        rightLeg.setAngle(Tau - legAngle);


        // Update glow effect
        ownerGodGlowEffect.update(timeDelta);
    }

    public void render(float x, float y, TextureAtlas atlas, SpriteBatch spriteBatch) {

        float scale = creature.isObserved() ? OBSERVATION_SCALING_FACTOR : 1f;
        float moveUpFudge = 50 * scale;

        // Render worship glow
        ownerGodGlowEffect.setPosition(x, y + moveUpFudge - 10);
        ownerGodGlowEffect.draw(spriteBatch );

        // Render creature bodyparts
        for (CreaturePart part : parts) {
            part.draw(atlas, spriteBatch, x, y + moveUpFudge, 0, scale);
        }

        // Render shadow
        shadow.render(x - 16, y-8, atlas, spriteBatch);
    }


    private CreaturePart createBodyPart(BodyPartShape shape, boolean mirror, Color skinColor, double basicShape, float extraLift) {
        double h = mix(length, 0.75, 1.5);
        double w = mix(fatness, 0.75, 1.5);
        return new CreaturePart(shape, mirror, w, h, hair, armor, spikes, skinColor, hairColor, armorColor, spikesColor, basicShape, scale, extraLift);
    }

    private void addPart(CreaturePart part) {
        addPart(part, 0, 0, 0);
    }

    private void addPart(CreaturePart part, double xOffs, double yOffs) {
        addPart(part, xOffs, yOffs, 0);
    }

    private void addPart(CreaturePart part, double xOffs, double yOffs, double angle) {
        part.setOffset(xOffs - BODY_IMAGE_SIZE / 2, yOffs - BODY_IMAGE_SIZE / 2);
        part.setAngle(angle);
        parts.add(part);
    }

    private void buildColors() {
        this.skinColor  = ColorUtils.hslColor(
                baseHue,
                baseSat,
                baseLum);
        this.armorColor = ColorUtils.hslColor(
                baseHue + armorHueAdjust,
                baseSat + armorSatAdjust,
                baseLum + armorLumAdjust);
        this.hairColor  = ColorUtils.hslColor(
                baseHue + hairHueAdjust,
                baseSat + hairSatAdjust,
                baseLum + hairLumAdjust);
        this.spikesColor = ColorUtils.hslColor(
                baseHue,
                baseSat * 0.1,
                spikeLum);
    }


    public void onDispose() {

    }
}

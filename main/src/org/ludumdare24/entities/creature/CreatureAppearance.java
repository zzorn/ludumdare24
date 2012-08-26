package org.ludumdare24.entities.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import org.gameflow.utils.ColorUtils;
import org.gameflow.utils.MathTools;
import org.ludumdare24.Mutator;

import java.util.Random;

/**
 * Messy troll drawing code here.
 */
public class CreatureAppearance {
    private static final int BODY_IMAGE_SIZE = 64;
    private static final double MIX_MUTATION_AMOUNT = 0.1;

    private final Creature creature;

    private float scale = 0.5f;

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

    private double armWaveSpeed = 1.5;
    private double armWaveSize = 0.5;

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


    public CreatureAppearance(Creature creature,
                              Color baseColor,
                              Mutator mutator) {
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

        buildColors();
    }

    public CreatureAppearance(Creature creature,
                              CreatureAppearance mother,
                              Mutator mutator) {
        this.creature = creature;

        initCreatureProperties(creature);

        baseHue = mutator.mutate(mother.baseHue, true);
        baseSat = mutator.mutate(mother.baseSat);
        baseLum = mutator.mutate(mother.baseLum);
        armorHueAdjust = mutator.mutate(mother.armorHueAdjust, true);
        armorSatAdjust = mutator.mutate(mother.armorSatAdjust, false, true);
        armorLumAdjust = mutator.mutate(mother.armorLumAdjust, false, true);
        hairHueAdjust = mutator.mutate(mother.hairHueAdjust, true);
        hairSatAdjust = mutator.mutate(mother.hairSatAdjust, false, true);
        hairLumAdjust = mutator.mutate(mother.hairLumAdjust, false, true);
        spikeLum = mutator.mutate(mother.spikeLum);

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
        baseLum = mutator.mix(mother.baseLum, father.baseLum);
        armorHueAdjust = mutator.mix(mother.armorHueAdjust, father.armorHueAdjust, true);
        armorSatAdjust = mutator.mix(mother.armorSatAdjust, father.armorSatAdjust, false, true);
        armorLumAdjust = mutator.mix(mother.armorLumAdjust, father.armorLumAdjust, false, true);
        hairHueAdjust = mutator.mix(mother.hairHueAdjust, father.hairHueAdjust, true);
        hairSatAdjust = mutator.mix(mother.hairSatAdjust, father.hairSatAdjust, false, true);
        hairLumAdjust = mutator.mix(mother.hairLumAdjust, father.hairLumAdjust, false, true);
        spikeLum = mutator.mix(mother.spikeLum, father.spikeLum);

        buildColors();
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

        head = createBodyPart(BodyPartShape.HEAD, false, skinColor, basicShape);
        torso = createBodyPart(BodyPartShape.TORSO, false, skinColor, basicShape);
        abdomen = createBodyPart(BodyPartShape.ABDOMEN, false, skinColor, basicShape);
        leftLeg = createBodyPart(BodyPartShape.LEG, false, skinColor, basicShape);
        rightLeg = createBodyPart(BodyPartShape.LEG, true, skinColor, basicShape);
        leftArm  = createBodyPart(BodyPartShape.ARM, false, skinColor, basicShape);
        rightArm = createBodyPart(BodyPartShape.ARM, true, skinColor, basicShape);
        leftEye = createBodyPart(BodyPartShape.EYE, false, Color.WHITE, basicShape);
        rightEye = createBodyPart(BodyPartShape.EYE, true, Color.WHITE, basicShape);

        float headCenterY = torso.getVisibleH() / 2 + head.getVisibleH() / 2;
        addPart(head, 0, headCenterY);

        double armHeight   = torso.getVisibleH() / 2 - leftArm.getVisibleH() / 2;
        double armDistance = torso.getVisibleW() / 2 + leftArm.getVisibleW() / 2;
        addPart(leftArm, -armDistance, armHeight);
        addPart(rightArm, armDistance, armHeight);
        leftArm.setRelativeOrigin(0.65f, 0.9f);
        rightArm.setRelativeOrigin(0.65f, 0.9f);


        double legHeight   = -torso.getVisibleH() / 2 -abdomen.getVisibleH() / 2 -leftLeg.getVisibleH() / 2;
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
    }

    public void onUpdate(float timeDelta) {
        // Flap your arms if you are a crazy creature
        totalTime += timeDelta;
        double armPos = (Math.sin(totalTime * armWaveSpeed * MathTools.Tau) * 0.5 + 0.5) * armWaveSize;
        double armAngle = MathTools.mix(armPos, (0.25 + 0.75) * MathTools.Tau, (0.25 + 0.35) * MathTools.Tau);
        leftArm.setAngle(armAngle);
        rightArm.setAngle(MathTools.Tau - armAngle);

        // Update glow effect
        ownerGodGlowEffect.update(timeDelta);
    }

    public void render(float x, float y, TextureAtlas atlas, SpriteBatch spriteBatch) {

        // Render worship glow
        ownerGodGlowEffect.setPosition(x, y);
        ownerGodGlowEffect.draw(spriteBatch );

        // Render creature bodyparts
        for (CreaturePart part : parts) {
            part.draw(atlas, spriteBatch, x, y, 0);
        }
    }


    private CreaturePart createBodyPart(BodyPartShape shape, boolean mirror, Color skinColor, double basicShape) {
        double h = MathTools.mix(length, 0.75, 1.5);
        double w = MathTools.mix(fatness, 0.75, 1.5);
        return new CreaturePart(shape, mirror, w, h, hair, armor, spikes, skinColor, hairColor, armorColor, spikesColor, basicShape, scale);
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

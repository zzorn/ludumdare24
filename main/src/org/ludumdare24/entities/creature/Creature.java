package org.ludumdare24.entities.creature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.gameflow.utils.ColorUtils;
import org.gameflow.utils.MathUtils;

import java.util.Random;

/**
 *
 */
public class Creature extends Entity {

    private Array<CreaturePart> parts = new Array<CreaturePart>();
    public Vector2 pos = new Vector2();
    public Vector2 velocity = new Vector2();
    public float angle = 0;

    public static final int BODY_IMAGE_SIZE = 64;

    private double armWaveSpeed = 1.5;
    private double armWaveSize = 0.5;
    private double totalTime = 0;
    public CreaturePart leftArm;
    public CreaturePart rightArm;
    public double basicShape = 0.5;
    public double hair = 0.5;
    public double armor = 0;
    public double spikes = 0;
    public double fatness = 0.8;
    public double length = 1;
    public Color skinColor = new Color(0.3f, 0.4f, 0.7f, 1f);
    public Color hairColor = Color.YELLOW.cpy();
    public Color armorColor = Color.WHITE.cpy();
    public Color spikesColor = Color.WHITE.cpy();
    public float SCALE = 1f;
    public CreaturePart rightLeg;
    private CreaturePart leftLeg;
    private CreaturePart abdomen;
    private CreaturePart torso;
    private CreaturePart head;
    private CreaturePart leftEye;
    private CreaturePart rightEye;

    public void onCreate(TextureAtlas atlas) {
        float tau = MathUtils.TauFloat;



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

        addPart(abdomen, 0, -torso.getVisibleH()/2 - abdomen.getVisibleH()/2);
        addPart(torso);

        double eyeXOffs = head.getVisibleW() / 4;
        addPart(leftEye, -eyeXOffs, headCenterY);
        addPart(rightEye, eyeXOffs, headCenterY);
    }

    public void randomize(Random random){
        skinColor = ColorUtils.randomColor(random);
        hairColor = ColorUtils.randomColor(random);
        armorColor = ColorUtils.randomColor(random);
        spikesColor = ColorUtils.randomColor(random);
        hair = random.nextFloat();
        armor = random.nextFloat();
        spikes = random.nextFloat();
        basicShape = random.nextFloat();
        length = 1 + random.nextGaussian() * 0.3;
        fatness = 1 + random.nextGaussian() * 0.3;
        armWaveSize = random.nextFloat();
        armWaveSpeed = random.nextFloat() ;
    }


    public void setPos(float x, float y) {
        pos.set(x, y);
    }

    private CreaturePart createBodyPart(BodyPartShape shape, boolean mirror, Color skinColor, double basicShape) {
        return new CreaturePart(shape, mirror, fatness, length, hair, armor, spikes, skinColor, hairColor, armorColor, spikesColor, basicShape, SCALE);
    }


    public double getArmWaveSpeed() {
        return armWaveSpeed;
    }

    public void setArmWaveSpeed(double armWaveSpeed) {
        this.armWaveSpeed = armWaveSpeed;
    }

    public double getArmWaveSize() {
        return armWaveSize;
    }

    public void setArmWaveSize(double armWaveSize) {
        this.armWaveSize = armWaveSize;
    }

    public double getHair() {
        return hair;
    }

    public void setHair(double hair) {
        this.hair = hair;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getFatness() {
        return fatness;
    }

    public void setFatness(double fatness) {
        this.fatness = fatness;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Color getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(Color skinColor) {
        this.skinColor = skinColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Color getArmorColor() {
        return armorColor;
    }

    public void setArmorColor(Color armorColor) {
        this.armorColor = armorColor;
    }

    public Color getSpikesColor() {
        return spikesColor;
    }

    public void setSpikesColor(Color spikesColor) {
        this.spikesColor = spikesColor;
    }

    public double getSpikes() {
        return spikes;
    }

    public void setSpikes(double spikes) {
        this.spikes = spikes;
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

    public void update(float timeDelta) {
        // Flap your arms if you are a crazy creature
        totalTime += timeDelta;
        double armPos = (Math.sin(totalTime * armWaveSpeed * MathUtils.Tau) * 0.5 + 0.5) * armWaveSize;
        double armAngle = MathUtils.mix(armPos, (0.25+0.75) * MathUtils.Tau, (0.25+0.35) * MathUtils.Tau);
        leftArm.setAngle(armAngle);
        rightArm.setAngle(MathUtils.Tau - armAngle);

        // Random walk
        velocity.x += timeDelta * (Math.random() - 0.5) * 100;
        velocity.y += timeDelta * (Math.random() - 0.5) * 100;
        velocity.x = MathUtils.clamp(velocity.x, -100, 100);
        velocity.y = MathUtils.clamp(velocity.y, -100, 100);
        pos.x += velocity.x * timeDelta;
        pos.y += velocity.y * timeDelta;
    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        for (CreaturePart part : parts) {
            part.draw(atlas, spriteBatch, pos, angle);
        }
    }

    public void onDispose() {
    }
}

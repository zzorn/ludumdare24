package org.ludumdare24.entities.creature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.gameflow.utils.ColorUtils;
import org.gameflow.utils.MathTools;
import org.ludumdare24.entities.WorldEntity;
import org.ludumdare24.world.GameWorld;

import java.util.Random;

/**
 *
 */
public class Creature extends WorldEntity {

    private static final double MAX_ARMOR_PROTECTION = 0.5;
    private Array<CreaturePart> parts = new Array<CreaturePart>();
    private float angle = 0;

    public static final int BODY_IMAGE_SIZE = 64;

    private double armWaveSpeed = 1.5;
    private double armWaveSize = 0.5;
    private double totalTime = 0;
    private CreaturePart leftArm;
    private CreaturePart rightArm;

    private double basicShape = 0.5;
    private double hair = 0.5;
    private double armor = 0;
    private double spikes = 0;
    private double fatness = 0.8;
    private double length = 1;


    private double maxHealth = 100;
    private double health = 100;



    private Color skinColor = new Color(0.3f, 0.4f, 0.7f, 1f);
    private Color hairColor = Color.YELLOW.cpy();
    private Color armorColor = Color.WHITE.cpy();
    private Color spikesColor = Color.WHITE.cpy();
    private float SCALE = 0.5f;
    private CreaturePart rightLeg;
    private CreaturePart leftLeg;
    private CreaturePart abdomen;
    private CreaturePart torso;
    private CreaturePart head;
    private CreaturePart leftEye;
    private CreaturePart rightEye;
    private final GameWorld gameWorld;

    public Creature(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void onCreate(TextureAtlas atlas) {
        float tau = MathTools.TauFloat;



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


    private CreaturePart createBodyPart(BodyPartShape shape, boolean mirror, Color skinColor, double basicShape) {
        return new CreaturePart(shape, mirror, fatness, length, hair, armor, spikes, skinColor, hairColor, armorColor, spikesColor, basicShape, SCALE);
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

    public void onUpdate(float timeDelta) {
        // Flap your arms if you are a crazy creature
        totalTime += timeDelta;
        double armPos = (Math.sin(totalTime * armWaveSpeed * MathTools.Tau) * 0.5 + 0.5) * armWaveSize;
        double armAngle = MathTools.mix(armPos, (0.25 + 0.75) * MathTools.Tau, (0.25 + 0.35) * MathTools.Tau);
        leftArm.setAngle(armAngle);
        rightArm.setAngle(MathTools.Tau - armAngle);

        // Random walk
        Vector2 velocity = getVelocity();
        velocity.x += timeDelta * (Math.random() - 0.5) * 100;
        velocity.y += timeDelta * (Math.random() - 0.5) * 100;
        velocity.x = MathTools.clamp(velocity.x, -100, 100);
        velocity.y = MathTools.clamp(velocity.y, -100, 100);
    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        for (CreaturePart part : parts) {
            part.draw(atlas, spriteBatch, getWorldPos(), angle);
        }
    }

    public void onDispose() {
    }

    /**
     * Damages a creature
     * @param amount
     */
    public void damage(float amount) {
        // Armor protects
        double damageThrough = MathTools.mix(armor, amount, amount * MAX_ARMOR_PROTECTION);

        trueDamage(damageThrough);
    }

    /**
     * Gives damage without using armor to defend.
     */
    private void trueDamage(double damageThrough) {

        // Change health
        health -= damageThrough;

        // Check if dies
        if (health <= 0) {
            // Dead, remove from game
            gameWorld.removeEntity(this);

            // Spawn some meat there, depending on body size
            // TODO
        }
    }
}

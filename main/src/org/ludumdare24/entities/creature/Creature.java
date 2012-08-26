package org.ludumdare24.entities.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.gameflow.utils.ColorUtils;
import org.gameflow.utils.MathTools;
import org.ludumdare24.Mutator;
import org.ludumdare24.entities.God;
import org.ludumdare24.entities.WorldEntity;
import org.ludumdare24.world.GameWorld;

import java.util.Random;

/**
 *
 */
public class Creature extends WorldEntity {

    private static final double MAX_ARMOR_PROTECTION = 0.5;

    private final GameWorld gameWorld;
    private final God god;

    private CreatureAppearance appearance;

    private double basicShape = 0.5;
    private double hair = 0.5;
    private double armor = 0;
    private double spikes = 0;
    private double fatness = 0.8;
    private double length = 1;

    private double maxHealth = 100;
    private double health = 100;

    public Creature(GameWorld gameWorld, God god, Mutator mutator) {
        this.gameWorld = gameWorld;
        this.god = god;

        basicShape = mutator.randomize();
        hair       = mutator.randomize();
        armor      = mutator.randomize();
        spikes     = mutator.randomize();
        fatness    = mutator.randomize();
        length     = mutator.randomize();

        Color color = god != null ? god.getColor() : mutator.randomizeColor();
        appearance = new CreatureAppearance(this, color, mutator);
    }

    public Creature(GameWorld gameWorld, Mutator mutator, Creature parent) {
        this.gameWorld = gameWorld;
        this.god = parent.getGod();

        this.setWorldPos(parent.getWorldPos().x, parent.getWorldPos().y);

        basicShape = mutator.mutate(parent.basicShape, true);
        hair       = mutator.mutate(parent.hair);
        armor      = mutator.mutate(parent.armor);
        spikes     = mutator.mutate(parent.spikes);
        fatness    = mutator.mutate(parent.fatness);
        length     = mutator.mutate(parent.length);

        appearance = new CreatureAppearance(this, parent.getAppearance(), mutator);
    }

    public Creature(GameWorld gameWorld, Mutator mutator, Creature mother, Creature father) {
        this.gameWorld = gameWorld;
        this.god = mother.getGod();

        this.setWorldPos(mother.getWorldPos().x, mother.getWorldPos().y);

        basicShape = mutator.mix(mother.basicShape, father.basicShape, true);
        hair       = mutator.mix(mother.hair, father.hair);
        armor      = mutator.mix(mother.armor, father.armor);
        spikes     = mutator.mix(mother.spikes, father.spikes);
        fatness    = mutator.mix(mother.fatness, father.fatness);
        length     = mutator.mix(mother.length, father.length);

        appearance = new CreatureAppearance(this, mother.getAppearance(), father.getAppearance(), mutator);
    }

    public CreatureAppearance getAppearance() {
        return appearance;
    }

    public God getGod() {
        return god;
    }

    public double getBasicShape() {
        return basicShape;
    }

    public double getHair() {
        return hair;
    }

    public double getArmor() {
        return armor;
    }

    public double getSpikes() {
        return spikes;
    }

    public double getFatness() {
        return fatness;
    }

    public double getLength() {
        return length;
    }

    public void onCreate(TextureAtlas atlas) {

        appearance.create(atlas);
    }

    public void onUpdate(float timeDelta) {
        // Update appearance
        appearance.onUpdate(timeDelta);

        // TODO: Smartness here

        // Random walk
        Vector2 velocity = getVelocity();
        velocity.x += timeDelta * (Math.random() - 0.5) * 100;
        velocity.y += timeDelta * (Math.random() - 0.5) * 100;
        velocity.x = MathTools.clamp(velocity.x, -100, 100);
        velocity.y = MathTools.clamp(velocity.y, -100, 100);
    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        appearance.render(getWorldPos().x, getWorldPos().y, atlas, spriteBatch);
    }

    public void onDispose() {
        appearance.onDispose();
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

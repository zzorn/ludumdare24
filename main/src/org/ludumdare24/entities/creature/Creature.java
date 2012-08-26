package org.ludumdare24.entities.creature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.utils.MathTools;
import org.ludumdare24.Mutator;
import org.ludumdare24.entities.FoodEntity;
import org.ludumdare24.entities.God;
import org.ludumdare24.entities.WorldEntity;
import org.ludumdare24.world.FoodType;
import org.ludumdare24.world.GameWorld;

/**
 *
 */
public class Creature extends WorldEntity {

    private static final double MAX_ARMOR_PROTECTION = 0.5;
    private static final double BEHAVIOUR_CHECK_INTERVAL_SECONDS = 1.0;

    private static final double DAMAGE_FROM_NO_ENERGY_PER_SECOND = 10.0;

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
    private double healPerSecond = 1;

    private double maxEnergy = 100+100*Math.random();
    private double energy = 100;
    private double basicEnergyUsagePerSecond = 1.5;
    private double pregnantEnergyUsagePerSecond = 1;
    private double woundedEnergyUsagePerSecond = 2;
    private double walkEnergyUsagePerSecond = 1;

    private double eatingSpeedEnergyPerSecond = 100;
    private double maxEatingDistance = 80;

    private double babyDevelopmentTime = 20;
    private double timeUntilBirthing = 0;
    private Creature baby = null;

    private Array<Behaviour> behaviours = new Array<Behaviour>();
    private Behaviour currentBehaviour = null;
    private double timeUntilBehaviourChange = BEHAVIOUR_CHECK_INTERVAL_SECONDS;
    private double timeUntilBehaviourReactivation = 0;
    private double timeSinceBehaviourChecked = 0;

    private Vector2 movementDirection = new Vector2();
    private double movementSpeedFactor = 0.5;
    private double maxMovementSpeedPerSecond = 1000;

    private double energyReleasedOnDeath = maxEnergy * 2;

    private FoodEntity targetFoodEntity = null;

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

        setupBehaviors();
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

        setupBehaviors();
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

        setupBehaviors();
    }

    private void setupBehaviors() {
        // Walk around
        behaviours.add(new Behaviour("Walk around", this, 4) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return 0.1;
            }

            @Override
            public void onActivated() {
                // Random jog
                moveInRandomDirection(0.7);
            }
        });

        // Flock to others
        behaviours.add(new Behaviour("Flock to other", this, 4) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return Math.random() * 0.5;
            }

            @Override
            public void onActivated() {
                Creature closestCreature = gameWorld.getClosestCreature(getX(), getY());
                if (closestCreature  != null) {
                    moveTowards(closestCreature.getWorldPos(), 0.2);
                }
                else {
                    // All alone in the world.  Stand and think about that.
                    moveInRandomDirection(0);
                }
            }
        });

        // Search food
        behaviours.add(new Behaviour("Search for food", this, 0.5) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return creature.getHunger() +  creature.getWounds();
            }

            @Override
            public void onActivated() {
                targetFoodEntity = gameWorld.getClosestFood(getX(), getY());
                if (targetFoodEntity != null) {
                    moveTowards(targetFoodEntity.getWorldPos(), 1);
                }
                else {
                    // Search for food
                    moveInRandomDirection(0.7);
                }
            }
        });

        // Lick wounds
        behaviours.add(new Behaviour("Lick wounds", this, 1) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return creature.getWounds() / 2;
            }

            @Override
            public void onActivated() {
                // Random crawl
                moveInRandomDirection(0.2);
            }
        });

    }

    private void moveTowards(Vector2 pos, double speed) {
        movementDirection.set(pos.x - getX(), pos.y - getY());
        movementSpeedFactor = speed;
    }

    private void moveInRandomDirection(double speed) {
        movementDirection.set(
                (float) Math.random() * 2 - 1,
                (float) Math.random() * 2 - 1);
        movementSpeedFactor = speed;
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
        // Simulate creature
        simulate(timeDelta);

        // Do behaviour
        behave(timeDelta);

        // Update appearance
        appearance.onUpdate(timeDelta);
    }

    private void behave(float timeDelta) {
        timeSinceBehaviourChecked += timeDelta;

        // Check if we should reselect behaviour
        timeUntilBehaviourChange -= timeDelta;
        timeUntilBehaviourReactivation -= timeDelta;
        if (timeUntilBehaviourChange <= 0 || timeUntilBehaviourReactivation <= 0 || currentBehaviour == null) {

            // Vote in a new behavior
            double highestImportance = Double.NEGATIVE_INFINITY;
            Behaviour bestBehaviour = null;
            for (Behaviour behaviour : behaviours) {
                double importance = behaviour.getImportance(timeSinceBehaviourChecked);
                if (importance > highestImportance) {
                    highestImportance = importance;
                    bestBehaviour = behaviour;
                }
            }

            // Change behaviour if needed
            if (bestBehaviour != currentBehaviour || timeUntilBehaviourReactivation <= 0) {
                targetFoodEntity = null;
                if (currentBehaviour != null) currentBehaviour.onDeactivated();

                currentBehaviour = bestBehaviour;

                if (currentBehaviour != null) {

                    currentBehaviour.onActivated();
                    timeUntilBehaviourReactivation = currentBehaviour.getReActivationTime();
                }
                else {
                    timeUntilBehaviourReactivation = Double.POSITIVE_INFINITY;
                }
            }

            // Add some random factor to next check time to avoid everyone checking at once.
            timeUntilBehaviourChange = BEHAVIOUR_CHECK_INTERVAL_SECONDS + BEHAVIOUR_CHECK_INTERVAL_SECONDS * Math.random();
            timeSinceBehaviourChecked = 0;
        }

        // Update behaviour
        if (currentBehaviour != null) {
            currentBehaviour.update(timeDelta);
        }
    }

    private void simulate(float timeDelta) {
        double energyUsagePerSecond = basicEnergyUsagePerSecond;

        // Eating
        if (energy < maxEnergy && targetFoodEntity != null && distanceTo(targetFoodEntity) <= maxEatingDistance) {
            energy += targetFoodEntity.eat(eatingSpeedEnergyPerSecond * timeDelta);
            if (energy > maxEnergy) {
                energy = maxEnergy;
            }
        }

        // Healing
        if (isWounded()) {
            energyUsagePerSecond += woundedEnergyUsagePerSecond;

            // Heal if energy
            if (energy > maxEnergy*0.2) {
                health += healPerSecond * timeDelta;
                if (health > maxHealth) health = maxHealth;
            }
        }

        // Pregnancy
        if (isPregnant()) {
            energyUsagePerSecond += pregnantEnergyUsagePerSecond;

            // Develop baby if energy
            if (energy > 0) {
                timeUntilBirthing -= timeDelta;
                if (timeUntilBirthing <= 0) {
                    // Birth!
                    Vector2 worldPos = getWorldPos();
                    baby.setWorldPos(
                            worldPos.x + 10 * (float) (Math.random() * 2 - 1),
                            worldPos.y + 10 * (float) (Math.random() * 2 - 1));
                    gameWorld.addEntity(baby);
                    baby = null;
                    timeUntilBirthing = 0;
                }
            }
        }

        // Movement
        movementSpeedFactor = MathTools.clampToZeroToOne(movementSpeedFactor);
        if (energy > 0) {
            energyUsagePerSecond += movementSpeedFactor * walkEnergyUsagePerSecond;
            getVelocity().set(movementDirection);
            getVelocity().nor().mul((float) (movementSpeedFactor * maxMovementSpeedPerSecond * timeDelta));
        }
        else {
            // Low energy move slowly
            getVelocity().set(movementDirection);
            getVelocity().nor().mul((float) (movementSpeedFactor * maxMovementSpeedPerSecond * timeDelta/4));
        }

        // Use energy
        energy -= energyUsagePerSecond * timeDelta;

        // Damage if out of energy
        if (energy <= 0) {
            energy = 0;
            trueDamage(DAMAGE_FROM_NO_ENERGY_PER_SECOND * timeDelta);
        }
    }

    /**
     * @return 0 if no health, 1 if full health.
     */
    public double getHealthStatus() {
        return MathTools.clampToZeroToOne(health / maxHealth);
    }

    /**
     * @return 0 if no energy, 1 if max energy
     */
    public double getEnergyStatus() {
        return MathTools.clampToZeroToOne(energy / maxEnergy);
    }

    /**
     * @return 0 if full energy, 1 if no energy
     */
    public double getHunger() {
        return 1.0 - getEnergyStatus();
    }

    /**
     * @return 0 if full energy, 1 if no energy
     */
    public double getWounds() {
        return 1.0 - getHealthStatus();
    }

    /**
     * @return 0 if not pregnant, 0..1 when pregnant, 1 when delivery nearly due.
     */
    public double getPregnancyStatus() {
        if (timeUntilBirthing <= 0) return 0;
        else return MathTools.clampToZeroToOne(1.0 - timeUntilBirthing / babyDevelopmentTime);
    }

    public boolean isWounded() {
        return health < maxHealth;
    }

    public boolean isPregnant() {
        return baby != null;
    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        // Render the creatures appearance
        appearance.render(getWorldPos().x, getWorldPos().y, atlas, spriteBatch);

        // Render behaviours if they have anything to render
        if (currentBehaviour != null) {
            currentBehaviour.render(atlas, spriteBatch);
        }
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
            gameWorld.spawnFood(FoodType.MEAT, getX(), getY(), energyReleasedOnDeath);
        }
    }
}

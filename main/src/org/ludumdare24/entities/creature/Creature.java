package org.ludumdare24.entities.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.ludumdare24.MainGame;
import org.ludumdare24.Mutator;
import org.ludumdare24.Sounds;
import org.ludumdare24.entities.AppleTree;
import org.ludumdare24.entities.FoodEntity;
import org.ludumdare24.entities.God;
import org.ludumdare24.entities.WorldEntity;
import org.ludumdare24.world.FoodType;
import org.ludumdare24.world.GameWorld;
import org.ludumdare24.world.Level;

import static org.gameflow.utils.MathTools.*;

/**
 *
 */
public class Creature extends WorldEntity {

    private static final double BASIC_HEALING_PER_SECOND = 3.0;
    private static final double BASIC_BABY_DEVELOPMENT_TIME = 16.0;
    private static final double BASIC_MOVEMENT_SPEED_PER_SECOND = 800.0;
    private static final double BASIC_LIFE_LENGTH_SECONDS = 150;

    private static final double ENERGY_CONSUMPTION_PER_KG_PER_SECOND = 0.02;
    private static final double ENERGY_NEEDED_TO_PRODUCE_ONE_KG = 1;

    private static final double MAX_ARMOR_PROTECTION = 0.5;
    private static final double MATING_BOOST_DURATION_SECONDS = 20;
    private static final double RAGE_TARGET_BOOST_DURATION_SECONDS = 30;
    private static final double BEHAVIOUR_CHECK_INTERVAL_SECONDS = 1.0;

    private static final double ATTACK_RANGE = 55.0;

    private static final double DAMAGE_FROM_NO_ENERGY_PER_SECOND = 10.0;
    private static final double DAMAGE_FROM_OLD_AGE_PER_SECOND = 10.0;
    private static final double NO_ENERGY_MOVEMENT_SLOWDOWN = 0.25;
    private static final double MATURITY_AGE = 0.2;
    private static final double MATING_DISTANCE = 37;
    private static final double RAGE_ATTACK_RADIUS = 500;

    private final MainGame game;
    private final GameWorld gameWorld;
    private final God god;

    // Name
    private String firstName;
    private String familyName;

    // Appearance
    private CreatureAppearance appearance;
    private double basicShape = 0.5;
    private double hat = 0.5;

    // Physical attributes
    private double heart = Math.random();        // Increases mass, max health, affects torso size
    private double stomach = Math.random();      // Increases mass, max energy, affects stomach size
    private double armor = Math.random();        // Increases mass, protection
    private double spikes = Math.random();       // Increases mass, attack
    private double eyes = Math.random();         // Increases energy usage, vision range
    private double fastMoving = Math.random();     // Increases energy usage, movement speed, affects leg length
    private double hair = Math.random();         // Changes optimal temperature zone
    private double fastBaby = Math.random(); // Changes between short development with high energy usage per second and longer with lower energy usage
    private double fastHealing = Math.random(); // Changes between short heal with high energy usage per second and longer with lower energy usage

    // Derived physical attributes
    private double mass = 100;  // Affects energy consumption up and development time up and movement speed down
    private double basicEnergyUsagePerSecond = 2;
    private double maxHealth = 100;
    private double healPerSecond = 5;
    private double maxEnergy = 100+50*Math.random();
    private double pregnantEnergyUsagePerSecond = 2;
    private double woundedEnergyUsagePerSecond = 2;
    private double movementEnergyUsagePerSecond = 2;
    private double attackEnergyUsagePerSecond = 2;
    private double attackDamagePerSecond = 20;
    private double eatingSpeedEnergyPerSecond = 50;
    private double maxEatingDistance = 50;
    private double sightRange = 300;
    private double maxAgeSeconds = 60;
    private double babyDevelopmentTime = 20;
    private double maxMovementSpeedPerSecond = 1650;
    private double energyReleasedOnDeath = maxEnergy * 0.45;

    // Mental attributes
    private double fearfulReckless = Math.random();
    private double peacefulAngry = Math.random();
    private double frigidNymphomanic = Math.random();
    private double spartanGlutton = Math.random();
    private double homesitterExplorer = Math.random();
    private double loneWolfGroupAnimal = Math.random();
    private double walkerRunner = Math.random();
    private double diciplinedRandom = Math.random();
    private double claustrofobicTreeHugger = Math.random();
    private double godFearingIrreligious = Math.random();


    // Derived mental attributes
    private double minDistanceToOthers = 100;
    private double maxDistanceToGodTarget = 300;


    // Runtime attributes
    private double health = 100;
    private double energy = maxEnergy / 2;
    private double ageSeconds = 0;
    private double matingBoostTimeLeft = 0;
    private double rageTargetBoostTimeLeft = 0;
    private double babyDevelopmentTimeLeft = 0;
    private Creature baby = null;
    private boolean dead = false;
    private boolean attacking = false;
    private boolean doActionRePrioritation = false;
    private String currentAction = "Doing nothing";

    // Behaviour
    private Array<Behaviour> behaviours = new Array<Behaviour>();
    private Behaviour currentBehaviour = null;
    private double timeUntilBehaviourChange = BEHAVIOUR_CHECK_INTERVAL_SECONDS;
    private double timeUntilBehaviourReactivation = 0;
    private double timeSinceBehaviourChecked = 0;

    // Movement
    private Vector2 movementDirection = new Vector2();
    private double movementSpeedFactor = 0.5;


    // Perception
    private FoodEntity closestFoodEntity = null;
    private Creature closestCreature = null;
    private Creature matingTarget = null;
    private Creature attackTarget = null;
    private AppleTree closestAppleTree = null;

    // Particle effect

    private ParticleEffect creatureParticle=null;
    private boolean observed;



    public Creature(MainGame game, GameWorld gameWorld, God god, Mutator mutator, double hat, Level levelData) {
        this.gameWorld = gameWorld;
        this.god = god;
        this.game = game;

        // Create basic attributes based on level or player
        if (levelData == null) {
            // Players start creature
            armor      = mutator.randomize(0.2, 0.5);
            spikes     = mutator.randomize(0.2, 0.5);
            fastMoving = mutator.randomize(0.4, 0.6);
            heart      = mutator.randomize(0.3, 0.6);
            stomach    = mutator.randomize(0.3, 0.6);
        }
        else {
            // Enemies on level
            armor      = levelData.getEnemyArmor();
            spikes     = levelData.getEnemyAttack();
            fastMoving = levelData.getEnemySpeed();
            stomach    = levelData.getEnemySize();
            heart      = levelData.getEnemyThoughness();
        }

        // Mutate basic attributes
        armor      = mutator.mutate(armor);
        spikes     = mutator.mutate(spikes);
        fastMoving = mutator.mutate(fastMoving);
        heart      = mutator.mutate(heart);
        stomach    = mutator.mutate(stomach);

        // Some appearance attributes
        basicShape = mutator.randomize();
        hair       = mutator.randomize();

        // Derive
        calculateDerivedAttributes();

        // Appearance
        this.hat = hat;
        Color color = god != null ? god.getColor() : mutator.randomizeColor();
        appearance = new CreatureAppearance(this, color, mutator, this.hat);

        setupBehaviors();

        createName(null);
    }

    public Creature(MainGame game, GameWorld gameWorld, Mutator mutator, Creature parent) {
        this.gameWorld = gameWorld;
        this.game = game;
        this.god = parent.getGod();

        this.setWorldPos(parent.getWorldPos().x, parent.getWorldPos().y);

        // Mutate basic attributes
        calculateBasicAttributes(mutator, parent, parent);

        // Derive
        calculateDerivedAttributes();

        // Appearance
        hat = parent.hat;
        appearance = new CreatureAppearance(this, parent.getAppearance(), mutator);

        setupBehaviors();

        createName(parent);
    }

    public Creature(MainGame game, GameWorld gameWorld, Mutator mutator, Creature mother, Creature father) {
        this.gameWorld = gameWorld;
        this.game = game;
        this.god = mother.getGod();

        this.setWorldPos(mother.getWorldPos().x, mother.getWorldPos().y);

        // Mutate basic attributes
        calculateBasicAttributes(mutator, mother, father);

        // Derive
        calculateDerivedAttributes();

        // Appearance
        hat = mother.hat;
        appearance = new CreatureAppearance(this, mother.getAppearance(), father.getAppearance(), mutator);

        setupBehaviors();

        createName(mother);
    }

    private void calculateBasicAttributes(Mutator mutator, Creature mother, Creature father) {
        // Physical attributes
        basicShape  = mutator.mix(mother.basicShape, father.basicShape, true);
        heart       = mutator.mix(mother.heart, father.heart);
        stomach     = mutator.mix(mother.stomach, father.stomach);
        hair        = mutator.mix(mother.hair, father.hair);
        armor       = mutator.mix(mother.armor, father.armor);
        spikes      = mutator.mix(mother.spikes, father.spikes);
        eyes        = mutator.mix(mother.eyes, father.eyes);
        fastMoving  = mutator.mix(mother.fastMoving, father.fastMoving);
        fastBaby    = mutator.mix(mother.fastBaby, father.fastBaby);
        fastHealing = mutator.mix(mother.fastHealing, father.fastHealing);

        // Mental attributes
        fearfulReckless         = mutator.mix(mother.fearfulReckless, father.fearfulReckless);
        peacefulAngry           = mutator.mix(mother.peacefulAngry, father.peacefulAngry);
        frigidNymphomanic       = mutator.mix(mother.frigidNymphomanic, father.frigidNymphomanic);
        spartanGlutton          = mutator.mix(mother.spartanGlutton, father.spartanGlutton);
        homesitterExplorer      = mutator.mix(mother.homesitterExplorer, father.homesitterExplorer);
        loneWolfGroupAnimal     = mutator.mix(mother.loneWolfGroupAnimal, father.loneWolfGroupAnimal);
        walkerRunner            = mutator.mix(mother.walkerRunner, father.walkerRunner);
        diciplinedRandom        = mutator.mix(mother.diciplinedRandom, father.diciplinedRandom);
        claustrofobicTreeHugger = mutator.mix(mother.claustrofobicTreeHugger, father.claustrofobicTreeHugger);
        godFearingIrreligious   = mutator.mix(mother.godFearingIrreligious, father.godFearingIrreligious);

    }

    private void calculateDerivedAttributes() {
        // Derived physical attributes
        mass = 20;
        mass += mix(heart, 10, 40);
        mass += mix(stomach, 20, 50);
        mass += mix(armor, 0, 100);
        mass += mix(spikes, 0, 20);

        basicEnergyUsagePerSecond = 0.3;
        basicEnergyUsagePerSecond += mix(eyes, 0, 0.5);
        basicEnergyUsagePerSecond += mix(fastMoving, 0, 0.5);
        basicEnergyUsagePerSecond += ENERGY_CONSUMPTION_PER_KG_PER_SECOND * mass;

        maxHealth = mix(heart, 50, 150);
        maxEnergy = mix(stomach, 50, 150);

        healPerSecond                = BASIC_HEALING_PER_SECOND * mix(fastHealing, 0.5, 5);
        woundedEnergyUsagePerSecond  = mix(fastHealing, 0.25, 10);

        pregnantEnergyUsagePerSecond = mix(fastBaby,0.5, 5 );
        babyDevelopmentTime = BASIC_BABY_DEVELOPMENT_TIME *  mix(fastBaby, 5, 0.25);

        movementEnergyUsagePerSecond = mix(fastMoving, 0.5, 5);
        maxMovementSpeedPerSecond = BASIC_MOVEMENT_SPEED_PER_SECOND * mix(fastMoving, 0.25, 10) * (100.0 / mass);

        eatingSpeedEnergyPerSecond = mix(stomach, 20, 80); // TODO: Could make similar eating speed vs absorbed energy tradeoff as above
        maxEatingDistance          = mix(fastMoving, 40, 60);

        attackEnergyUsagePerSecond = mix(spikes, 0.2, 3);
        attackDamagePerSecond      = mix(spikes, 30, 100);

        sightRange = mix(eyes, 100, RAGE_ATTACK_RADIUS);

        maxAgeSeconds = BASIC_LIFE_LENGTH_SECONDS * (100.0 / mass); // Fat trolls live shorter :P

        energyReleasedOnDeath = maxEnergy * 0.5;

        // Derived mental attributes
        minDistanceToOthers = mix(loneWolfGroupAnimal, 400, 60);
        maxDistanceToGodTarget = mix(godFearingIrreligious, 100, 1000);
    }


    /**
     * Determine the first name and family name for this troll.
     * @param mother the mother of the troll, or null if this troll has no mother.
     */
    private void createName(Creature mother) {
        // Create first name
        firstName = createFirstName();

        if (mother == null) {
            // No mother, create new family name
            familyName = createFamilyName();
        }
        else {
            if (Math.random() < 0.5) {
                // Use mothers family name
                familyName = mother.getFamilyName();
            }
            else if (Math.random() < 0.5) {
                // Use mothers first name as family name
                familyName = mother.getFirstName() + randomString("son", "dottir","");
            }
            else {
                // Create new family name
                familyName = createFamilyName();
            }
        }
    }

    /**
     * @return a first name for this troll.
     */
    private String createFirstName() {
        return randomString("Tryg", "Un", "Bur", "Hur", "Lur", "Mur", "Nir", "Gar", "Bal", "Bar", "Tur", "Mun") +
               randomString("am", "um", "ul", "uk", "ul", "du", "di", "ni", "", "", "", "", "", "", "", "")+
               randomString("dir", "dir", "dik", "dil", "bar", "mur", "ko", "ro", "do", "go", "", "", "", "", "");
    }

    /**
     * @return a new family name for this troll.
     */
    private String createFamilyName() {
        return randomString("Stone", "Apple", "Bone", "Troll", "Cave", "Tree", "Boulder", "Hill", "Mound", "Grave", "Valley", "Root", "Un", "Buk", "Ruk") +
               randomString("hollow", "root", "nest", "home", "shade", "side", "rest", "brock", "rak", "jack", "digger", "lifter", "roller", "eater", "gnawer", "muncher", "murr", "durr", "hurr", "dor", "dir", "", "", "", "", "");
    }

    /**
     * @return a random string from the strings passed in.
     */
    private String randomString(String ... strings) {
        int i = (int) ((strings.length - 1) * Math.random());
        return strings[i];
    }

    private void setupBehaviors() {
        // Walk around
        behaviours.add(new Behaviour(randomString("I'm walking around.", "Having a stroll over here."), this, 4) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return 0.1;
            }

            @Override
            public void onActivated(double activationImportance) {
                // Random jog
                moveInRandomDirection(0.7);
            }
        });

        behaviours.add(new Behaviour(randomString("I'm heading over to that apple tree", "Where there are apple trees, there are apples!", "Looking for a tree"), this, 4) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                if (closestAppleTree == null) return 0;
                else return claustrofobicTreeHugger * Math.random();
            }

            @Override
            public void onActivated(double activationImportance) {
                // Random jog
                if (closestAppleTree != null) moveTowards(closestAppleTree.getWorldPos(), walkerRunner);
            }
        });

        // Walk towards move target
        behaviours.add(new Behaviour(randomString("You called me, I move!", "Dabu?", "Zug Zug", "Stop ordering me around!"), this, 3) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                if (god != null) {
                    double distanceToGodMoveTarget = god.getMoveTarget().dst(getX(), getY());
                    double distancePull = map(distanceToGodMoveTarget, 0.5 * maxDistanceToGodTarget, 1.5 * maxDistanceToGodTarget, 0, 1);
                    double recencyPull = god.getMoveTargetPull() * 10 * (1.0-godFearingIrreligious);
                    return distancePull + recencyPull;
                }
                else return 0;
            }

            @Override
            public void onActivated(double activationImportance) {
                moveTowards(god.getMoveTarget(), 1);
            }
        });

        // Mate
        behaviours.add(new Behaviour(randomString("Looking for some love!", "Seen any hot troll mothers around?"), this, 5) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                if (!canMate() || closestCreature == null) return 0;
                else {
                    double attractiveness = 0;

                    // Similarity
                    attractiveness += closestCreature.getAppearance().similarity(getAppearance()) * 2;

                    // General willingness
                    attractiveness += frigidNymphomanic;

                    // Mating boost
                    attractiveness += getMatingBoost() * 4;

                    // Energy
                    attractiveness += getEnergyStatus();

                    // Other party doing well
                    attractiveness += closestCreature.getEnergyStatus();

                    return attractiveness / 5;
                }
            }

            @Override
            public void onActivated(double activationImportance) {
                if (closestCreature != null) {
                    matingTarget = closestCreature;
                    moveTowards(closestCreature.getWorldPos(), walkerRunner);
                }
                else {
                    standStill();
                }
            }

            @Override
            public void onDeactivated() {
                matingTarget = null;
            }
        });

        // Flock to others
        behaviours.add(new Behaviour(randomString("Wait for me!", "Wait for me!", "Where are you going guys?", "Hey mates, lets hang out", "Lets hang out"), this, 3) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                double importance = Math.random() * 0.2;

                // Prefer similar
                if (closestCreature != null) {
                    importance += closestCreature.getAppearance().similarity(getAppearance()) - 0.65;
                }

                return importance;
            }

            @Override
            public void onActivated(double activationImportance) {
                if (closestCreature  != null) {
                    moveTowards(closestCreature.getWorldPos(), walkerRunner);
                }
                else {
                    // All alone in the world.  Stand and think about that.
                    standStill();
                }
            }
        });

        // Lone panic
        behaviours.add(new Behaviour(randomString("Where is everyone?", "I'm feeling lonely", "Am I the last of my species?"), this, 4) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return closestCreature  == null ? 0.5 : 0;
            }

            @Override
            public void onActivated(double activationImportance) {
                // All alone in the world.  Stand and think about that.
                standStill();
            }
        });

        // Overcrowd
        behaviours.add(new Behaviour(randomString("Need some elbow room...", "Too crowded here.", "Need some air", "I don't like this crowd", "Heading off", "I'm off"), this, 2) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                if (closestCreature == null) return 0;
                else {
                    double importance = 0;

                    double distanceToClosest = distanceTo(closestCreature);
                    if (distanceToClosest > minDistanceToOthers) importance = 0;
                    else importance = (minDistanceToOthers - distanceToClosest) / minDistanceToOthers;

                    // Avoid different trolls
                    importance += 0.5 - closestCreature.getAppearance().similarity(getAppearance());

                    return importance;
                }
            }

            @Override
            public void onActivated(double activationImportance) {
                if (closestCreature  != null) {
                    //moveInRandomDirection(0.3 + activationImportance);
                    moveAwayFrom(closestCreature.getWorldPos(), activationImportance + walkerRunner);
                }
                else {
                    if (Math.random() < 0.4) {
                        // All alone in the world.  Stand and think about that.
                        standStill();
                    }
                    else {
                        moveInRandomDirection(0.1 + Math.random() * walkerRunner);
                    }
                }
            }
        });

        // Attack
        behaviours.add(new Behaviour(randomString("Take that!", "Raaaugh!", "Fear my spikes!", "This calls for violence!", "It has come to this!"), this, 2) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                if (closestCreature == null) return 0;
                else {
                    double rageTarget = closestCreature.getRageTargetBoost() * 20.0;
                    double wrongRegionRage = closestCreature.getGod() != god ? 0.4 : 0; // Other god! They must die!
                    double difference = 0.6 - closestCreature.getAppearance().similarity(getAppearance());
                    double basicAttitude = peacefulAngry * 0.3;
                    double ods = getHealthStatus() - closestCreature.getHealthStatus();
                    return rageTarget + (wrongRegionRage + basicAttitude + ods + difference) * getHealthStatus();
                }
            }

            @Override
            public void onActivated(double activationImportance) {
                if (closestCreature  != null) {
                    moveTowards(closestCreature.getWorldPos(), mix(walkerRunner, activationImportance / 2, 1));
                    attackTarget = closestCreature;
                }
                else {
                    standStill();
                }
            }

            @Override
            public void onDeactivated() {
                attackTarget = null;
            }
        });

        // Search food
        behaviours.add(new Behaviour(randomString("I'm hungry", "An apple would taste good", "When's lunch?", "I'm starving over here"), this, 0.5) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return creature.getHunger() +  creature.getWounds();
            }

            @Override
            public void onActivated(double actiavationImportance) {
                closestFoodEntity = gameWorld.getClosestFood(getX(), getY());
                if (closestFoodEntity != null) {
                    moveTowards(closestFoodEntity.getWorldPos(), 1);
                }
                else if (closestAppleTree != null) {
                    moveTowards(closestAppleTree.getWorldPos(), 0.2);
                }
                else {
                    // Search for food
                    moveInRandomDirection(0.5 * walkerRunner);
                }
            }
        });

        // Lick wounds
        behaviours.add(new Behaviour(randomString("Ow, this hurts", "I'm almost dead!", "Ouch!", "Ow ow ow"), this, 1) {
            @Override
            public double getImportance(double timeSinceLastAsked) {
                return creature.getWounds() / 2;
            }

            @Override
            public void onActivated(double actiavationImportance) {
                // Random crawl
                moveInRandomDirection(0.2 + Math.random() * walkerRunner);
            }
        });

    }

    private boolean canMate() {
        return !isPregnant() && getRelativeAge() >= MATURITY_AGE;
    }

    private void moveTowards(Vector2 pos, double speed) {
        movementDirection.set(pos.x - getX(), pos.y - getY());
        movementSpeedFactor = speed;
    }

    private void moveTowards(float x, float y, double speed) {
        movementDirection.set(x - getX(), y - getY());
        movementSpeedFactor = speed;
    }

    private void moveAwayFrom(Vector2 pos, double speed) {
        movementDirection.set(pos.x - getX(), pos.y - getY());
        movementDirection.mul(-1);
        movementSpeedFactor = speed;
    }

    private void moveAwayFrom(float x, float y, double speed) {
        movementDirection.set(x - getX(), y - getY());
        movementDirection.mul(-1);
        movementSpeedFactor = speed;
    }

    private void standStill() {
        movementDirection.set(
                (float) Math.random() * 2 - 1,
                (float) Math.random() * 2 - 1);
        movementSpeedFactor = 0;
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
        return heart + stomach;
    }

    public double getLength() {
        return fastMoving;
    }

    public void onCreate(TextureAtlas atlas) {
        creatureParticle = new ParticleEffect();

        appearance.create(atlas);
    }

    public void onUpdate(float timeDelta) {
        // Simulate creature
        simulate(timeDelta);

        // Do behaviour
        behave(timeDelta);

        // Update appearance
        appearance.onUpdate(timeDelta);
        if (creatureParticle != null){
            creatureParticle.setPosition(getX() , getY());
            creatureParticle.update(timeDelta);
        }
    }

    private void behave(float timeDelta) {
        timeSinceBehaviourChecked += timeDelta;

        // Check if we should reselect behaviour
        timeUntilBehaviourChange -= timeDelta;
        timeUntilBehaviourReactivation -= timeDelta;
        if (timeUntilBehaviourChange <= 0 || timeUntilBehaviourReactivation <= 0 || currentBehaviour == null || doActionRePrioritation) {
            doActionRePrioritation = false;

            // Update world perception for behaviours
            closestFoodEntity = gameWorld.getClosestFood(getX(), getY(), (float) sightRange);
            closestAppleTree  = gameWorld.getClosestAppleTree(getX(), getY(), (float) sightRange);
            closestCreature   = gameWorld.getClosestCreature(getX(), getY(), this, (float) sightRange);

            // Vote in a new behavior
            double highestImportance = Double.NEGATIVE_INFINITY;
            Behaviour bestBehaviour = null;
            for (Behaviour behaviour : behaviours) {
                double importance = clampToZeroToOne(behaviour.getImportance(timeSinceBehaviourChecked));
                if (importance > highestImportance) {
                    highestImportance = importance;
                    bestBehaviour = behaviour;
                }
            }

            // Change behaviour if needed
            if (bestBehaviour != currentBehaviour || timeUntilBehaviourReactivation <= 0) {

                if (currentBehaviour != null) currentBehaviour.onDeactivated();

                currentBehaviour = bestBehaviour;

                if (currentBehaviour != null) {
                    //System.out.println("currentBehaviour = " + currentBehaviour.getName());
                    currentAction = currentBehaviour.getName();
                    currentBehaviour.onActivated(highestImportance);
                    timeUntilBehaviourReactivation = currentBehaviour.getReActivationTime() * (0.5 + Math.random());
                }
                else {
                    timeUntilBehaviourReactivation = Double.POSITIVE_INFINITY;
                }
            }

            // Add some random factor to next check time to avoid everyone checking at once.
            timeUntilBehaviourChange = BEHAVIOUR_CHECK_INTERVAL_SECONDS * (0.5 + Math.random());
            timeSinceBehaviourChecked = 0;
        }

        // Update behaviour
        if (currentBehaviour != null) {
            currentBehaviour.update(timeDelta);
        }
    }

    private void simulate(float timeDelta) {
        matingBoostTimeLeft -= timeDelta;

        double energyUsagePerSecond = basicEnergyUsagePerSecond;

        // Eating
        if (energy < maxEnergy && closestFoodEntity != null && distanceTo(closestFoodEntity) <= maxEatingDistance) {
            energy += closestFoodEntity.eat(eatingSpeedEnergyPerSecond * timeDelta);
            if (energy > maxEnergy) {
                energy = maxEnergy;
            }
            if (creatureParticle!= null ){
                creatureParticle.load(Gdx.files.internal(closestFoodEntity.getFoodType().getParticles()), game.getAtlas());
                creatureParticle.start();
            }


        }

        // Attacking
        if (energy > 0 && attackTarget != null && distanceTo(attackTarget) <= ATTACK_RANGE) {
            energy -= attackEnergyUsagePerSecond;
            attackTarget.damage((float) attackDamagePerSecond * timeDelta);
            attacking = true;
        }
        else {
            attacking = false;
        }

        // Healing
        if (isWounded()) {
            energyUsagePerSecond += woundedEnergyUsagePerSecond;

            // Heal if energy
            if (energy > 0) {
                health += healPerSecond * timeDelta;
                if (health > maxHealth) health = maxHealth;
            }
        }

        // Mating
        if (matingTarget != null && canMate() && distanceTo(matingTarget) < MATING_DISTANCE) {
            mateWith(matingTarget);
            if (god != null && god.isPlayerGod()&& game!= null && game.soundService != null){
              game.soundService.play(Sounds.KISS);
            }
        }

        // Pregnancy
        if (isPregnant()) {
            energyUsagePerSecond += pregnantEnergyUsagePerSecond;

            // Develop baby if energy
            if (energy > 0) {
                babyDevelopmentTimeLeft -= timeDelta;
                if (babyDevelopmentTimeLeft <= 0) {
                    childBirth();
                }
            }
        }

        // Movement
        movementSpeedFactor = clampToZeroToOne(movementSpeedFactor);
        double currentMovementSpeed = movementSpeedFactor * maxMovementSpeedPerSecond * timeDelta;
        energyUsagePerSecond += movementSpeedFactor * movementEnergyUsagePerSecond;
        if (energy <= 0) {
            // No energy - move slowly
            currentMovementSpeed *= NO_ENERGY_MOVEMENT_SLOWDOWN;
        }
        getVelocity().set(movementDirection);
        getVelocity().nor().mul((float) currentMovementSpeed);

        // Use energy
        energy -= energyUsagePerSecond * timeDelta;

        // Damage if out of energy
        if (energy <= 0) {
            energy = 0;
            trueDamage(DAMAGE_FROM_NO_ENERGY_PER_SECOND * timeDelta);
        }

        // Age
        ageSeconds += timeDelta;
        if (ageSeconds > maxAgeSeconds) {
            trueDamage(DAMAGE_FROM_OLD_AGE_PER_SECOND * timeDelta);
        }
    }

    /**
     * @return 0 if no health, 1 if full health.
     */
    public double getHealthStatus() {
        return clampToZeroToOne(health / maxHealth);
    }

    /**
     * @return 0 if no energy, 1 if max energy
     */
    public double getEnergyStatus() {
        return clampToZeroToOne(energy / maxEnergy);
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
     * @return 0 for just born, 1 for just about to die from old age.
     */
    public double getRelativeAge() {
        return clampToZeroToOne(ageSeconds / maxAgeSeconds);
    }

    /**
     * @return 0 for not booster, 1 for full boost.
     */
    public double getMatingBoost() {
        return clampToZeroToOne(matingBoostTimeLeft / MATING_BOOST_DURATION_SECONDS);
    }

    /**
     * @return 0 for not booster, 1 for full boost.
     */
    public double getRageTargetBoost() {
        return clampToZeroToOne(rageTargetBoostTimeLeft / RAGE_TARGET_BOOST_DURATION_SECONDS);
    }

    /**
     * @return 0 if not pregnant, 0..1 when pregnant, 1 when delivery nearly due.
     */
    public double getPregnancyStatus() {
        if (babyDevelopmentTimeLeft <= 0) return 0;
        else return clampToZeroToOne(1.0 - babyDevelopmentTimeLeft / babyDevelopmentTime);
    }

    public void boostMating() {
        matingBoostTimeLeft = MATING_BOOST_DURATION_SECONDS;
    }

    public void makeRageTarget() {
        rageTargetBoostTimeLeft = RAGE_TARGET_BOOST_DURATION_SECONDS;
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
        if (creatureParticle != null){
            creatureParticle.draw(spriteBatch);
        }
    }

    public void onDispose() {
        appearance.onDispose();
    }

    public void mateWith(Creature dad) {
        if (canMate() && gameWorld.canAddCreatures()) {
            baby = new Creature(game, gameWorld, gameWorld.getMutator(), this, dad);
            babyDevelopmentTimeLeft = babyDevelopmentTime;
        }
    }

    /**
     * Damages a creature
     * @param amount
     */
    public void damage(float amount) {
        // Armor protects
        double damageThrough = mix(armor, amount, amount * MAX_ARMOR_PROTECTION);

        trueDamage(damageThrough);

    }

    /**
     * Gives damage without using armor to defend.
     */
    private void trueDamage(double damageThrough) {

        // Change health
        health -= damageThrough;

        // blood splash (don't work)
        if (creatureParticle!= null ){
            creatureParticle.load(Gdx.files.internal("particles/blood.particle"), game.getAtlas());
            creatureParticle.start();
        }


        // Check if dies
        if (health <= 0) {
            die();

        }
    }

    private void childBirth() {
        // Birth!
        Vector2 worldPos = getWorldPos();
        baby.setWorldPos(
                worldPos.x + 10 * (float) (Math.random() * 2 - 1),
                worldPos.y + 10 * (float) (Math.random() * 2 - 1));
        gameWorld.addEntity(baby);
        if (creatureParticle!= null ){
            creatureParticle.load(Gdx.files.internal("particles/child.particle"), game.getAtlas());
            creatureParticle.start();
        }


        // Baby follows mother in worship
        if (god != null) god.addWorshipper(baby);

        baby = null;
        babyDevelopmentTimeLeft = 0;
    }

    private void die() {
        if (!dead) {
            dead = true;

            // Decrease followers for god
            if (god != null) god.removeWorshipper(this);

            // Dead, remove from game
            gameWorld.removeEntity(this);

            // Spawn some meat there, depending on body size
            gameWorld.spawnFood(FoodType.MEAT, getX(), getY(), energyReleasedOnDeath);


        }
    }

    public String getName() {
        return getFirstName() + " " + getFamilyName();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getEnergy() {
        return energy;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public double getBabyDevelopmentTimeLeft() {
        return babyDevelopmentTimeLeft;
    }

    public boolean isDead() {
        return dead;
    }

    public double getAgeSeconds() {
        return ageSeconds;
    }

    public void setObserved(boolean observed) {
        this.observed = observed;
    }

    public boolean isObserved() {
        return observed;
    }

    /**
     * @return true if this creature is attacking someone else.
     */
    public boolean isAttacking() {
        return attacking;
    }

    /**
     * @return how fast currently moving, 0 = none, 1 = full speed.
     */
    public double getMovementSpeedFactor() {
        return movementSpeedFactor;
    }

    /**
     * @return what it is trying to do now.
     */
    public String getCurrentAction() {
        return currentAction;
    }

    /**
     * Forces to re-check the action next update.
     */
    public void reEvaluateAction() {
        doActionRePrioritation = true;
    }

    @Override
    public float getDrawOrder() {
        // Lift forward if observed
        float drawOrder = super.getDrawOrder();
        if (isObserved()) drawOrder += 200;
        return drawOrder;
    }

    public void rageAttackCreature(Creature target) {
        if (distanceTo(target) < RAGE_ATTACK_RADIUS && target != this) {
            attackTarget = target;
            moveTowards(target.getWorldPos(), 1);
        }
    }
}

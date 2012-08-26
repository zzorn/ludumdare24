package org.ludumdare24.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.gameflow.utils.MathTools;
import org.ludumdare24.MainGame;
import org.ludumdare24.entities.AppleTree;
import org.ludumdare24.entities.FoodEntity;
import org.ludumdare24.entities.God;
import org.ludumdare24.entities.PlayerGod;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.screens.GameScreen;

import java.util.Random;

/**
 *
 */
public class GameWorld {

    private static final int FOOD_SPREAD = 15;
    private static final int MAX_FOOD_ENTITIES_COUNT = 100;
    private final int initialPlayerCreatureCount = 8;
    private final int initialUngodlyCreatureCount =15;
    private final int initialTreeCount = 30;

    private PlayerGod player;

    private Array<Creature> creatures = new Array<Creature>();
    private Array<AppleTree> appleTrees = new Array<AppleTree>();
    private Array<FoodEntity> foodEntities = new Array<FoodEntity>();

    private Array<Entity> entitiesToRemove = new Array<Entity>();
    private Array<Entity> entitiesToAdd = new Array<Entity>();

    private Array<WorldListener> worldListeners = new Array<WorldListener>();
    private Random random = new Random();

    public void create(MainGame game) {
        // Create player
        player = new PlayerGod(game);



        createTribe(400, 200, player, initialPlayerCreatureCount);
        createTribe(1000, 200, null, initialUngodlyCreatureCount);
        createTribe(0, 200, null, initialUngodlyCreatureCount);
        createTribe(400, 0, null, initialUngodlyCreatureCount);
        createTribe(400, 400, null, initialUngodlyCreatureCount);


        // Create some trees
        for (int i = 0; i < initialTreeCount; i++) {
            AppleTree tree = new AppleTree(this, random);
            tree.setWorldPos(random.nextFloat() * 1000, random.nextFloat() * 1000);
            appleTrees.add(tree);
        }
    }

    private void createTribe(int x, int y, God god, int tribeSize) {
        for (int i = 0; i < tribeSize; i++) {
            createCreature(god, x, y);
        }
    }

    private void createCreature(God god, float x, float y) {
        Creature creature = new Creature(this, god );

        creature.randomize(random);

        float x2 = x + (float)(random.nextGaussian() * 10);
        float y2 = y + (float )(random.nextGaussian() * 10);
        creature.setWorldPos(x2, y2);

        addEntity(creature);
    }

    /**
     * Adds food to the game world at some pos.
     */
    public void spawnFood(FoodType foodType, float x, float y, double totalEnergy) {
        if (totalEnergy > 0) {
            // Calculate how many food items to spawn
            int num = Math.max(1, (int) (totalEnergy / foodType.getEnergyInOne())); // At least one

            // Calculate how much energy in each food item
            double energyPerFood = totalEnergy / num;
            double energyPart = energyPerFood / foodType.getEnergyInOne();

            for (int i = 0; i < num && foodEntities.size < MAX_FOOD_ENTITIES_COUNT; i++) {
                // Create a food item
                FoodEntity foodEntity = new FoodEntity(this, random, foodType, energyPart);

                // Put it close to the target point
                foodEntity.setWorldPos(
                        x + (float) random.nextGaussian() * FOOD_SPREAD,
                        y + (float) random.nextGaussian() * FOOD_SPREAD);

                addEntity(foodEntity);
            }
        }
    }

    public void showOnScreen(GameScreen gameScreen) {
        // Show hud
        gameScreen.addEntity(player);

        // Add creatures
        for (Creature creature : creatures) {
            gameScreen.addEntity(creature);
        }

        // Add trees
        for (AppleTree appleTree : appleTrees) {
            gameScreen.addEntity(appleTree);
        }
    }

    public void update(float durationSeconds) {
        // Remove entities to remove
        for (Entity entity : entitiesToRemove) {
            // Remove from correct list
            if (Creature.class.isInstance(entity)) creatures.removeValue((Creature) entity, true);
            else if (AppleTree.class.isInstance(entity)) appleTrees.removeValue((AppleTree) entity, true);
            else if (FoodEntity.class.isInstance(entity)) foodEntities.removeValue((FoodEntity) entity, true);
            else throw new IllegalArgumentException("Unknown entity type " + entity.getClass());

            notifyEntityRemoved(entity);
        }
        entitiesToRemove.clear();

        // Add entities to add
        for (Entity entity : entitiesToAdd) {
            // Add to correct list
            if (Creature.class.isInstance(entity)) creatures.add((Creature) entity);
            else if (AppleTree.class.isInstance(entity)) appleTrees.add((AppleTree) entity);
            else if (FoodEntity.class.isInstance(entity)) foodEntities.add((FoodEntity) entity);
            else throw new IllegalArgumentException("Unknown entity type " + entity.getClass());

            notifyEntityAdded(entity);
        }
        entitiesToAdd.clear();
    }

    public Creature getClosestCreature(float x, float y) {
        float closestDistance = Float.POSITIVE_INFINITY;
        Creature closestCreature = null;
        for (Creature creature : creatures) {
            Vector2 worldPos = creature.getWorldPos();
            float distance = MathTools.distanceSquared(worldPos.x, worldPos.y, x, y);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCreature = creature;
            }
        }

        return closestCreature;
    }

    public void removeEntity(Entity entity) {
        if (!entitiesToRemove.contains(entity, true)) entitiesToRemove.add(entity);
    }

    public void addEntity(Entity entity) {
        if (!entitiesToAdd.contains(entity, true)) entitiesToAdd.add(entity);
    }

    public void addListener(WorldListener value) {
        worldListeners.add(value);
    }

    public void removeListener(WorldListener value) {
        worldListeners.removeValue(value, true);
    }

    private void notifyEntityAdded(Entity entity) {
        for (WorldListener worldListener : worldListeners) {
            worldListener.onEntityCreated(entity);
        }
    }

    private void notifyEntityRemoved(Entity entity) {
        for (WorldListener worldListener : worldListeners) {
            worldListener.onEntityRemoved(entity);
        }
    }


    public Random getRandom() {
        return random;
    }
}

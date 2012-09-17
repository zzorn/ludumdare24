package org.ludumdare24.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.ludumdare24.MainGame;
import org.ludumdare24.Mutator;
import org.ludumdare24.entities.*;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.screens.GameScreen;

import java.util.Random;

import static org.gameflow.utils.MathTools.*;

/**
 *
 */
public class GameWorld {

    private static final int MAX_CREATURES_COUNT = 300;
    private static final int FOOD_SPREAD = 15;
    private static final int MAX_FOOD_ENTITIES_COUNT = 100;

    public static final int MAX_LEVEL = 10;


    private final int initialPlayerCreatureCount = 5;

    private final int initialUngodlyCreatureCount = 5;
    private final int finalUngodlyCreatureCount = 20;
    private final int initialUngodlyTribeCount = 1;
    private final int finalUngodlyTribeCount = 5;

    private final int initialTreeCount = 30;
    private final int finalTreeCount = 5;

    private int level = 1;

    private PlayerGod player;

    private Array<Creature> creatures = new Array<Creature>();
    private Array<AppleTree> appleTrees = new Array<AppleTree>();
    private Array<FoodEntity> foodEntities = new Array<FoodEntity>();

    private TileMap tileMap;

    private Array<Entity> entitiesToRemove = new Array<Entity>();
    private Array<Entity> entitiesToAdd = new Array<Entity>();

    private Array<Level> levels = new Array<Level>();

    private Array<WorldListener> worldListeners = new Array<WorldListener>();
    private Random random = new Random();

    private Mutator mutator = new Mutator(random);

    public GameWorld() {
        levels.add(new Level("Home Forest",    5,   5, 200, "grassterrain",  4, 2, 1,   1, 5, 0.5, 0.8, 0.2, 0.8, 0.4));
        levels.add(new Level("Over the Hills", 4,   3,  50, "hillterrain",   4, 2, 1.5,   2, 4, 0.6, 0.5, 0.5, 0.7, 0.5));
        levels.add(new Level("Dry Savannah",   6,   1,  10, "desertterrain", 4, 1, 1,   3, 6, 0.7, 0.5, 0.5, 0.7, 0.5));

        levels.add(new Level("Apple Valley",   3,   6, 100, "grassterrain",  4, 2, 1,   4, 7, 0.7, 0.5, 0.5, 0.4, 0.7));
        levels.add(new Level("Rabbit Hills",   7,   1,  20, "hillterrain",   4, 2, 1.5,   7, 5, 0.8, 0.5, 0.5, 0.6, 0.5));
        levels.add(new Level("Skull Hollow",   1,   3,  40, "desertterrain", 4, 1, 1,   3, 10, 0.7, 0.5, 1, 1, 0.3));

        levels.add(new Level("Cloud Forest",   7,   3,  15, "grassterrain",   4, 2, 2,   5, 15, 0.6, 0.5, 0.5, 0.5, 0.5));
        levels.add(new Level("Desert Tribes", 20,   1,  15, "desertterrain",  4, 3, 2,   10, 7, 0.8, 1, 0.5, 0.5, 1));
        levels.add(new Level("Horde Hill",     2,   4, 120, "hillterrain",   4, 2, 1.5,   1, 100, 0.7, 0.5, 0.5, 0.5, 0.5));

        levels.add(new Level("Troll Country",  4,   4,  50, "grassterrain", 4, 1.5, 0.75,   4, 20, 1, 1, 1, 0.7, 0.5));
    }

    public int getLevel() {
        return level;
    }

    public Level getLevelData() {
        return levels.get(level - 1);
    }

    public Level getLevelDataForLevel(int l) {
        return levels.get(l - 1);
    }

    public void create(MainGame game) {
        // Create player
        player = new PlayerGod(game);

        final int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();

        int cx = w / 2;
        int cy = h / 2;

        createTribe(game, cx, cy, player, initialPlayerCreatureCount, Math.random() * 0.8 + 0.2, getLevelData());

        setupLevel(game, 1);

    }

    public void setupLevel(MainGame game, int level) {

        this.level = level;

        Level levelData = getLevelData();

        final int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();

        // Create tribes
        float cx = w / 2;
        float cy = h / 2;

        float distanceFactor = 0.75f;

        int tribeCount = levelData.getTribeCount();
        for (int i = 0; i < tribeCount; i++) {
            final float a = (float) Math.random() * TauFloat;
            float tribeX = cx + (float) Math.cos(a) * distanceFactor * cx;
            float tribeY = cy + (float) Math.sin(a) * distanceFactor * cy;
            int memberCount = levelData.getTribeHeadCount();
            createTribe(game, tribeX, tribeY, null, memberCount, 0, levelData);
        }

        // Create some trees
        appleTrees.clear();
        for (int group = 0; group < levelData.getTreeGroups(); group++) {
            float groupCenterX = cx + (random.nextFloat() - 0.5f) * w * 0.8f;
            float groupCenterY = cy + (random.nextFloat() - 0.5f) * h * 0.8f;
            for (int i = 0; i < levelData.getTreeGroupTreeCount(); i++) {
                float treeX = groupCenterX + (float) (random.nextGaussian() * levelData.getTreeGroupSize());
                float treeY = groupCenterY + (float) (0.75f * random.nextGaussian() * levelData.getTreeGroupSize());

                AppleTree tree = new AppleTree(this, random);
                tree.setWorldPos(treeX, treeY);
                appleTrees.add(tree);
            }
        }

        // Create tile map
        tileMap = new TileMap(level, levelData);

    }

    private void createTribe(MainGame game, float x, float y, God god, int tribeSize, final double hat, Level levelData) {

        // Place move target
        if (god != null) god.placeMoveTarget(x, y);

        // Tribe mother
        Creature tribeMother = createCreature(game, god, x, y, null, hat, levelData);

        // Spawn members based on mother
        for (int i = 0; i < tribeSize; i++) {
            createCreature(game, god, x, y, tribeMother, hat, levelData);
        }
    }

    private Creature createCreature(MainGame game, God god, float x, float y, Creature mother, double hat, Level levelData) {

        Creature creature;
        if (mother == null) {
            if (god == null) {
                // Adjust enemy properties based on level
                creature = new Creature(game, this, god, mutator, hat, levelData);
            }
            else {
                // Randomize players creatures
                creature = new Creature(game, this, god, mutator, hat, null);
            }

        } else {
            creature = new Creature(game, this, mutator, mother);
        }

        float x2 = x + (float)(random.nextGaussian() * 120);
        float y2 = y + (float )(random.nextGaussian() * 80);
        creature.setWorldPos(x2, y2);

        addEntity(creature);

        // Count creature for god
        if (god != null) {
            god.addWorshipper(creature);
        }

        return creature;
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

        // Add tilemap
        gameScreen.addEntity(tileMap);
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

    public Creature getClosestCreature(float x, float y, Creature exceptThis) {
        return (Creature) findClosestEntity(x, y, null, creatures);
    }

    public Creature getClosestCreatureOfGod(float x, float y, God god) {
        float closestDistance = Float.POSITIVE_INFINITY;
        Creature closestEntity = null;
        for (Creature entity : creatures) {
            if (entity.getGod() == god) {
                Vector2 worldPos = entity.getWorldPos();
                float distance = distanceSquared(worldPos.x, worldPos.y, x, y);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }
        return closestEntity;
    }

    public Creature getClosestCreature(float x, float y, Creature exceptThis, float withinDistance) {
        return (Creature) findClosestEntity(x, y, exceptThis, creatures, withinDistance);
    }

    public FoodEntity getClosestFood(float x, float y) {
        return (FoodEntity) findClosestEntity(x, y, null, foodEntities);
    }

    public FoodEntity getClosestFood(float x, float y, float withinDistance) {
        return (FoodEntity) findClosestEntity(x, y, null, foodEntities, withinDistance);
    }

    public AppleTree getClosestAppleTree(float x, float y) {
        return (AppleTree) findClosestEntity(x, y, null, appleTrees);
    }

    public AppleTree getClosestAppleTree(float x, float y, float withinDistance) {
        return (AppleTree) findClosestEntity(x, y, null, appleTrees, withinDistance);
    }

    private WorldEntity findClosestEntity(float x, float y, WorldEntity exceptThis, Array<? extends WorldEntity> entities, float withinDistance) {
        WorldEntity closestEntity = findClosestEntity(x, y, exceptThis, entities);
        if (closestEntity != null) {
            float distance = distance(closestEntity.getX(), closestEntity.getY(), x, y);
            if (distance > withinDistance) return null; // Too far away
        }
        return closestEntity;
    }

    private WorldEntity findClosestEntity(float x, float y, WorldEntity exceptThis, Array<? extends WorldEntity> entities) {
        float closestDistance = Float.POSITIVE_INFINITY;
        WorldEntity closestEntity = null;
        for (WorldEntity entity : entities) {
            if (entity != exceptThis) {
                Vector2 worldPos = entity.getWorldPos();
                float distance = distanceSquared(worldPos.x, worldPos.y, x, y);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }
        return closestEntity;
    }

    public void removeEntity(Entity entity) {
        if (!entitiesToRemove.contains(entity, true)) {
            entitiesToRemove.add(entity);
        }
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

    public int getNumberOfCreatures() {
        return creatures.size;
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

    public Mutator getMutator() {
        return mutator;
    }

    public boolean canAddCreatures() {
        return creatures.size < MAX_CREATURES_COUNT;
    }

    public Array<Creature> getCreatures() {
        return creatures;
    }
}

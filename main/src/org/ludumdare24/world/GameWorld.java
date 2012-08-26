package org.ludumdare24.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.utils.MathTools;
import org.ludumdare24.MainGame;
import org.ludumdare24.entities.AppleTree;
import org.ludumdare24.entities.God;
import org.ludumdare24.entities.PlayerGod;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.screens.GameScreen;

import java.util.Random;

/**
 *
 */
public class GameWorld {

    private final int initialCreatureCountPerGod = 30;
    private final int initialTreeCount = 30;

    private PlayerGod player;

    private Array<Creature> creatures = new Array<Creature>();
    private Array<AppleTree> appleTrees = new Array<AppleTree>();

    private Array<WorldListener> worldListeners = new Array<WorldListener>();
    private Random random = new Random();

    public void create(MainGame game) {
        // Create player
        player = new PlayerGod(game);

        // Create players creatures
        for (int i = 0; i < initialCreatureCountPerGod; i++) {
            createCreature(player);
        }

        // Create some trees
        for (int i = 0; i < initialTreeCount; i++) {
            AppleTree tree = new AppleTree(random);
            tree.setWorldPos(random.nextFloat() * 1000, random.nextFloat() * 1000);
            appleTrees.add(tree);
        }
    }

    private void createCreature(God god) {
        Creature creature = new Creature(this);

        creature.randomize(random);

        creature.setWorldPos(random.nextFloat() * 1000, random.nextFloat() * 1000);

        addCreature(creature);
    }

    public void addCreature(Creature creature) {
        creatures.add(creature);
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

    public void addListener(WorldListener value) {
        worldListeners.add(value);
    }

    public void removeListener(WorldListener value) {
        worldListeners.removeValue(value, true);
    }

    /**
     * Called by the creature when it died
     */
    public void onCreatureDied(Creature creature) {
        for (WorldListener listener : worldListeners) {
            creatures.removeValue(creature, true);
            listener.onEntityRemoved(creature);
        }
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

}

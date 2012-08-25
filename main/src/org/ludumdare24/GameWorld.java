package org.ludumdare24;

import com.badlogic.gdx.utils.Array;
import org.gameflow.utils.ColorUtils;
import org.ludumdare24.entities.God;
import org.ludumdare24.entities.PlayerGod;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.screens.GameScreen;

import java.util.Random;

/**
 *
 */
public class GameWorld {

    private static final int INITIAL_CREATURES_PER_GOD = 30;

    private PlayerGod player;

    private Array<Creature> creatures = new Array<Creature>();

    private Random random = new Random();

    public void create(MainGame game) {
        // Create player
        player = new PlayerGod(game);

        // Create players creatures
        for (int i = 0; i < INITIAL_CREATURES_PER_GOD; i++) {
            createCreature(player);
        }
    }

    private void createCreature(God god) {
        Creature creature = new Creature();

        creature.randomize(random);

        creature.setPos(random.nextFloat() * 1000, random.nextFloat() * 1000);

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
    }


}

package org.ludumdare24.screens;

import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.entities.PlayerGod;
import org.ludumdare24.world.GameWorld;
import org.ludumdare24.world.WorldListener;

/**
 *
 */
public class GameScreen extends Screen2D implements WorldListener {

    private final MainGame game;

    public GameScreen(MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game = game;
    }

    @Override
    protected void onCreate() {
        GameWorld gameWorld = game.getGameWorld();
        gameWorld.showOnScreen(this);
        gameWorld.addListener(this);
    }

    @Override
    protected void onDispose() {
        if (game.isGameWorldCreated()) {
            GameWorld gameWorld = game.getGameWorld();
            gameWorld.removeListener(this);
        }
    }

    public void onEntityCreated(Entity entity) {
        addEntity(entity);
    }

    public void onEntityRemoved(Entity entity) {
        removeEntity(entity);
    }
}

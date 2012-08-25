package org.ludumdare24.screens;

import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.entities.PlayerGod;

/**
 *
 */
public class GameScreen extends Screen2D {

    private final MainGame game;
    private PlayerGod player;

    private Creature creature;

    public GameScreen(MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game = game;
    }

    @Override
    protected void onCreate() {
        // Create player
        player = new PlayerGod(game);
        addEntity(player);

        creature = new Creature();
        addEntity(creature);
    }

    @Override
    protected void onDispose() {

    }
}

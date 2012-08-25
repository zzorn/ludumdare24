package org.ludumdare24.screens;

import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.entities.PlayerGod;

/**
 *
 */
public class GameScreen extends Screen2D {

    private final MainGame game;
    private PlayerGod player;

    public GameScreen(MainGame game) {
        super(game.getUiScale());
        this.game = game;
    }

    @Override
    protected void onCreate() {
        // Create player
        player = new PlayerGod();
        addEntity(player);
    }

    @Override
    protected void onDispose() {

    }
}

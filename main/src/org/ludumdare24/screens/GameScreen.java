package org.ludumdare24.screens;

import com.badlogic.gdx.utils.Array;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.entities.PlayerGod;

/**
 *
 */
public class GameScreen extends Screen2D {

    private final MainGame game;

    public GameScreen(MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game = game;
    }

    @Override
    protected void onCreate() {
        game.getGameWorld().showOnScreen(this);
    }

    @Override
    protected void onDispose() {

    }
}

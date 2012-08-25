package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gameflow.entity.Entity;
import org.gameflow.screen.Screen2D;

/**
 *
 */
public class Creature implements Entity {

    public Actor create(Screen2D screen2D) {

        return null;
    }

    public void update(float timeDelta) {

    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        spriteBatch.draw(atlas.findRegion("testCreature"), 100, 100, 100, 100, 64, 64, 2, 1, 0.44f);

    }

    public void dispose() {

    }
}
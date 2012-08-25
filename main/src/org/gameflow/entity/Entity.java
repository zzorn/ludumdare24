package org.gameflow.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gameflow.screen.Screen2D;

/**
 *
 */
public interface Entity {

    Actor create(TextureAtlas atlas, Screen2D screen2D);

    void update(float timeDelta);

    void render(TextureAtlas atlas, SpriteBatch spriteBatch);

    void dispose();

}

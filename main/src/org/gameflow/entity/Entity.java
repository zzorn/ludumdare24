package org.gameflow.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gameflow.screen.Screen2D;

/**
 *
 */
public interface Entity {

    Actor create(Screen2D screen2D);

    void update(float timeDelta);

    void render(SpriteBatch spriteBatch);

    void dispose();

}

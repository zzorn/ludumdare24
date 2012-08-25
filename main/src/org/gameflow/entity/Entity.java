package org.gameflow.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *
 */
public interface Entity {

    Actor create();

    void update(float timeDelta);

    void render(SpriteBatch spriteBatch);

    void dispose();

}

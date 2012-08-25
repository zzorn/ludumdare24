package org.ludumdare24.entities.creature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.gameflow.screen.Screen2D;

/**
 *
 */
public class Creature implements Entity {

    private Array<CreaturePart> parts = new Array<CreaturePart>();
    public Vector2 pos = new Vector2();
    public float angle = 0;

    public Actor create(Screen2D screen2D) {
        Color c = new Color();

        parts.add(new CreaturePart(BodyPartShape.POTATO, 1, 1, 0.5, 0, Color.RED, Color.YELLOW, Color.WHITE, new Vector2(0,0), 0));
        parts.add(new CreaturePart(BodyPartShape.POTATO, 1, 1, 1, 0, Color.ORANGE, Color.YELLOW, Color.WHITE, new Vector2(0,60), 0));

        pos.set(400, 200);

        return null;
    }

    public void update(float timeDelta) {

    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        for (CreaturePart part : parts) {
            part.draw(atlas, spriteBatch, pos, angle);
        }
    }

    public void dispose() {
    }
}

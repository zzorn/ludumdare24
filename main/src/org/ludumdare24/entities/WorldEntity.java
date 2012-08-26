package org.ludumdare24.entities;

import com.badlogic.gdx.math.Vector2;
import org.gameflow.entity.Entity;

/**
 *
 */
public abstract class WorldEntity extends Entity {

    private Vector2 pos = new Vector2();
    private Vector2 velocity = new Vector2();

    public Vector2 getWorldPos() {
        return pos;
    }

    public void setWorldPos(float x, float y) {
        pos.set(x, y);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public final void update(float timeDelta) {
        onUpdate(timeDelta);
        updateMovement(timeDelta);
    }

    protected void onUpdate(float timeDelta) {}

    protected void updateMovement(float timeDelta) {
        pos.x += velocity.x * timeDelta;
        pos.y += velocity.y * timeDelta;
    }

}

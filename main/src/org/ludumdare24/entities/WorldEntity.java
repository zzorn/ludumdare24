package org.ludumdare24.entities;

import com.badlogic.gdx.math.Vector2;
import org.gameflow.entity.Entity;
import org.gameflow.utils.MathTools;

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

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public final void update(float timeDelta) {
        onUpdate(timeDelta);
        updateMovement(timeDelta);
    }

    @Override
    public float getDrawOrder() {
        return -pos.y;
    }

    protected void onUpdate(float timeDelta) {}

    protected void updateMovement(float timeDelta) {
        pos.x += velocity.x * timeDelta;
        pos.y += velocity.y * timeDelta;
    }

    /**
     * Distance to other entity.
     */
    public double distanceTo(WorldEntity entity) {
        return MathTools.distance(entity.getX(), entity.getY(), getX(), getY());
    }



}

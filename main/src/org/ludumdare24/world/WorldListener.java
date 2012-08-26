package org.ludumdare24.world;

import org.gameflow.entity.Entity;

/**
 * Listener that is notified about changed to the game world.
 */
public interface WorldListener {

    void onEntityCreated(Entity entity);

    void onEntityRemoved(Entity entity);

}

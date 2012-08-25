package org.gameflow.services.levels;

import com.badlogic.gdx.utils.Array;

/**
 * Something that can generate levels.
 */
public interface LevelGenerator {

    /**
     * Do any needed initialization.
     */
    void startup();

    /**
     * @return the id of the default first level.
     */
    String getStartLevel();

    /**
     * @return the levels that are unlocked from the start.
     */
    Array<String> getInitiallyUnlockedLevels();

    /**
     * @return a level instance with the specified id.
     */
    Level getLevel(String levelId);

    /**
     * Clear out any allocated resources.
     * (Created screens will be disposed by the caller).
     */
    void shutdown();
}

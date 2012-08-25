package org.gameflow.services.levels;

import com.badlogic.gdx.utils.Array;
import org.gameflow.Service;

/**
 * Manages levels.
 */
public interface LevelService extends Service {

    /**
     * Starts playing the first level.
     */
    void startFirstLevel();

    /**
     * Starts playing the specified level.
     */
    void startLevel(String levelId);

    /**
     * Should be called when a level completes successfully.
     * Unlocks next levels.
     * @return the unlocked next levels.
     */
    Array<String> levelCompleted(Level level);

    /**
     * @return returns or re-creates the specified level.
     */
    Level getLevel(String levelId);

    /**
     * @return the currently active level, or null if none.
     */
    Level getCurrentLevel();


    /**
     * @return all unlocked levels.
     */
    Array<String> getUnlockedLevels();

    /**
     * Adds an unlocked level.
     */
    void addUnlockedLevel(String levelId);

    /**
     * Adds a set of unlocked levels.
     */
    void addUnlockedLevels(Array<String> unlockedLevels);
}

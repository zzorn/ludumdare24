package org.gameflow.services.levels;

import com.badlogic.gdx.utils.Array;
import org.gameflow.screen.Screen;

/**
 * Data about a level in the game.
 */
public interface Level {

    /**
     * @return id of this level
     */
    String getLevelId();

    /**
     * @return levels that are unlocked when completing this level.
     */
    Array<String> getNextLevels();

    /**
     * @return a screen that the level can be played on.
     * Should call LevelService.levelCompleted when the player passes the level, so that the next levels get unlocked.
     * May call LevelService.startLevelChooser when player exits the level, to allow the player to pick another one.
     */
    Screen getScreen();

}

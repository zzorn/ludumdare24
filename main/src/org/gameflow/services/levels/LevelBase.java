package org.gameflow.services.levels;

import com.badlogic.gdx.utils.Array;
import org.gameflow.screen.Screen;

/**
 * Base class for levels.
 */
public abstract class LevelBase implements Level {

    private final String levelId;
    private final Array<String> nextLevels = new Array<String>();
    private Screen levelScreen = null;

    public LevelBase(String levelId) {
        this.levelId = levelId;
    }

    public LevelBase(String levelId, Array<String> nextLevels) {
        this.levelId = levelId;
        this.nextLevels.addAll(nextLevels);
    }

    public LevelBase(String levelId, String nextLevel) {
        this.levelId = levelId;
        this.nextLevels.add(nextLevel);
    }


    public final String getLevelId() {
        return levelId;
    }

    public Array<String> getNextLevels() {
        return nextLevels;
    }

    public Screen getScreen() {
        if (levelScreen == null) {
            levelScreen = createLevelScreen();
        }

        return levelScreen;
    }

    protected abstract Screen createLevelScreen();

}

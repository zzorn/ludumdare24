package org.gameflow.services.levels;

import com.badlogic.gdx.utils.Array;

/**
 * Implements some common functionality of level generators.
 */
public abstract class LevelGeneratorBase implements LevelGenerator {

    private final Array<String> initiallyUnlockedLevels = new Array<String>();

    protected LevelGeneratorBase() {
    }

    protected LevelGeneratorBase(String initialLevelName) {
        this.initiallyUnlockedLevels.add(initialLevelName);
    }

    protected LevelGeneratorBase(Array<String> initiallyUnlockedLevels) {
        this.initiallyUnlockedLevels.addAll(initiallyUnlockedLevels);
    }

    public void addInitiallyUnlockedLevel(String levelId) {
        initiallyUnlockedLevels.add(levelId);
    }

    @Override
    public void startup() {}

    @Override
    public String getStartLevel() {
        return initiallyUnlockedLevels.size <= 0 ? null : initiallyUnlockedLevels.get(0);
    }

    @Override
    public final Array<String> getInitiallyUnlockedLevels() {
        return initiallyUnlockedLevels;
    }

    @Override
    public Level getLevel(String levelId) {
        // TODO: Do caching if needed
        return createLevel(levelId);
    }

    protected abstract Level createLevel(String levelId);

    @Override
    public void shutdown() {}

}

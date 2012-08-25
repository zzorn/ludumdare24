package org.gameflow.services.levels;

import com.badlogic.gdx.utils.Array;
import org.gameflow.ServiceBase;
import org.gameflow.Game;
import org.gameflow.services.options.OptionsService;

/**
 * Implementation of LevelService.
 */
public class LevelServiceImpl extends ServiceBase implements LevelService {

    private static final String UNLOCKED_LEVELS_KEY = "LevelService.unlockedLevels";
    private final LevelGenerator levelGenerator;
    private final OptionsService optionsService;
    private final Game game;

    private Array<String> unlockedLevels = new Array<String>();

    private Level currentLevel = null;

    public LevelServiceImpl(LevelGenerator levelGenerator, Game game, OptionsService optionsService) {
        this.levelGenerator = levelGenerator;
        this.game = game;
        this.optionsService = optionsService;
    }

    @Override
    public void create() {
        // Startup level generator
        levelGenerator.startup();

        // Get unlocked levels.
        unlockedLevels.addAll(levelGenerator.getInitiallyUnlockedLevels());
        unlockedLevels = optionsService.get(UNLOCKED_LEVELS_KEY, unlockedLevels);
    }

    public Level getLevel(String levelId) {
        // TODO: Maybe cache levels, if they do not take much memory? (most memory in levelscreen?)
        if (currentLevel != null && currentLevel.getLevelId().equals(levelId)) {
            return currentLevel;
        }
        else {
            return levelGenerator.getLevel(levelId);
        }
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void startFirstLevel() {
        String firstLevel = levelGenerator.getStartLevel();
        startLevel(firstLevel);
    }

    public void startLevel(String levelId) {
        log("Changing level to '"+levelId+"'");

        // Stop earlier level
        currentLevel = null;

        // Change current level
        if (levelId != null && !(levelId.trim().length() == 0)) {
            currentLevel = levelGenerator.getLevel(levelId);
        }

        // Show new level screen
        if (currentLevel != null) {
            game.setScreen(currentLevel.getScreen());
        }
    }

    public Array<String> levelCompleted(Level level) {
        // Unlock new levels
        Array<String> nextLevels = level.getNextLevels();
        addUnlockedLevels(nextLevels);

        return nextLevels;
    }

    public Array<String> getUnlockedLevels() {
        return unlockedLevels;
    }

    public void addUnlockedLevel(String levelId) {
        if (!unlockedLevels.contains(levelId, false)) {
            unlockedLevels.add(levelId);

            // Save
            optionsService.set(UNLOCKED_LEVELS_KEY, unlockedLevels);
        }
    }

    public void addUnlockedLevels(Array<String> unlockedLevels) {
        for (String unlockedLevel : unlockedLevels) {
            addUnlockedLevel(unlockedLevel);
        }
    }

    @Override
    public void dispose() {
        levelGenerator.shutdown();
    }

}

package org.ludumdare24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.gameflow.GameBase;
import org.gameflow.services.levels.LevelGenerator;
import org.gameflow.services.levels.LevelService;
import org.gameflow.services.levels.LevelServiceImpl;
import org.gameflow.services.options.InMemoryOptionsService;
import org.gameflow.services.options.OptionsService;
import org.gameflow.services.sound.SoundResourceHandle;
import org.gameflow.services.sound.SoundService;
import org.gameflow.services.sound.SoundServiceImpl;
import org.ludumdare24.screens.MainMenuScreen;

/**
 *
 */
public class MainGame extends GameBase {

    public final OptionsService optionsService = addService(new InMemoryOptionsService());
    public final SoundService soundService = addService(new SoundServiceImpl());

    private TextureAtlas atlas;


    public TextureAtlas getAtlas() {
        return atlas;
    }

    public double getUiScale() {
        return 2.0;
    }

    @Override
    protected void setup() {
        atlas = new TextureAtlas(Gdx.files.internal("images/images.pack"));
        setScreen(new MainMenuScreen(this));
    }

    @Override
    protected void onShutdownStarted() {
        atlas.dispose();
    }
}

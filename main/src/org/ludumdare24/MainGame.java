package org.ludumdare24;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.gameflow.GameBase;
import org.gameflow.Service;
import org.gameflow.ServiceBase;
import org.gameflow.services.options.InMemoryOptionsService;
import org.gameflow.services.options.OptionsService;
import org.gameflow.services.sound.SoundService;
import org.gameflow.services.sound.SoundServiceImpl;
import org.ludumdare24.screens.HelpPageScreen;
import org.ludumdare24.screens.MainMenuScreen;
import org.ludumdare24.world.GameWorld;

/**
 *
 */
public class MainGame extends GameBase {

    public final OptionsService optionsService = addService(new InMemoryOptionsService());
    public final SoundService soundService = addService(new SoundServiceImpl());
    public final Service gameWorldUpdateService = addService(new ServiceBase() {
        @Override
        public void update(float deltaSeconds) {
            if (gameWorld != null) gameWorld.update(deltaSeconds);
        }
    });

    private TextureAtlas atlas;
    private GameWorld gameWorld = null;

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public GameWorld getGameWorld() {
        if (gameWorld == null) {
            gameWorld = new GameWorld();
            gameWorld.create(this);
        }
        return gameWorld;
    }

    public boolean isGameWorldCreated() {
        return gameWorld != null;
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

    public void clearGameWorld() {
        gameWorld = null;
    }


    public int getHelpPageCount() {
        // NOTE: Remember to update this if you add more help pages!
        return 7;
    }

    public HelpPageScreen createHelpPage(int index) {
        switch (index) {
            case 1: return createHelpPageWithContent(index, null,
                    "You are the god of a species of trolls.",
                    "Your task is to look out for your worshippers",
                    "and help them to evolve and survive.",
                    "But beware!  You are not the only god",
                    "in this world.");
            case 2: return createHelpPageWithContent(index, "smiteButtonNotPressScaled",
                    "With the smite tool you can kill both enemy",
                    "trolls and your own worshippers.");
            case 3: return createHelpPageWithContent(index, "heartButtonNotPressScaled",
                    "With the love button you can help your",
                    "trolls to fall in love, and get children.",
                    "They can fall in love on their own also.");
            case 4: return createHelpPageWithContent(index, "moveButtonNotPressScaled",
                    "With move you can command your nearby creeps",
                    "to go towards the place you click.",
                    "They will usually obey you",
                    "but they have a mind of their own too.");
            case 5: return createHelpPageWithContent(index, "rageButtonNotPressScaled",
                    "When you click an enemy with the rage tool",
                    "your nearby trolls will heedlessly attack the target",
                    "and fight to the last troll to kill it - ",
                    "even if it is one of their own.");
            case 6: return createHelpPageWithContent(index, "foodButtonNotPressScaled",
                    "With the feed tool you can toss apples from the sky",
                    "to your faithful followers as a prize,",
                    "or to save them if they are dying in hunger.");
            case 7: return createHelpPageWithContent(index, null,
                    "Using your tools will drain yor mana.",
                    "But your mana will recover over time.");
        }

        return createHelpPageWithContent(0, null, "Unknown help page");
    }

    private HelpPageScreen createHelpPageWithContent(int index, String imageName, String ... text) {
        return new HelpPageScreen(this, index, imageName, text);
    }


}

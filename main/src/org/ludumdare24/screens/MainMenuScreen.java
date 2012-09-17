package org.ludumdare24.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.Sounds;

/**
 *
 */
public class MainMenuScreen extends Screen2D {


    private final MainGame game;

    public MainMenuScreen(MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game =game;
    }

    @Override
    protected void onCreate() {

        Table table = new Table(getSkin());

        // Start game
        String gameButtonName = "Start game";
        if (game.isGameWorldCreated()) {
            gameButtonName = "Resume game";
        }
        table.add(createButton(gameButtonName, new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new GameScreen(game));
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);

        // Options
        table.row();
        table.add(createButton("Options", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new OptionsScreen(game));
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);

        // Help
        table.row();
        table.add(createButton("Help", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(game.createHelpPage(1) );
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);

        // Credits
        if (game.isGameWorldCreated()) {
            table.row();
            table.add(createButton("New game", new ClickListener() {
                public void click(Actor actor, float x, float y) {
                    game.clearGameWorld();
                    game.setScreen(new GameScreen(game));
                    game.soundService.play(Sounds.UI_CLICK);
                }
            })).fillX().padBottom(10);
        }

        // Help
        table.row();
        table.add(createButton("Credits", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new CreditScreen(game) );
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);

        // Quit
        table.row();
        table.add(createButton("Quit game", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.soundService.play(Sounds.QUIT);
                // Let sound play
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // do nothing
                }
                Gdx.app.exit();
            }
        })).fillX().padBottom(10);

        table.setFillParent(true);

        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }


}

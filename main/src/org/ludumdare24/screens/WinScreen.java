package org.ludumdare24.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.Sounds;

/**
 *
 */
public class WinScreen extends Screen2D {


    private final MainGame game;
    private ParticleEffect winParticle=null;


    public WinScreen(MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game =game;
    }

    @Override
    protected void onCreate() {
        winParticle = new ParticleEffect();
        winParticle.load(Gdx.files.internal("particles/winn.particle"), getAtlas());
        winParticle.start();
        winParticle.setPosition(Gdx.graphics.getWidth()/2,0);


        Table table = new Table(getSkin());


        table.add(new Label("You Win!", getSkin())).padBottom(30);
        table.row();

        table.add(new Label("Your trolls are the only ones left alive!", getSkin())).left();
        table.row();


        table.add(createButton("Continue playing", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                // Add some more enemies to play against
                game.getGameWorld().createEnemyTribes(game);

                game.setScreen(new GameScreen(game));
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);

        // Options
        table.row();
        table.add(createButton("Main Menu", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                // Clear the game world so we can't resume it.
                game.clearGameWorld();
                game.setScreen(new MainMenuScreen(game));
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
    public void onTopLayerRender() {
        winParticle.draw(getBatch());

    }



    @Override
    protected void onUpdate(float deltaSeconds) {
     winParticle.update(deltaSeconds);
    }

    @Override
    protected void onDispose() {
    }


}

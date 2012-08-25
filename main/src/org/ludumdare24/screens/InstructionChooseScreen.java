package org.ludumdare24.screens;

import com.badlogic.gdx.Gdx;
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
public class InstructionChooseScreen extends Screen2D {


    private final MainGame game;

    public InstructionChooseScreen(MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game =game;
    }

    @Override
    protected void onCreate() {

        Table table = new Table(getSkin());

        table.add(new Label("Choose 'All' to see all instructions or   ", getSkin())).top().colspan(10);
        table.row();
        table.add(new Label("a specific section to jum right there.   ", getSkin())).top().colspan(10) ;
        table.row();


        table.add(createButton("All", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new InstructionScreen(game));
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);


        table.row();


        table.add(createButton("Tools", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new InstructionScreen2(game));
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);

        table.row();

        table.add(createButton("Something", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new InstructionScreen8(game) );
                game.soundService.play(Sounds.UI_CLICK);
            }
        })).fillX().padBottom(10);

        table.row();


        table.setFillParent(true);

        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }


}

package org.ludumdare24.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.Sounds;

/**
 *
 */
public class InstructionScreen extends Screen2D {

    private static final String SOUND_ENABLED = "soundEnabled";

    private final MainGame game;

    public InstructionScreen (MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game = game;
    }

    @Override
    protected void onCreate() {
        Table table = new Table(getSkin());
        table.setFillParent(true);

        // Return button
        table.add(createButton("Return", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).expandX().top().left();

        table.add(createButton("Next", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new InstructionScreen2(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).expandX().top().right();

        table.row();
        // Toggle sounds enabled checkbox
        table.add(new Label("Instruction", getSkin())).expand().top();
        table.row();
        table.add(new Label("You are the god of a tribe of creeps." ,getSkin())).top();
        table.row();
        table.add(new Label("Your task is to look out for your worshippers" ,getSkin())).top();
        table.row();
        table.add(new Label("and help them to evolve and survive." ,getSkin())).top();
        table.row();
        table.add(new Label("But beware!! You are not the only god" ,getSkin())).top();
        table.row();
        table.add(new Label("in this world. There are others who" ,getSkin())).top();
        table.row();
        table.add(new Label("wants their worshippers to survive" ,getSkin())).top();
        table.row();
        table.add(new Label("because wats a god without supporters?" ,getSkin())).top();




        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }


}

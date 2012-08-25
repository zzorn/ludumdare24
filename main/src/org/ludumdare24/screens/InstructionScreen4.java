package org.ludumdare24.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.Sounds;

/**
 *
 */
public class InstructionScreen4 extends Screen2D {

    private static final String SOUND_ENABLED = "soundEnabled";

    private final MainGame game;

    public InstructionScreen4 (MainGame game) {
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
                game.setScreen(new InstructionScreen5(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).expandX().top().right();

        table.row();

        table.add(new Label("Tools", getSkin())).expand().top();
        table.row();
        table.add(new Label("An other way to control them is move: " ,getSkin())).top();
        table.add(new Image(getAtlas().findRegion("moveButtonNotPressScaled") ));
        table.row();
        table.add(new Label("Whit move you can command your nearby creeps" ,getSkin())).top();
        table.row();
        table.add(new Label("to go in the direction you click." ,getSkin())).top();
        table.row();
        table.add(new Label("and they will usually obey you" ,getSkin())).top();
        table.row();
        table.add(new Label("but like everything they have a mind of their own." ,getSkin())).top();





        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }


}

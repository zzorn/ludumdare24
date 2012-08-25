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
public class InstructionScreen2 extends Screen2D {

    private static final String SOUND_ENABLED = "soundEnabled";

    private final MainGame game;

    public InstructionScreen2 (MainGame game) {
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
                game.setScreen(new InstructionScreen3(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).expandX().top().right();

        table.row();

        // Toggle sounds enabled checkbox
        table.add(new Label("Tools", getSkin())).expand().top();
        table.row();
        table.add(new Label("Like all gods you have tools you can use!" ,getSkin())).top().padBottom(20);
        table.row();
        table.add(new Label("Your first tool is smite: " ,getSkin())).top();
        table.add(new Image(getAtlas().findRegion("smiteButtonNotPressScaled") ));
        table.row();
        table.add(new Label("Whit smite you can kill both enemy." ,getSkin())).top();
        table.row();
        table.add(new Label("creeps and your own worshippers." ,getSkin())).top();





        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }


}

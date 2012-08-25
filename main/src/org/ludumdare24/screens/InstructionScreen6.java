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
public class InstructionScreen6 extends Screen2D {

    private static final String SOUND_ENABLED = "soundEnabled";

    private final MainGame game;

    public InstructionScreen6 (MainGame game) {
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
                game.setScreen(new InstructionScreen7(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).expandX().top().right();
        table.row();

        table.add(new Label("Tools", getSkin())).expand().top();
        table.row();
        table.add(new Label("Your last tool is feed: " ,getSkin())).top();
        table.add(new Image(getAtlas().findRegion("foodButtonNotPressScaled") ));
        table.row();
        table.add(new Label("Whit it you can toss apples from the sky  " ,getSkin())).top();
        table.row();
        table.add(new Label("to yor faithful followers as a prize  ." ,getSkin())).top();
        table.row();
        table.add(new Label("or if they are dying in hunger" ,getSkin())).top();






        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }


}

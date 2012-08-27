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
public class CreditScreen extends Screen2D {

    private static final String SOUND_ENABLED = "soundEnabled";

    private final MainGame game;

    public CreditScreen(MainGame game) {
        super(game.getAtlas(), game.getUiScale());
        this.game = game;
    }

    @Override
    protected void onCreate() {
        Table table = new Table(getSkin());
        table.setFillParent(true);

        // Toggle sounds enabled checkbox
        table.add(new Label("Troll God was made by:", getSkin())).padBottom(30);
        table.row();
        table.add(new Label("Zzorn & Shiera  ", getSkin())).padBottom(10);
        table.row();
        table.add(new Label("For Ludumdare 24  ", getSkin())).padBottom(10);
        table.row();
        table.add(new Label("August 2012", getSkin())).padBottom(20);
        table.row();


        // Return button
        table.add(createButton("Return", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).colspan(2);

        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }


}

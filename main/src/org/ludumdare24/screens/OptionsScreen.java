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
public class OptionsScreen extends Screen2D {

    private static final String SOUND_ENABLED = "soundEnabled";

    private final MainGame game;

    public OptionsScreen(MainGame game) {
        super(game.getUiScale());
        this.game = game;
    }

    @Override
    protected void onCreate() {
        Table table = new Table(getSkin());
        table.setFillParent(true);

        // Toggle sounds enabled checkbox
        table.add(new Label("Sound Effects", getSkin())).left();
        final CheckBox soundEffectsCheckbox = new CheckBox(getSkin());
        soundEffectsCheckbox.setChecked(game.optionsService.get(SOUND_ENABLED, true));
        soundEffectsCheckbox.setClickListener(new ClickListener() {
            public void click(Actor actor, float x, float y) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.optionsService.set(SOUND_ENABLED, enabled);
                game.soundService.setEnabled(enabled);
                game.soundService.play(Sounds.UI_CLICK);
            }
        });
        table.add(soundEffectsCheckbox).padLeft(16);
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

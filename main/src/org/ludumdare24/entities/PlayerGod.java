package org.ludumdare24.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import org.gameflow.screen.Screen2D;

/**
 * The god that is the players character.
 */
public class PlayerGod extends God {

    private Label manaLabel;

    private Tool currentTool;

    private ParticleEffect cursorEffect=null;


    @Override
    public Actor create(Screen2D screen2D) {
        Table hud = new Table();
        hud.setFillParent(true);

        // Mana gauge
        manaLabel = new Label("Mana", screen2D.getSkin());
        hud.add(manaLabel).expand().top().right();

        hud.row();

        // Action buttons
        Table buttons = new Table();
        TextButton smiteButton = screen2D.createButton("Smite", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                changeTool(Tool.SMITE);
            }
        });

        buttons.add(smiteButton);

        buttons.add(screen2D.createButton("Love", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                changeTool(Tool.LOVE );
            }
        }));
        hud.add(buttons).expand().bottom();

        changeTool(null);


        // Add HUD to screen
        screen2D.getStage().addActor(hud);

        return super.create(screen2D);
    }

    private void changeTool(Tool tool) {
        currentTool = tool;
        cursorEffect = new ParticleEffect();
        cursorEffect.load(Gdx.files.internal("particles/smitSelect.particle"),);
        switch (currentTool ){
            case SMITE:

                break;
            case LOVE:
                break;
            case MOVE:
                break;
            case TOTEM:
                break;
            default :
                break;



        }
    }

    @Override
    public void update(float timeDelta) {
        super.update(timeDelta);

        // Show mana
        manaLabel.setText("Mana: " + (int)getMana());

        // listen for presses
        //Gdx.input.


    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}

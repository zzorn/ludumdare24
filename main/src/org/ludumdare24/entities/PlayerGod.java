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
import org.ludumdare24.MainGame;
import org.ludumdare24.Sounds;
import org.ludumdare24.screens.MainMenuScreen;

/**
 * The god that is the players character.
 */
public class PlayerGod extends God {

    private Label manaLabel;

    private Tool currentTool;

    private ParticleEffect cursorEffect=null;
    private TextureAtlas atlas;

    private final MainGame game;

    public PlayerGod(MainGame game) {
        this.game = game;
    }

    @Override
    public Actor create(TextureAtlas atlas, Screen2D screen2D) {
        this.atlas = atlas;
        cursorEffect = new ParticleEffect();

        Table hud = new Table();
        hud.setFillParent(true);

        // Mana gauge
        manaLabel = new Label("Mana", screen2D.getSkin());
        hud.add(manaLabel).expand().top().right();

        hud.row();

        // Action buttons
        Table buttons = new Table();

        buttons.add(screen2D.createButton("Smite", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                changeTool(Tool.SMITE);
            }
        }));

        buttons.add(screen2D.createButton("Love", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                changeTool(Tool.LOVE );
            }
        }));

        buttons.add(screen2D.createButton("Move", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                changeTool(Tool.MOVE);
            }
        }));

        buttons.add(screen2D.createButton("Rage", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                changeTool(Tool.RAGE);
            }
        }));




        buttons.add(screen2D.createButton("Menu", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).colspan(2);

        hud.add(buttons).expand().bottom();

        changeTool(null);


        // Add HUD to screen
        screen2D.getStage().addActor(hud);

        return super.create(atlas, screen2D);
    }

    private void changeTool(Tool tool) {
        currentTool = tool;
        if (currentTool ==null){

        }
        else{
            /*
            if (cursorEffect != null){
                cursorEffect.dispose();
            }
             */
            switch (currentTool ){

                case SMITE:
                    cursorEffect.load(Gdx.files.internal("particles/smitSelect.particle"), atlas);
                    cursorEffect.start();
                    break;

                case LOVE:
                    cursorEffect.load(Gdx.files.internal("particles/heartSelect.particle"), atlas);
                    cursorEffect.start();
                    break;

                case MOVE:
                    cursorEffect.load(Gdx.files.internal("particles/moveSelect.particle"), atlas);
                    cursorEffect.start();
                    break;
                case TOTEM:
                    break;
                case RAGE:
                    cursorEffect.load(Gdx.files.internal("particles/rageSelect.particle"), atlas);
                    cursorEffect.start();
                    break;
                default :
                    break;



            }
        }

    }

    @Override
    public void update(float timeDelta) {
        super.update(timeDelta);

        // Show mana
        manaLabel.setText("Mana: " + (int)getMana());

        if (cursorEffect != null){
            cursorEffect.update(timeDelta );
            cursorEffect.setPosition(Gdx.input.getX(), (Gdx.graphics.getHeight()-Gdx.input.getY()));
        }

        // listen for presses
        //Gdx.input.


    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        if (cursorEffect != null){
            cursorEffect.draw(spriteBatch);
        }


    }

    @Override
    public void dispose() {
        if (cursorEffect != null){
            cursorEffect.dispose();
        }
    }
}

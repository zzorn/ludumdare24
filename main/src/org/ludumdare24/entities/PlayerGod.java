package org.ludumdare24.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import org.gameflow.screen.Screen2D;
import org.gameflow.utils.MathTools;
import org.ludumdare24.MainGame;
import org.ludumdare24.Sounds;
import org.ludumdare24.entities.creature.Creature;
import org.ludumdare24.screens.GameOverScreen;
import org.ludumdare24.screens.MainMenuScreen;
import org.ludumdare24.screens.WinScreen;
import org.ludumdare24.world.FoodType;

/**
 * The god that is the players character.
 */
public class PlayerGod extends God {

    private Label manaLabel;
    private Label followerLabel;

    private Tool currentTool;
    private Creature selectedCreature=null;
    private Creature observedCreature=null;

    private ParticleEffect cursorEffect=null;
    private ParticleEffect toolEffect=null;
    private TextureAtlas atlas;

    private final MainGame game;
    private Table observationTable;
    private Label observedNameLabel;
    private Label observedHealthLabel;
    private Label observedEnergyLabel;
    private Label observedArmorLabel;
    private Label observedAttackLabel;
    private Label observedBabyLabel;

    public PlayerGod(MainGame game) {
        super("particles/ownGlow.particles", new Color(0.2f, 0.5f, 0.8f, 1f));
        this.game = game;
    }

    @Override
    public void onCreate(TextureAtlas atlas) {
        this.atlas = atlas;
        cursorEffect = new ParticleEffect();
        toolEffect = new ParticleEffect();
        changeTool(null);
    }

    @Override
    public void showOnScreen(Screen2D screen2D) {
        Table hud = new Table();
        hud.setFillParent(true);

        // Mana gauge
        Table manaTable = new Table();
        manaTable.add(new Image(atlas.findRegion("manaStar")) );
        manaLabel = new Label("Mana", screen2D.getSkin());
        manaTable.add(manaLabel);
        hud.add(manaTable).expand().top().left();

        // Follower count
        Table followerTable = new Table();
        followerTable.add(new Image(atlas.findRegion("troll_icon")) );
        followerLabel = new Label("Trolls", screen2D.getSkin());
        followerTable.add(followerLabel);
        hud.add(followerTable).expand().top();

        // Menu button
        hud.add(screen2D.createButton("Menu", new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                game.soundService.play(Sounds.UI_ACCEPT);
            }
        })).expand().top().right();

        hud.row();


        // Labels used to show info about an observed troll
        observedNameLabel = new Label("Igor Trollowitch", screen2D.getSkin());
        observedHealthLabel = new Label("Health: 51", screen2D.getSkin());
        observedEnergyLabel = new Label("Energy: 43", screen2D.getSkin());
        observedArmorLabel = new Label("Armor: 13", screen2D.getSkin());
        observedAttackLabel = new Label("Attack: 54", screen2D.getSkin());
        observedBabyLabel = new Label("Baby due in: 5", screen2D.getSkin());

        // Table to show the observations in
        observationTable = new Table();
        observationTable.add(observedNameLabel).left();
        observationTable.row();
        observationTable.add(observedHealthLabel).left();
        observationTable.row();
        observationTable.add(observedEnergyLabel).left();
        observationTable.row();
        observationTable.add(observedArmorLabel).left();
        observationTable.row();
        observationTable.add(observedAttackLabel).left();
        observationTable.row();
        observationTable.add(observedBabyLabel).left();
        observationTable.row();
        hud.add(observationTable).left().expandY().colspan(3);
        hud.row();

        // Hide initially
        observationTable.visible = false;


        // Action buttons
        Table buttons = new Table();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.setMinCheckCount(0);
        buttons.add(createToolButton(Tool.SMITE, "smiteButton", buttonGroup, screen2D));
        buttons.add(createToolButton(Tool.LOVE, "heartButton", buttonGroup, screen2D));
        buttons.add(createToolButton(Tool.MOVE, "moveButton", buttonGroup, screen2D));
        buttons.add(createToolButton(Tool.RAGE, "rageButton", buttonGroup, screen2D));
        buttons.add(createToolButton(Tool.FEED, "foodButton", buttonGroup, screen2D));
        buttons.add(createToolButton(Tool.WATCH, "watchButton", buttonGroup, screen2D));

        hud.add(buttons).expand().bottom().colspan(3);

        // Add HUD to screen
        screen2D.getStage().addActor(hud);

        // Listen to user input
        screen2D.getInputMultiplexer().addProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer) {
                if (cursorEffect != null) {
                    cursorEffect.setPosition(x, y);
                }
                return false;
            }

            @Override
            public boolean tap(int x, int y, int count) {
                // TODO: Convert to world coordiantes
                float worldX = x;
                float worldY = Gdx.graphics.getHeight() - y;

                useTool(worldX, worldY);
                return true;
            }

            @Override
            public boolean pan(int x, int y, int deltaX, int deltaY) {
                // TODO: Move camera
                System.out.println("PlayerGod.pan");
                System.out.println("deltaX = " + deltaX);
                System.out.println("deltaY = " + deltaY);
                return true;
            }

            @Override
            public boolean zoom(float originalDistance, float currentDistance) {
                // TODO: Zoom camera up to max zoom and down to min zoom
                return true;
            }
        }));
    }

    private ImageButton createToolButton(final Tool tool, String imageName, ButtonGroup buttonGroup, Screen2D screen2D) {
        ImageButton button = screen2D.createImageButton(imageName, new ClickListener() {
            public void click(Actor actor, float x, float y) {
                game.soundService.play(Sounds.UI_CLICK);
                ((Button)actor).setChecked(true);
                changeTool(tool);
            }
        });
        buttonGroup.add(button);

        if (currentTool == tool) {
            button.setChecked(true);
        }

        return button;
    }

    private void changeTool(Tool tool) {
        observedCreature = null;
        currentTool = tool;
        if (currentTool != null) {
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
                case RAGE:
                    cursorEffect.load(Gdx.files.internal("particles/rageSelect.particle"), atlas);
                    cursorEffect.start();
                    break;
                case FEED:
                    cursorEffect.load(Gdx.files.internal("particles/feedSelect.particle"), atlas);
                    cursorEffect.start();
                    break;
                case WATCH:
                    cursorEffect.load(Gdx.files.internal("particles/watchSelect.particle"), atlas);
                    cursorEffect.start();
                    break;
                default :
                    break;

            }
        }

    }


    private void useTool(float x, float y) {


        if (currentTool != null) {


             if (currentTool.getManaCost() > getMana()) {game.soundService.play(Sounds.OOM);}

             if (currentTool.getManaCost() <= getMana() ){
                 selectedCreature = null;
                 Creature closestCreature = game.getGameWorld().getClosestCreature(x, y, null);



                 switch (currentTool ){

                    case SMITE:
                            if (MathTools.distance(closestCreature.getWorldPos().x , closestCreature.getWorldPos().y, x, y )<100){
                                if (closestCreature != null) {
                                    selectedCreature =closestCreature ;
                                    closestCreature.damage(1000);
                                    changeMana(-Tool.SMITE.getManaCost());
                                    toolEffect.load(Gdx.files.internal("particles/smite.particle"), atlas);
                                    toolEffect.start();
                                    int randomSound = ((int) ((Math.random())*2));
                                    if (randomSound==0 ){ game.soundService.play(Sounds.SMITE2);}
                                    else{ game.soundService.play(Sounds.SMITE1);}

                                }
                            }
                        break;

                    case LOVE:
                        closestCreature = game.getGameWorld().getClosestCreatureOfGod(x, y, this);
                        if (MathTools.distance(closestCreature.getWorldPos().x , closestCreature.getWorldPos().y, x, y )<100){
                            if (closestCreature != null) {
                                selectedCreature = closestCreature ;
                                changeMana(-Tool.LOVE.getManaCost());
                                //TODO boost falling in love
                                toolEffect.load(Gdx.files.internal("particles/love.particle"), atlas);
                                toolEffect.start();
                                game.soundService.play(Sounds.LOVE);
                            }
                        }
                        break;

                    case MOVE:
                        changeMana(-Tool.MOVE.getManaCost());

                        toolEffect.load(Gdx.files.internal("particles/move.particle"), atlas);
                        toolEffect.start();
                        toolEffect.setPosition(x,y);

                        placeMoveTarget(x, y);
                        break;

                    case RAGE:
                        if (MathTools.distance(closestCreature.getWorldPos().x , closestCreature.getWorldPos().y, x, y )<100){
                            if (closestCreature != null) {
                                selectedCreature = closestCreature ;
                                changeMana(-Tool.RAGE.getManaCost());
                                //TODO get nearby creatures to attack this creature
                                toolEffect.load(Gdx.files.internal("particles/raged.particle"), atlas);
                                toolEffect.start();
                                game.soundService.play(Sounds.RAGE);
                            }
                        }
                        break;
                    case FEED:
                        changeMana(-Tool.FEED.getManaCost());
                        toolEffect.load(Gdx.files.internal("particles/empty.particle"), atlas);
                        toolEffect.start();
                        game.getGameWorld().spawnFood(FoodType.APPLE, x, y,400  );
                        break;

                    case WATCH:
                        observedCreature = game.getGameWorld().getClosestCreatureOfGod(x, y, this);
                        selectedCreature = observedCreature;
                        changeMana(-Tool.WATCH.getManaCost());
                        toolEffect.load(Gdx.files.internal("particles/watchSelect.particle"), atlas);
                        toolEffect.start();
                        break;
                    default :
                        break;

                    }
                }
             }


        }


    @Override
    public void update(float timeDelta) {
        super.update(timeDelta);

        // Show mana
        manaLabel.setText("Mana " + (int)getMana());

        // Show followers
        int numberOfAllTrolls = game.getGameWorld().getNumberOfCreatures();
        followerLabel.setText("Trolls " + getNumberOfFollowers() + "/" + numberOfAllTrolls);

        // Show observation data if needed
        if (observedCreature != null) {
            observationTable.visible = true;
            observedNameLabel.setText(observedCreature.getName());
            if (observedCreature.isDead()) {
                observedHealthLabel.setText("Died, R.I.P");
                observedEnergyLabel.setText("");
                observedArmorLabel.setText("");
                observedAttackLabel.setText("");
                observedBabyLabel.setText("");
            }
            else {
                observedHealthLabel.setText("Health " + (int) observedCreature.getHealth() + " (" + (int) (100*observedCreature.getHealthStatus()) + "%)");
                observedEnergyLabel.setText("Energy " + (int) observedCreature.getEnergy() + " (" + (int) (100*observedCreature.getEnergyStatus()) + "%)");
                observedArmorLabel.setText("Armor " + (int) observedCreature.getArmor());
                observedAttackLabel.setText("Attack " + (int) observedCreature.getSpikes());
                if (observedCreature.isPregnant()) {
                    observedBabyLabel.setText("Baby due in " + (int) observedCreature.getBabyDevelopmentTimeLeft());
                }
                else {
                    observedBabyLabel.setText("Age " + (int) observedCreature.getAgeSeconds());
                }
            }
        }
        else {
            observationTable.visible = false;
        }

        // Effects
        if (cursorEffect != null){
            cursorEffect.setPosition(Gdx.input.getX(), (Gdx.graphics.getHeight()-Gdx.input.getY()));
            cursorEffect.update(timeDelta);
        }

        if (toolEffect != null) {
            if (selectedCreature != null){
               toolEffect.setPosition(selectedCreature.getWorldPos().x, selectedCreature.getWorldPos().y);
            }
            toolEffect.update(timeDelta );
        }

        // Check win / loose conditions
        if (getNumberOfFollowers() >= numberOfAllTrolls) {
            game.setScreen(new WinScreen(game));
        }
        else if (getNumberOfFollowers() <= 0) {
            game.setScreen(new GameOverScreen(game));
        }
    }

    public void topLayerRender(TextureAtlas atlas, SpriteBatch spriteBatch) {
        if (cursorEffect != null){
            cursorEffect.draw(spriteBatch);
        }
        if (toolEffect != null){
            toolEffect.draw(spriteBatch);
        }


    }

    @Override
    public void onDispose() {
        /* This frees the image atlas, can't do that..
        if (cursorEffect != null){
            cursorEffect.dispose();
        }
        */
    }


}

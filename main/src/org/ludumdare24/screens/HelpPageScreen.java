package org.ludumdare24.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.Cell;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.MainGame;
import org.ludumdare24.Sounds;

/**
 *
 */
public class HelpPageScreen extends Screen2D {

    private final MainGame game;
    private final int helpPageIndex;
    private final String imageName;
    private final Array<String> helpRows;

    public HelpPageScreen(MainGame game, int helpPageIndex, String imageName, String... helpRows) {
        super(game.getAtlas(), game.getUiScale());
        this.game = game;
        this.helpPageIndex = helpPageIndex;
        this.imageName = imageName;
        this.helpRows = new Array<String>(helpRows);
    }


    @Override
    protected void onCreate() {
        Table table = new Table(getSkin());
        table.setFillParent(true);

        // Prew button
        if (helpPageIndex>1) {
            table.add(createButton("Previous", new ClickListener() {
                public void click(Actor actor, float x, float y) {
                    game.setScreen(game.createHelpPage(helpPageIndex - 1));
                    game.soundService.play(Sounds.UI_CLICK);
                }
            })).expandX().top().left();
        }
        else {
            table.add(createButton("Return", new ClickListener() {
                public void click(Actor actor, float x, float y) {
                    game.setScreen(new MainMenuScreen(game));
                    game.soundService.play(Sounds.UI_ACCEPT);
                }
            })).expandX().top().left();
        }


        // Next button
        if (helpPageIndex < game.getHelpPageCount()) {
            table.add(createButton("Next", new ClickListener() {
                public void click(Actor actor, float x, float y) {
                    game.setScreen(game.createHelpPage(helpPageIndex + 1));
                    game.soundService.play(Sounds.UI_CLICK);
                }
            })).expandX().top().right();
        }
        else {
            table.add(createButton("Return", new ClickListener() {
                public void click(Actor actor, float x, float y) {
                    game.setScreen(new MainMenuScreen(game));
                    game.soundService.play(Sounds.UI_ACCEPT);
                }
            })).expandX().top().right();
        }

        table.row();

        // Image
        if (imageName != null) {
            table.add(new Image(getAtlas().findRegion(imageName))).colspan(2);
            table.row();
        }

        // Add help rows
        Cell lastHelpCell = null;
        for (String helpRow : helpRows) {
            lastHelpCell = table.add(new Label(helpRow, getSkin())).top().colspan(2);
            table.row();
        }

        // Fill up empty sapce at bottom
        if (lastHelpCell != null) {
            lastHelpCell.expand();
        }


        // Add to screen
        getStage().addActor(table);
    }

    @Override
    protected void onDispose() {
    }
}

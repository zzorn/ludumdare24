package org.gameflow.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import org.gameflow.Service;
import org.gameflow.entity.Entity;

/**
 * A screen implementation with a sprite stage and other utilities.
 */
public abstract class Screen2D extends ScreenBase {

    private double screenSizeScale = 1.0;

    private BitmapFont font;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;

    private final Array<Entity> entities = new Array<Entity>();
    private ObjectMap<Entity, Actor> entityActors = new ObjectMap<Entity, Actor>();

    private final TextureAtlas atlas;

    public Screen2D(TextureAtlas atlas) {
        super(null);
        this.atlas = atlas;
    }

    public Screen2D(TextureAtlas atlas, double screenSizeScale) {
        super(null);
        this.screenSizeScale = screenSizeScale;
        this.atlas = atlas;
    }

    public Screen2D(TextureAtlas atlas, String id) {
        super(id);
        this.atlas = atlas;
    }

    public double getScreenSizeScale() {
        return screenSizeScale;
    }

    public void setScreenSizeScale(double screenSizeScale) {
        this.screenSizeScale = screenSizeScale;
        if (stage != null) {
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public ObjectMap<Entity, Actor> getEntityActors() {
        return entityActors;
    }

    public void setEntityActors(ObjectMap<Entity, Actor> entityActors) {
        this.entityActors = entityActors;
    }

    public BitmapFont getFont() {
        return font;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Stage getStage() {
        return stage;
    }

    public final void create() {
        font = new BitmapFont();
        batch = new SpriteBatch();
        stage = new Stage(0, 0, true);

        // Create actors for entities that were added earlier
        for (Entity entity : entities) {
            createEntity(entity);
        }

        onCreate();
    }

    /**
     * Adds an entity to the screen.
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
        createEntity(entity);
    }

    /**
     * Removes an entity from the screen.
     */
    public void removeEntity(Entity entity) {
        if (entities.contains(entity,  true)) {
            entities.removeValue(entity, true);
            Actor actor = entityActors.get(entity);
            if (actor != null) {
                entityActors.remove(entity);

                if (isSceneCreated()) {
                    stage.removeActor(actor);
                    entity.dispose();
                }
            }
        }
    }

    protected abstract void onCreate();

    @Override
    public void update(float deltaSeconds) {
        // Update the actors
        stage.act(deltaSeconds);

        onUpdate(deltaSeconds);

        // Update entities
        for (Entity entity : entities) {
            entity.update(deltaSeconds);
        }
    }

    protected void onUpdate(float deltaSeconds) {}

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage actors
        stage.draw();

        batch.begin();

        // Render entities
        for (Entity entity : entities) {
            entity.render(atlas, batch);
        }

        // Render screen
        onRender();

        batch.end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
    }

    protected void onRender() {}

    @Override
    public void resize(int width, int height) {
        // Resize the stage
        stage.setViewport(
                (int)(width / screenSizeScale),
                (int)(height / screenSizeScale),
                true);

        onResize(width, height);
    }

    protected void onResize(int width, int height) {}

    public final void dispose() {
        onDispose();

        // Dispose entities
        for (Entity entity : entities) {
            entity.dispose();
        }

        font.dispose();
        batch.dispose();
        stage.dispose();
        if (skin != null) {
            skin.dispose();
        }
    }

    protected abstract void onDispose();

    public Skin getSkin() {
        if( skin == null ) {
            skin = new Skin( Gdx.files.internal( "uiskin.json" ), Gdx.files.internal( "uiskin.png" ) );
        }
        return skin;
    }



    public TextButton createButton(String text, ClickListener listener) {
        TextButton button = new TextButton(getSkin());
        button.setText(text);
        button.setClickListener(listener);
        return button;
    }


    private void createEntity(Entity entity) {
        if (isSceneCreated()) {
            Actor actor = entity.create(this);
            if (actor != null) {
                entityActors.put(entity, actor);
                stage.addActor(actor);
            }
        }
    }

    private boolean isSceneCreated() {
        return stage != null;
    }


}

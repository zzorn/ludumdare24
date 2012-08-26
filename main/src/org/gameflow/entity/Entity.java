package org.gameflow.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gameflow.screen.Screen2D;

/**
 *
 */
public abstract class Entity {

    private boolean created = false;
    private boolean disposed = false;

    public boolean isCreated() {
        return created;
    }

    public boolean isDisposed() {
        return disposed;
    }

    public final void create(TextureAtlas atlas) {
        //if (disposed) throw new IllegalStateException("Can not create an entity after disposing it.");
        if (!created) {
            created = true;
            onCreate(atlas);
        }
    }

    public void showOnScreen(Screen2D screen2D) {};

    protected abstract void onCreate(TextureAtlas atlas);

    public void update(float timeDelta) {}

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {}

    public float getDrawOrder() {
        return 0;
    }

    public final void dispose() {
        if (created && !disposed) {
            disposed = true;
            onDispose();
        }
    }

    public abstract void onDispose();

    public void topLayerRender(TextureAtlas atlas, SpriteBatch batch) {}
}

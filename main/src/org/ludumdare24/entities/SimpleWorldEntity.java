package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.gameflow.utils.ImageRef;

/**
 * World entity with just a simple image.
 */
public class SimpleWorldEntity extends WorldEntity {

    private final ImageRef imageRef;


    public SimpleWorldEntity(ImageRef imageRef) {
        this.imageRef = imageRef;
    }

    public ImageRef getImageRef() {
        return imageRef;
    }

    @Override
    protected void onCreate(TextureAtlas atlas) {

    }

    @Override
    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
        float x = getWorldPos().x;
        float y = getWorldPos().y;
        imageRef.render(x, y, atlas, spriteBatch);
    }

    @Override
    public void onDispose() {
    }
}

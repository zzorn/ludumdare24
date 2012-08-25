package org.gameflow.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;


/**
 *
 */
public class ImageEntity extends Image implements Entity {

    public ImageEntity() {
    }

    public ImageEntity(TextureRegion region) {
        super(region);
    }

    public ImageEntity(TextureRegion region, Scaling scaling) {
        super(region, scaling);
    }

    public ImageEntity(TextureRegion region, Scaling scaling, int align) {
        super(region, scaling, align);
    }

    public ImageEntity(TextureRegion region, Scaling scaling, int align, String name) {
        super(region, scaling, align, name);
    }

    public Actor create() {
        return this;
    }

    public void update(float timeDelta) {}

    public void render(SpriteBatch spriteBatch) {}

    public void dispose() {}

}

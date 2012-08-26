package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.gameflow.entity.Entity;
import org.gameflow.utils.ImageRef;

import java.util.Random;

/**
 *
 */
public class AppleTree extends SimpleWorldEntity {

    public AppleTree() {
        this(new Random());
    }

    public AppleTree(Random random) {
        super(new ImageRef("appleTree", random.nextInt(4) + 1));
    }
}

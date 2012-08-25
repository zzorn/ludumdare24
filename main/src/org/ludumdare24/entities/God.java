package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gameflow.entity.Entity;
import org.gameflow.screen.Screen2D;

/**
 *
 */
public class God implements Entity {

    private double maxMana = 100;
    private double mana = 50;
    private double manaRegenerationPerSecond = 2;

    public double getMaxMana() {
        return maxMana;
    }

    public double getMana() {
        return mana;
    }

    public void changeMana(double change) {
        mana += change;
    }

    protected void updateMana(float deltaTime) {
        mana += manaRegenerationPerSecond * deltaTime;
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public Actor create(Screen2D screen2D) {
        return null;
    }

    public void update(float timeDelta) {
        updateMana(timeDelta);
    }

    public void render(SpriteBatch spriteBatch) {
    }

    public void dispose() {
    }
}

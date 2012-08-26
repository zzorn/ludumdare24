package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gameflow.entity.Entity;
import org.gameflow.screen.Screen2D;

/**
 *
 */
public class God extends Entity {

    private double maxMana = 100;
    private double mana = 50;
    private double manaRegenerationPerSecond = 4;
    private final String GlowEffectName;

    private final Color color;

    public God(String glowEffectName, Color color) {
        GlowEffectName = glowEffectName;
        this.color = color;
    }

    public String getGlowEffectName() {
        return GlowEffectName;
    }

    public Color getColor() {
        return color;
    }

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

    public void onCreate(TextureAtlas atlas) {
    }

    public void update(float timeDelta) {
        updateMana(timeDelta);
    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
    }

    public void onDispose() {
    }
}

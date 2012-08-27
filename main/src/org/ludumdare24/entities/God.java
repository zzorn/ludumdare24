package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.gameflow.entity.Entity;
import org.gameflow.screen.Screen2D;
import org.ludumdare24.entities.creature.Creature;

/**
 *
 */
public class God extends Entity {

    private static final double MANA_REGEN_PER_FOLLOWER = 0.25;

    private double maxMana = 100;
    private double mana = 100;
    private double manaRegenerationPerSecond = 5;
    private Vector2 moveTarget = new Vector2();
    private double moveTargetPullTimeLeft = 0;
    private double moveTargetPullTimeSeconds = 10;

    private int numberOfFollowers = 0;

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
        mana += (manaRegenerationPerSecond + MANA_REGEN_PER_FOLLOWER * numberOfFollowers) * deltaTime;
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public void onCreate(TextureAtlas atlas) {
    }

    public void update(float timeDelta) {
        updateMana(timeDelta);

        moveTargetPullTimeLeft -= timeDelta;
        if (moveTargetPullTimeLeft < 0) moveTargetPullTimeLeft = 0;
    }

    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
    }

    public void onDispose() {
    }

    public Vector2 getMoveTarget() {
        return moveTarget;
    }

    /**
     * @return pull of move target, 0 no pull, 1 full pull.
     */
    public double getMoveTargetPull() {
        return moveTargetPullTimeLeft / moveTargetPullTimeSeconds;
    }

    public void placeMoveTarget(float x, float y) {
        moveTarget.set(x, y);
        moveTargetPullTimeLeft = moveTargetPullTimeSeconds;
    }

    public void addFollower(Creature creature) {
        numberOfFollowers++;
    }

    public void removeFollower(Creature creature) {
        numberOfFollowers--;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public boolean isPlayerGod() {
        return false;
    }
}

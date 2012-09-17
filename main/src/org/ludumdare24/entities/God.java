package org.ludumdare24.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.entity.Entity;
import org.ludumdare24.entities.creature.Creature;

/**
 *
 */
public class God extends Entity {

    private static final double MANA_REGEN_PER_FOLLOWER = 0.1;

    private double maxMana = 100;
    private double mana = 100;
    private double manaRegenerationPerSecond = 1;
    private Vector2 moveTarget = new Vector2();
    private double moveTargetPullTimeLeft = 0;
    private double moveTargetPullTimeSeconds = 10;

    private final String GlowEffectName;

    private final Color color;

    private Array<Creature> worshippers = new Array<Creature>();

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

    public void changeMaxMana(double delta) {
        maxMana += delta;
    }

    public void changeManaRegenerationPerSecond(double delta) {
        manaRegenerationPerSecond += delta;
    }

    public double getMana() {
        return mana;
    }

    public void changeMana(double change) {
        mana += change;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    protected void updateMana(float deltaTime) {
        mana += (manaRegenerationPerSecond + MANA_REGEN_PER_FOLLOWER * getNumberOfWorshippers()) * deltaTime;
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

    public void addWorshipper(Creature creature) {
        worshippers.add(creature);
    }

    public void removeWorshipper(Creature creature) {
        worshippers.removeValue(creature, true);
    }

    public int getNumberOfWorshippers() {
        return worshippers.size;
    }

    public boolean isPlayerGod() {
        return false;
    }

    public Array<Creature> getWorshippers() {
        return worshippers;
    }
}

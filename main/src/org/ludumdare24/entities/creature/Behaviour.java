package org.ludumdare24.entities.creature;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Base class for creature behaviours.
 */
public class Behaviour {

    private final String name;
    protected final Creature creature;
    protected final double reActivationTime;

    public Behaviour(String name, Creature creature, double reActivationTime) {
        this.name = name;
        this.creature = creature;
        this.reActivationTime = reActivationTime;
    }

    /**
     * Called after creature is created, once.
     */
    public void setup() {
    }

    public String getName() {
        return name;
    }

    /**
     * @return importance to switch to this action, now.
     */
    public double getImportance(double timeSinceLastAsked) {
        return 0;
    }

    public double getReActivationTime() {
        return reActivationTime;
    }

    /**
     * Called when this behaviour gets activated.
     */
    public void onActivated(double actiavationImportance) {

    }

    /**
     * Called while this behaviour is active every timestep.
     */
    public void update(double timeDelta) {
    }

    /**
     * Called while this behaviour is active every render pass.
     */
    public void render(TextureAtlas atlas, SpriteBatch spriteBatch) {
    }

    /**
     * Called when this behaviour gets deactivated.
     */
    public void onDeactivated() {

    }

}

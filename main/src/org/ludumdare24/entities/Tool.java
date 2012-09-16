package org.ludumdare24.entities;

/**
 * Represents the selected tool in the game.
 */
public enum Tool {

    SMITE(30),
    LOVE(15),
    MOVE(7),
    RAGE(20),
    FEED(12),
    WATCH(0);
    private final double manaCost;

    private Tool(double manaCost) {
        this.manaCost = manaCost;

    }

    public double getManaCost() {
        return manaCost;
    }
}


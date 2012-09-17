package org.ludumdare24.entities;

/**
 * Represents the selected tool in the game.
 */
public enum Tool {

    SMITE(30, "Smite", "Select any troll to smite with lighting"),
    LOVE(15, "Love", "Select one of your trolls to make it fall madly in love"),
    MOVE(7, "Move", "Tap a location to tell your trolls to move there"),
    RAGE(10, "Rage", "Mark an enemy troll for your trolls to attack"),
    FEED(12, "Feed", "Drop apples to feed your trolls"),
    WATCH(0, "Watch", "Watch one of your trolls");

    private final int manaCost;
    private final String name;
    private final String helpText;

    private Tool(int manaCost, String name, String helpText) {
        this.manaCost = manaCost;
        this.name = name;

        this.helpText = helpText;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getName() {
        return name;
    }

    public String getHelpText() {
        return helpText;
    }
}


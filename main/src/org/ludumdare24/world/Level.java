package org.ludumdare24.world;

/**
 * Holds information about a level
 */
public class Level {
    private final String name;
    private final int treeGroups;
    private final int treeGroupTreeCount;
    private final double treeGroupSize;
    private final String terrainTexture;
    private final int terrainTextureCount;
    private final double terrainScaleX;
    private final double terrainScaleY;
    private final int tribeCount;
    private final int tribeHeadCount;
    private final double enemyThoughness;
    private final double enemyArmor;
    private final double enemyAttack;
    private final double enemySpeed;
    private final double enemySize;

    public Level(String name, int treeGroups, int treeGroupTreeCount, double treeGroupSize, String terrainTexture, int terrainTextureCount, double terrainScaleX, double terrainScaleY, int tribeCount, int tribeHeadCount, double enemyThoughness, double enemyArmor, double enemyAttack, double enemySpeed, double enemySize) {
        this.name = name;
        this.treeGroups = treeGroups;
        this.treeGroupTreeCount = treeGroupTreeCount;
        this.treeGroupSize = treeGroupSize;
        this.terrainTexture = terrainTexture;
        this.terrainTextureCount = terrainTextureCount;
        this.terrainScaleX = terrainScaleX;
        this.terrainScaleY = terrainScaleY;
        this.tribeCount = tribeCount;
        this.tribeHeadCount = tribeHeadCount;
        this.enemyThoughness = enemyThoughness;
        this.enemyArmor = enemyArmor;
        this.enemyAttack = enemyAttack;
        this.enemySpeed = enemySpeed;
        this.enemySize = enemySize;
    }

    public String getName() {
        return name;
    }

    public int getTreeGroups() {
        return treeGroups;
    }

    public int getTreeGroupTreeCount() {
        return treeGroupTreeCount;
    }

    public double getTreeGroupSize() {
        return treeGroupSize;
    }

    public String getTerrainTexture() {
        return terrainTexture;
    }

    public int getTribeCount() {
        return tribeCount;
    }

    public int getTribeHeadCount() {
        return tribeHeadCount;
    }

    public int getTerrainTextureCount() {
        return terrainTextureCount;
    }

    public double getEnemyThoughness() {
        return enemyThoughness;
    }

    public double getEnemyArmor() {
        return enemyArmor * enemyThoughness;
    }

    public double getEnemyAttack() {
        return enemyAttack * enemyThoughness;
    }

    public double getEnemySpeed() {
        return enemySpeed * enemyThoughness;
    }

    public double getEnemySize() {
        return enemySize * enemyThoughness;
    }

    public float getTerrainScaleX() {
        return (float) terrainScaleX;
    }

    public float getTerrainScaleY() {
        return (float) terrainScaleY;
    }
}

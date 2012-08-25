package org.ludumdare24.entities.creature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class CreaturePart {

    private BodyPartShape shape;
    private double fatness;
    private double length;
    private double hair;
    private double armor;
    private Color baseColor;
    private Color hairColor;
    private Color armorColor;
    private Vector2 relativePos;
    private float relativeAngle;

    private TextureAtlas.AtlasRegion baseImage;
    private TextureAtlas.AtlasRegion hairImage;
    private TextureAtlas.AtlasRegion armorImage;
    private Vector2 tempPos = new Vector2();
    private int scale;

    public CreaturePart(BodyPartShape shape, double fatness, double length, double hair, double armor, Color baseColor, Color hairColor, Color armorColor, Vector2 relativePos, float relativeAngle) {
        this.shape = shape;
        this.fatness = fatness;
        this.length = length;
        this.hair = hair;
        this.armor = armor;
        this.baseColor = baseColor;
        this.hairColor = hairColor;
        this.armorColor = armorColor;
        this.relativePos = relativePos;
        this.relativeAngle = relativeAngle;
        scale = 2;
    }

    public void draw(TextureAtlas atlas, SpriteBatch spriteBatch, Vector2 pos, float angle) {
        if (baseImage  == null) baseImage  = getImage(atlas, shape.getImageName());
        if (hairImage  == null) hairImage  = getImage(atlas, shape.getHairImageName(hair));
        if (armorImage == null) armorImage = getImage(atlas, shape.getArmorImageName(armor));

        tempPos.set(pos);
        tempPos.add(relativePos);
        double w = baseImage.getRegionWidth() * fatness;
        double h = baseImage.getRegionHeight() * length;
        float currentAngle = relativeAngle + angle;

        drawLayer(spriteBatch, baseImage, tempPos, w, h, currentAngle, baseColor);
        drawLayer(spriteBatch, armorImage, tempPos, w, h, currentAngle, armorColor);
        drawLayer(spriteBatch, hairImage, tempPos, w, h, currentAngle, hairColor);
    }

    private void drawLayer(SpriteBatch spriteBatch, TextureAtlas.AtlasRegion image, Vector2 pos, double w, double h, float angle, Color color) {
        if (image != null) {
            spriteBatch.setColor(color);
            spriteBatch.draw(image, pos.x, pos.y, 0, 0, (float) w, (float) h, scale, scale, angle);
            spriteBatch.setColor(Color.WHITE);
        }
    }

    private TextureAtlas.AtlasRegion getImage(TextureAtlas atlas, String imageName) {
        if (imageName == null) return null;
        else return atlas.findRegion(imageName);
    }

}

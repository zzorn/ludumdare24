package org.ludumdare24.entities.creature;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.gameflow.utils.MathUtils;

/**
 *
 */
public class CreaturePart {

    private BodyPartShape shape;
    private float fatness;
    private float length;
    private float hair;
    private float armor;
    private float spikes;
    private Color baseColor;
    private Color hairColor;
    private Color armorColor;
    private Color spikesColor;

    //private Matrix4 transformation = new Matrix4();

    protected Vector2 basePos = new Vector2(0,0);
    protected float baseAngle = 0f;
    protected float angle = 0f;

    private float originX = 0.5f;
    private float originY = 0.5f;

    private Array<CreaturePart> attachedParts = new Array<CreaturePart>();

    private TextureAtlas.AtlasRegion baseImage;
    private TextureAtlas.AtlasRegion hairImage;
    private TextureAtlas.AtlasRegion armorImage;
    private TextureAtlas.AtlasRegion spikesImage;
    private Vector2 tempPos = new Vector2();
    private float scale;

    private final float visibleW;
    private final float visibleH;
    private final boolean mirror;
    private final double basicShape;
    private static final float FIXED_PART_DISTANCE_SCALE = 0.55f;

    public CreaturePart(BodyPartShape shape, boolean mirror, double fatness, double length, double hair, double armor, double spikes, Color baseColor, Color hairColor, Color armorColor, Color spikesColor, double basicShape, float scale) {
        this.shape = shape;
        this.mirror = mirror;
        this.spikesColor = spikesColor;
        this.basicShape = basicShape;
        this.fatness = (float) fatness;
        this.length = (float) length;
        this.hair = (float) hair;
        this.armor = (float) armor;
        this.spikes = (float) spikes;
        this.baseColor = baseColor;
        this.hairColor = hairColor;
        this.armorColor = armorColor;
        this.scale = scale;

        visibleW = (float) (shape.getVisibleWidth() * fatness * this.scale);
        visibleH = (float) (shape.getVisibleHeight() * length * this.scale);

        //transformation.idt();
    }

    public float getVisibleW() {
        return visibleW;
    }

    public float getVisibleH() {
        return visibleH;
    }

    public void setRelativeOrigin(float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
    }

    public void setOffset(double xOffs, double yOffs) {
        basePos.set((float) xOffs, (float) yOffs);
    }

    public void setAngle(double angle) {
        //transformation.setToRotation(0,0,1, angle + baseAngle);
        this.angle = (float) angle;
    }

    public void addPart(CreaturePart part, float direction, float angle) {
        // Add the part to the edge of this part at the specified direction
        float x = visibleW * 0.5f + scale * (float)(Math.cos(direction + baseAngle + MathUtils.TauFloat/4) * visibleW * 0.5 * FIXED_PART_DISTANCE_SCALE);
        float y = visibleH * 0.5f + scale * (float)(Math.sin(direction + baseAngle + MathUtils.TauFloat/4) * visibleH * 0.5 * FIXED_PART_DISTANCE_SCALE);
        part.basePos.set(x, y);
        part.basePos.rotate(baseAngle);
        part.basePos.add(basePos);
        part.baseAngle = direction;
        part.setAngle(angle);

        attachedParts.add(part);
    }

    public void draw(TextureAtlas atlas, SpriteBatch spriteBatch, Vector2 pos, float angle) {

        float currentAngle = baseAngle + this.angle + angle;

        // Draw all attached parts
        for (CreaturePart attachedPart : attachedParts) {
            attachedPart.draw(atlas, spriteBatch, pos, currentAngle);
        }

        // Draw the part itself
        if (baseImage  == null) baseImage  = getImage(atlas, shape.getBasicImageName(basicShape));
        if (hairImage  == null) hairImage  = getImage(atlas, shape.getHairImageName(hair));
        if (armorImage == null) armorImage = getImage(atlas, shape.getArmorImageName(armor));
        if (spikesImage == null) spikesImage = getImage(atlas, shape.getSpikeImageName(spikes));

        tempPos.set(basePos);
        tempPos.add(pos);

        drawLayer(spriteBatch, baseImage, tempPos, currentAngle, baseColor);
        drawLayer(spriteBatch, armorImage, tempPos, currentAngle, armorColor);
        drawLayer(spriteBatch, hairImage, tempPos, currentAngle, hairColor);
        drawLayer(spriteBatch, spikesImage, tempPos, currentAngle, spikesColor);

    }

    private void drawLayer(SpriteBatch spriteBatch, TextureAtlas.AtlasRegion image, Vector2 pos, float angle, Color color) {
        if (image != null) {
            spriteBatch.setColor(color);
            float w = image.getRegionWidth() * fatness;
            float xOffs = 0;
            if (mirror) {
                xOffs = w ;
                w *= -1;
            }
            float h = image.getRegionHeight() * length;
            float x = pos.x + xOffs ;
            float y = pos.y;
            spriteBatch.draw(image, x, y, w*originX, h*originY, w, h, scale, scale, MathUtils.toDegrees(angle));
            spriteBatch.setColor(Color.WHITE);
        }
    }

    private TextureAtlas.AtlasRegion getImage(TextureAtlas atlas, String imageName) {
        if (imageName == null) return null;
        else return atlas.findRegion(imageName);
    }

}

package org.gameflow.utils;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

/**
 *
 */
public class ColorUtils {

    public static Color randomColor(Random random) {
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
    }
}

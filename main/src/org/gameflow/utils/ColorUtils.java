package org.gameflow.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

/**
 *
 */
public class ColorUtils {

    public static Color randomColor(Random random) {
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f);
    }

    public static double hue(Color color) {
        double hue;
        double r = color.r;
        double g = color.g;
        double b = color.b;

        double cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;

        double cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        double saturation;
        if (cmax == 0) {
            saturation = 0;
        } else {
            saturation = (cmax - cmin) / cmax;
        }

        if (saturation == 0) {
            hue = 0;
        }
        else {
            double redc   = (cmax - r) / (cmax - cmin);
            double greenc = (cmax - g) / (cmax - cmin);
            double bluec  = (cmax - b) / (cmax - cmin);

            if (r == cmax) hue = bluec - greenc;
            else if (g == cmax) hue = 2.0 + redc - bluec;
            else hue = 4.0 + greenc - redc;
            hue = hue / 6.0f;
            if (hue < 0) hue = hue + 1.0;
        }

        return hue;
    }

    public static double sat(Color color) {
        double r = color.r;
        double g = color.g;
        double b = color.b;

        double cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;

        double cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        double saturation;
        if (cmax == 0) {
            saturation = 0;
        } else {
            saturation = (cmax - cmin) / cmax;
        }

        return saturation;
    }

    public static double lum(Color color) {
        double r = color.r;
        double g = color.g;
        double b = color.b;

        double cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;

        return cmax;
    }

    public static Color hslColor(double hue, double sat, double lum) {
        return hslColor(hue, sat, lum, 1, null);
    }

    public static Color hslColor(double hue, double sat, double lum, double alpha) {
        return hslColor(hue, sat, lum, alpha, null);
    }

    public static Color hslColor(double hue, double sat, double lum, double alpha, Color color) {
        hue   = MathTools.rollToZeroToOne(hue);
        sat   = MathTools.clampToZeroToOne(sat);
        lum   = MathTools.clampToZeroToOne(lum);
        alpha = MathTools.clampToZeroToOne(alpha);

        double r = 0, g = 0, b = 0;
        if (sat == 0) {
            r = g = b = lum;
        } else {
            double h = (hue - Math.floor(hue)) * 6.0f;
            double f = h - java.lang.Math.floor(h);
            double p = lum * (1.0f - sat);
            double q = lum * (1.0f - sat * f);
            double t = lum * (1.0f - (sat * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = lum;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = lum;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = lum;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = lum;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = lum;
                    break;
                case 5:
                    r = lum;
                    g = p;
                    b = q;
                    break;
            }
        }

        if (color == null) color = new Color();
        color.set((float) r, (float) g, (float) b, (float) alpha);
        return color;
    }
}

package org.gameflow.utils;

/**
 *
 */
public class MathTools {

    public static final double Tau = Math.PI * 2;
    public static final float TauFloat = (float) Tau;

    public static double mix(double t, double start, double end) {
        return start * (1.0 - t) + end * t;
    }

    public static double relPos(double t, double start, double end) {
        if (end == start) return 0.5;
        else return (t - start) / (end - start);
    }

    public static double map(double t, double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        double r = relPos(t, sourceStart, sourceEnd);
        return mix(r, targetStart, targetEnd);
    }


    public static float toDegrees(double angle) {
        return (float)(360.0 * angle / Tau);
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) return min;
        else if (value > max) return max;
        else return value;
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;
        return (float) Math.sqrt(x*x + y*y);
    }

    public static float distanceSquared(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;
        return x*x + y*y;
    }
}

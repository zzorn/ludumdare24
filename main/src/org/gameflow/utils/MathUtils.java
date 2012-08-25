package org.gameflow.utils;

/**
 *
 */
public class MathUtils {

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


    public static float toDegrees(float angle) {
        return 360f * angle / TauFloat;
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) return min;
        else if (value > max) return max;
        else return value;
    }
}

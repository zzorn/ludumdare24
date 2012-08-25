package org.gameflow.utils;

/**
 *
 */
public class MathUtils {

    public static double mix(double t, double start, double end) {
        return start * (1.0 - t) + end * t;
    }


}

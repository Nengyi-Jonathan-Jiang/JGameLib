package jGameLib.util.math;

public class MathUtil {
    public static double smoothStep(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * Returns x modulo m. This is different from the
     * % operator in the way it handles negative
     * numbers
     */
    public static double mod(double x, double m) {
        return ((x % m) + m) % m;
    }
}

package jGameLib.util.math;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

import static jGameLib.util.math.MathUtil.smoothStep;

/**
 * A class representing a pair of floating point coordinates
 * or size. Vectors can be added to each other, scaled, and
 * multiplied component-wise. Vectors are immutable, which
 * means that they cannot be changed once constructed.
 */
public final class Vec2 {
    public final double x, y;
    /**
     * The zero vector [0, 0]
     */
    public static final Vec2 zero = new Vec2(0, 0);

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs the vector [v, v]
     */
    public Vec2(double v) {
        this(v, v);
    }

    /**
     * Creates a {@link Vec2} from a {@link Dimension}
     */
    public Vec2(Dimension d) {
        this(d.width, d.height);
    }

    /**
     * Creates a {@link Vec2} from a {@link Vec2i}. <br>
     * Decimal places are truncated.
     */
    public Vec2(Vec2i v) {
        this(v.x, v.y);
    }

    /**
     * Adds two vectors. <br>
     * add([x1, y1], [x2, y2]) = [x1 + x2, y1 + y2]
     */
    public static Vec2 add(Vec2 a, Vec2 b) {
        return new Vec2(a.x + b.x, a.y + b.y);
    }

    /**
     * Subtracts two vectors. <br>
     * sub([x1, y1], [x2, y2]) = [x1 - x2, y1 - y2]
     */
    public static Vec2 sub(Vec2 a, Vec2 b) {
        return new Vec2(a.x - b.x, a.y - b.y);
    }

    /**
     * Scales a vector by a constant c. <br>
     * scale([x, y], c) = [c * x, c * y]
     */
    public static Vec2 scale(Vec2 p, double c) {
        return new Vec2(p.x * c, p.y * c);
    }

    /**
     * Scales each component of a vector by sx and sy, respectively. <br>
     * scale([x, y], sx, sy) = [sx * x, sy * y]. <br>
     * This is a shortcut for component-wise vector multiplication. <br>
     */
    public static Vec2 scale(Vec2 p, double sx, double sy) {
        return new Vec2(p.x * sx, p.y * sy);
    }

    /**
     * @return The length of the vector sqrt(x * x + y * y)
     */
    public static double magnitude(Vec2 p) {
        return Math.sqrt(p.x * p.x + p.y * p.y);
    }

    /**
     * @return The distance between two vectors, equal to
     * magnitude(sub(a, b))
     */
    public static double distance(Vec2 a, Vec2 b) {
        return sub(a, b).magnitude();
    }

    /**
     * @return The manhattan distance between two vectors. <br>
     * manhattanDistance([x1, y1], [x2, y2]) = abs(x1 - x2) + abs(y1 - y2)
     */
    public static double manhattanDistance(Vec2 a, Vec2 b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * @return The x-component of the vector
     */
    public double x() {
        return x;
    }

    /**
     * @return The y-component of the vector
     */
    public double y() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Vec2) obj;
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }

    /**
     * Shorthand for {@link Vec2#add(Vec2, Vec2)}
     */
    public Vec2 plus(Vec2 other) {
        return add(this, other);
    }

    /**
     * Shorthand for {@link Vec2#add(Vec2, Vec2)}
     */
    public Vec2 plus(double x, double y) {
        return plus(new Vec2(x, y));
    }

    /**
     * Shorthand for {@link Vec2#sub(Vec2, Vec2)}
     */
    public Vec2 minus(Vec2 other) {
        return sub(this, other);
    }

    /**
     * Shorthand for {@link Vec2#scale(Vec2, double, double)}
     */
    public Vec2 minus(double x, double y) {
        return minus(new Vec2(x, y));
    }

    /**
     * Shorthand for {@link Vec2#scale(Vec2, double)}
     */
    public Vec2 times(double c) {
        return scale(this, c);
    }

    /**
     * Shorthand for {@link Vec2#scale(Vec2, double, double)}
     */
    public Vec2 times(double sx, double sy) {
        return scale(this, sx, sy);
    }

    /**
     * Shorthand for {@link Vec2#scale(Vec2, double, double)}
     */
    public Vec2 times(Vec2 scale) {
        return scale(this, scale.x, scale.y);
    }

    /**
     * Equivalent to {@link Vec2#magnitude(Vec2)}
     */
    public double magnitude() {
        return magnitude(this);
    }

    /**
     * Shorthand for {@link Vec2#distance(Vec2, Vec2)}
     */
    public double distanceTo(Vec2 other) {
        return distance(this, other);
    }

    /**
     * Shorthand for {@link Vec2#manhattanDistance(Vec2, Vec2)}
     */
    public double manhattanDistanceTo(Vec2 other) {
        return manhattanDistance(this, other);
    }

    /**
     * Applies the given function to both components
     * of the vector and returns the resulting vector
     */
    public Vec2 transform(DoubleUnaryOperator func) {
        return new Vec2(func.applyAsDouble(x), func.applyAsDouble(y));
    }

    /**
     * Applies an AffineTransform to a vector
     */
    public static Vec2 transform(Vec2i v, AffineTransform transform) {
        Point2D.Double dst = new Point2D.Double();
        transform.transform(new Point2D.Double(v.x, v.y), dst);
        return new Vec2(dst.x, dst.y);
    }

    /**
     * Takes the average of the vectors
     */
    public static Vec2 average(Vec2... v) {
        return Arrays.stream(v).reduce(Vec2.zero, Vec2::add).times(1. / v.length);
    }

    /**
     * Equivalent to {@link Vec2#average(Vec2..., double...)}({v1, v2}, {w1, 1 - w1}). When t is zero, the output is v2, and when t is 1, the output is v1
     */
    public static Vec2 average(Vec2 v1, Vec2 v2, double w1) {
        return average(new Vec2[]{v1, v2}, new double[]{w1, 1 - w1});
    }

    /**
     * Takes a weighted average of the vectors. The output is equal to
     * <pre>sum(v[i] * w[i]) / sum(w[i]).</pre>
     */
    public static Vec2 average(Vec2[] v, double[] w) {
        return IntStream.range(0, v.length)
            .mapToObj(i -> v[i].times(w[i]))
            .reduce(Vec2.zero, Vec2::add)
            .times(1 / Arrays.stream(w).sum());
    }

    /**
     * Linearly interpolates between two vectors
     * When t is zero, the output is v1, and when t is 1, the
     * output is v2. Otherwise, the result is a mix of the
     * two vectors.
     */
    public static Vec2 interpolate(Vec2 v1, Vec2 v2, double t) {
        return average(v1, v2, 1 - t);
    }

    /**
     * Interpolates between two vectors using the {@link MathUtil#smoothStep} function
     */
    public static Vec2 smoothInterpolate(Vec2 v1, Vec2 v2, double t) {
        return interpolate(v1, v2, smoothStep(t));
    }
}
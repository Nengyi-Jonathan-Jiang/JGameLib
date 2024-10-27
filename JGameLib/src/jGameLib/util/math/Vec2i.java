package jGameLib.util.math;


import java.awt.*;
import java.util.Objects;
import java.util.function.Function;

/**
 * A class representing a pair of integer coordinates or size.
 * Vectors can be added to each other, scaled, and multiplied
 * component-wise. Vectors are immutable, which means that
 * they cannot be changed once constructed.
 */
public final class Vec2i {
    public final int x, y;
    /**
     * The zero vector [0, 0]
     */
    public static final Vec2i zero = new Vec2i(0, 0);

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs the vector [v, v]
     */
    public Vec2i(int v) {
        this(v, v);
    }

    /**
     * Creates a {@link Vec2i} from a {@link Dimension}
     */
    public Vec2i(Dimension d) {
        this(d.width, d.height);
    }

    /**
     * Creates a {@link Vec2i} from a {@link Vec2}. <br>
     * Decimal places are truncated.
     */
    public Vec2i(Vec2 v) {
        this((int) v.x, (int) v.y);
    }

    /**
     * Adds two vectors. <br>
     * add([x1, y1], [x2, y2]) = [x1 + x2, y1 + y2]
     */
    public static Vec2i add(Vec2i a, Vec2i b) {
        return new Vec2i(a.x + b.x, a.y + b.y);
    }

    /**
     * Subtracts two vectors. <br>
     * sub([x1, y1], [x2, y2]) = [x1 - x2, y1 - y2]
     */
    public static Vec2i sub(Vec2i a, Vec2i b) {
        return new Vec2i(a.x - b.x, a.y - b.y);
    }

    /**
     * Scales a vector by a constant c. <br>
     * scale([x, y], c) = [c * x, c * y]
     */
    public static Vec2i scale(Vec2i p, int c) {
        return new Vec2i(p.x * c, p.y * c);
    }

    /**
     * Scales each component of a vector by sx and sy, respectively. <br>
     * scale([x, y], sx, sy) = [sx * x, sy * y]. <br>
     * This is a shortcut for component-wise vector multiplication. <br>
     */
    public static Vec2i scale(Vec2i p, int sx, int sy) {
        return new Vec2i(p.x * sx, p.y * sy);
    }

    /**
     * @return The length of the vector sqrt(x * x + y * y)
     */
    public static double magnitude(Vec2i p) {
        return Math.sqrt(p.x * p.x + p.y * p.y);
    }

    /**
     * @return The distance between two vectors, equal to
     * magnitude(sub(a, b))
     */
    public static double distance(Vec2i a, Vec2i b) {
        return sub(a, b).magnitude();
    }

    /**
     * @return The manhattan distance between two vectors. <br>
     * manhattanDistance([x1, y1], [x2, y2]) = abs(x1 - x2) + abs(y1 - y2)
     */
    public static int manhattanDistance(Vec2i a, Vec2i b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    /**
     * Converts this {@link Vec2i} into a {@link Dimension}
     */
    public Dimension toDimension() {
        return new Dimension((int) x, (int) y);
    }

    /**
     * @return The x-component of the vector
     */
    public int x() {
        return x;
    }

    /**
     * @return The y-component of the vector
     */
    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Vec2i) obj;
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
     * Shorthand for {@link Vec2i#add(Vec2i, Vec2i)}
     */
    public Vec2i plus(Vec2i other) {
        return add(this, other);
    }

    /**
     * Shorthand for {@link Vec2i#sub(Vec2i, Vec2i)}
     */
    public Vec2i minus(Vec2i other) {
        return sub(this, other);
    }

    /**
     * Shorthand for {@link Vec2i#scale(Vec2i, int)}
     */
    public Vec2i times(int c) {
        return scale(this, c);
    }

    /**
     * Shorthand for {@link Vec2i#scale(Vec2i, int, int)}
     */
    public Vec2i times(int sx, int sy) {
        return scale(this, sx, sy);
    }

    /**
     * Equivalent to {@link Vec2i#magnitude(Vec2i)}
     */
    public double magnitude() {
        return magnitude(this);
    }

    /**
     * Shorthand for {@link Vec2i#distance(Vec2i, Vec2i)}
     */
    public double distanceTo(Vec2i other) {
        return distance(this, other);
    }

    /**
     * Shorthand for {@link Vec2i#manhattanDistance(Vec2i, Vec2i)}
     */
    public int manhattanDistanceTo(Vec2i other) {
        return manhattanDistance(this, other);
    }

    /**
     * Applies the given function to both components
     * of the vector and returns the resulting vector
     */
    public Vec2i transform(Function<Integer, Integer> func) {
        return new Vec2i(func.apply(x), func.apply(y));
    }
}
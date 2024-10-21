package jGameLib.ecs.ui2d;

import jGameLib.ecs.StateMachine;
import jGameLib.math.Vec2;
import jGameLib.math.Vec2i;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

/**
 * A canvas on which to draw stuff.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class GameCanvas extends JPanel {
    public Graphics2D graphics = null;
    private Consumer<JGraphics> drawFunction = null;

    public final Object synchronizer = new Object();

    private final Vec2i referenceSize;
    private double scale = 1;

    public GameCanvas(Vec2i referenceSize) {
        setBackground(Color.BLACK);
        this.referenceSize = referenceSize;
    }

    /**
     * Repaints the canvas, using the given function. Future calls to {@link JPanel#repaint()}, not
     * necessarily triggered by the {@link StateMachine}, will also use the
     * function.
     *
     * @param drawFunction The function to call to paint the canvas
     */
    public void repaint(Consumer<JGraphics> drawFunction) {
        this.drawFunction = drawFunction;
        super.repaint();
    }

    /**
     * Paints the canvas with the given graphics context, should never be called directly
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics = (Graphics2D) g.create();

        scale = referenceSize == null
            ? 1
            : referenceSize.y < getAspectRatio() * referenceSize.x
            ? 1.0 * getWidth() / referenceSize.x
            : 1.0 * getHeight() / referenceSize.y;

        if (drawFunction != null) {
            synchronized (synchronizer) {
                graphics.transform(new AffineTransform(scale, 0, 0, scale, getWidth() / 2., getHeight() / 2.));
                drawFunction.accept(new JGraphics(graphics, getSizeAsVec()));
            }
        }
    }

    public double getScale() {
        return scale;
    }

    public Vec2 getMousePos() {
        return toWorldSpace(getMousePosition());
    }

    public Vec2 toWorldSpace(Point mousePosition) {
        return mousePosition == null ? null : new Vec2(mousePosition.x, mousePosition.y).minus(getSizeAsVec().times(.5)).times(1 / getScale());
    }

    public Vec2 getSizeAsVec() {
        return new Vec2(getWidth(), getHeight());
    }

    public double getAspectRatio() {
        return getSizeAsVec().y / getSizeAsVec().x;
    }
}
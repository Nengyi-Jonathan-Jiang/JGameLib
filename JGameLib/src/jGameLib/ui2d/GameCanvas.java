package jGameLib.ui2d;

import jGameLib.core.StateMachine;
import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

/**
 * A canvas on which to draw stuff.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class GameCanvas {
    public Graphics2D graphics = null;
    private Consumer<JGraphics> drawFunction = null;

    private double scale = 1;

    public final Object synchronizer = new Object();
    public final JPanel canvas;

    public GameCanvas(Vec2i referenceSize) {
        this.canvas = new JPanel() {
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
                        drawFunction.accept(new JGraphics(graphics, GameCanvas.this.getSize()));
                    }
                }
            }
        };
        canvas.setBackground(Color.BLACK);
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
        canvas.repaint();
    }

    public double getScale() {
        return scale;
    }

    public Vec2 getMousePos() {
        return toWorldSpace(canvas.getMousePosition());
    }

    public Vec2 toWorldSpace(Point mousePosition) {
        return mousePosition == null ? null : new Vec2(mousePosition.x, mousePosition.y).minus(getSize().times(.5)).times(1 / getScale());
    }

    public Vec2 getSize() {
        return new Vec2(canvas.getWidth(), canvas.getHeight());
    }

    public double getAspectRatio() {
        return getSize().y / getSize().x;
    }
}
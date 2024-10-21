package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.JGraphics;
import jGameLib.math.Vec2;
import jGameLib.math.Vec2i;
import jGameLib.util.Pair;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

@SuppressWarnings("unused")
public class RoundedRectRendererBehavior extends CachedRendererBehavior<Pair<Pair<Double, Color>, Color>> {
    protected final static Color DEFAULT_BORDER_COLOR = Color.BLACK;
    protected final static Color DEFAULT_FILL_COLOR = new Color(0, 0, 0, 0);
    protected Color border_color;
    protected Color fill_color;
    protected double radius;

    public RoundedRectRendererBehavior(double radius, Color border_color, Color fill_color) {
        this.border_color = border_color;
        this.fill_color = fill_color;
        this.radius = radius;
    }

    public RoundedRectRendererBehavior setBorderColor(Color color) {
        border_color = color;
        return this;
    }

    public RoundedRectRendererBehavior setFillColor(Color color) {
        fill_color = color;
        return this;
    }

    public RoundedRectRendererBehavior setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    @Override
    protected void draw(Vec2 size, JGraphics graphics) {
        var shape = new RoundRectangle2D.Double(
            -boundingBox.getSize().x / 2,
            -boundingBox.getSize().y / 2,
            boundingBox.getSize().x,
            boundingBox.getSize().y,
            radius * 2, radius * 2);

        if (fill_color.getAlpha() != 0) {
            graphics.setColor(fill_color);
            graphics.originalGraphics().fill(shape);
        }
        if (border_color.getAlpha() != 0) {
            graphics.setColor(border_color);
            graphics.originalGraphics().draw(shape);
        }
    }

    @Override
    protected Pair<Pair<Double, Color>, Color> getCacheKey() {
        return new Pair<>(new Pair<>(radius, fill_color), border_color);
    }

    @Override
    protected Vec2i getCacheImageSize() {
        // Need some extra space for the border
        return new Vec2i(boundingBox.getSize().plus(4, 4));
    }
}
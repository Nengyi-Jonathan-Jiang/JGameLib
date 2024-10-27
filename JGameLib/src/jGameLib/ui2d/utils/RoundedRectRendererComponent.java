package jGameLib.ui2d.utils;

import jGameLib.ui2d.JGraphics;
import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;
import jGameLib.util.Pair;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

@SuppressWarnings("unused")
//public class RoundedRectRendererBehavior extends CachedRendererBehavior<Pair<Pair<Double, Color>, Color>> {
public class RoundedRectRendererComponent
    extends RectRendererComponent
    implements CacheableRendererComponent<Pair<Pair<Double, Color>, Color>> {

    protected double radius;

    public RoundedRectRendererComponent(double radius, Color border_color, Color fill_color) {
        this.border_color = border_color;
        this.fill_color = fill_color;
        this.radius = radius;
    }

    public RoundedRectRendererComponent setBorderColor(Color color) {
        border_color = color;
        return this;
    }

    public RoundedRectRendererComponent setFillColor(Color color) {
        fill_color = color;
        return this;
    }

    public RoundedRectRendererComponent setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public void drawToCachedImage(Vec2 size, JGraphics graphics) {
        var shape = new RoundRectangle2D.Double(
            -boundingBox.getSize().x / 2,
            -boundingBox.getSize().y / 2,
            boundingBox.getSize().x,
            boundingBox.getSize().y,
            radius * 2, radius * 2);

        if (fill_color != null && fill_color.getAlpha() != 0) {
            graphics.setColor(fill_color);
            graphics.originalGraphics().fill(shape);
        }
        if (border_color != null && border_color.getAlpha() != 0) {
            graphics.setColor(border_color);
            graphics.originalGraphics().draw(shape);
        }
    }

    @Override
    public Pair<Pair<Double, Color>, Color> getCacheKey() {
        return new Pair<>(new Pair<>(radius, fill_color), border_color);
    }

    @Override
    public Vec2i getCacheImageSize() {
        // Need some extra space for the border
        return new Vec2i(boundingBox.getSize().plus(4, 4));
    }

    @Override
    public void draw(JGraphics graphics) {
        CacheableRendererComponent.draw(this, graphics);
    }
}
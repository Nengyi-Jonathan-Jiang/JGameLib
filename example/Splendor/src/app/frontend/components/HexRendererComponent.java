package app.frontend.components;

import jGameLib.ecs.ui2d.JGraphics;
import jGameLib.ecs.ui2d.utils.CachedRendererBehavior;
import jGameLib.math.Vec2;
import jGameLib.util.Pair;

import java.awt.*;
import java.awt.geom.Path2D;

public class HexRendererComponent extends CachedRendererBehavior<Pair<Color, Color>> {
    protected final static Color DEFAULT_BORDER_COLOR = Color.BLACK;
    protected final static Color DEFAULT_FILL_COLOR = new Color(0, 0, 0, 0);
    protected Color border_color;
    protected Color fill_color;

    public HexRendererComponent() {
        this(DEFAULT_BORDER_COLOR);
    }

    public HexRendererComponent(Color border_color) {
        this(border_color, DEFAULT_FILL_COLOR);
    }

    public HexRendererComponent(Color border_color, Color fill_color) {
        this.border_color = border_color;
        this.fill_color = fill_color;
    }

    public HexRendererComponent setBorderColor(Color color) {
        border_color = color;
        return this;
    }

    public HexRendererComponent setFillColor(Color color) {
        fill_color = color;
        return this;
    }

    @Override
    protected void draw(Vec2 size, JGraphics graphics) {
        Vec2 p1 = size.times(0.0, .25),
            p2 = size.times(0.5, 0.0),
            p3 = size.times(1.0, .25),
            p4 = size.times(1.0, .75),
            p5 = size.times(0.5, 1.0),
            p6 = size.times(0.0, .75);

        var path = new Path2D.Double();
        path.moveTo(p6.x, p6.y);
        for (Vec2 p : new Vec2[]{p1, p2, p3, p4, p5, p6}) path.lineTo(p.x, p.y);

        if (fill_color.getAlpha() != 0) {
            graphics.setColor(fill_color);
            graphics.originalGraphics().fill(path);
        }
        if (border_color.getAlpha() != 0) {
            graphics.setColor(border_color);
            graphics.originalGraphics().draw(path);
        }
    }

    @Override
    protected Pair<Color, Color> getCacheKey() {
        return new Pair<>(fill_color, border_color);
    }
}

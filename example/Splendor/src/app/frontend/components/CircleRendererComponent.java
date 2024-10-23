package app.frontend.components;

import jGameLib.ui2d.JGraphics;
import jGameLib.ui2d.utils.CacheableRendererComponent;
import jGameLib.ui2d.utils.RectRendererComponent;
import jGameLib.util.math.Vec2;
import jGameLib.util.math.Vec2i;
import jGameLib.util.Pair;

import java.awt.*;

public class CircleRendererComponent
    extends RectRendererComponent
    implements CacheableRendererComponent<Pair<Color, Color>> {

    public CircleRendererComponent(Color border_color, Color fill_color) {
        super(border_color, fill_color);
    }

    @Override
    public void draw(JGraphics graphics) {
        CacheableRendererComponent.draw(this, graphics);
    }

    @Override
    public void drawToCachedImage(Vec2 size, JGraphics graphics) {
        Vec2i s = new Vec2i(boundingBox.getSize().times(0.5)).times(2);
        graphics.setColor(fill_color);
        graphics.originalGraphics().fillArc(-s.x / 2, -s.y / 2, s.x, s.y, 0, 360);
        graphics.setColor(border_color);
        graphics.originalGraphics().drawArc(-s.x / 2, -s.y / 2, s.x, s.y, 0, 360);
    }

    @Override
    public Pair<Color, Color> getCacheKey() {
        return new Pair<>(border_color, fill_color);
    }

    @Override
    public Vec2i getCacheImageSize() {
        return new Vec2i(boundingBox.getSize().plus(4, 4));
    }
}
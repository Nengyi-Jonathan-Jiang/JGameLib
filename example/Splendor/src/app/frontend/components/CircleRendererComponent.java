package app.frontend.components;

import jGameLib.ecs.ui2d.JGraphics;
import jGameLib.ecs.ui2d.utils.RectRendererComponent;
import jGameLib.math.Vec2i;

import java.awt.*;

public class CircleRendererComponent extends RectRendererComponent {

    public CircleRendererComponent(Color border_color, Color fill_color) {
        super(border_color, fill_color);
    }

    @Override
    public void draw(JGraphics graphics) {
        Vec2i p = new Vec2i(boundingBox.getAbsoluteTopLeft());
        Vec2i s = new Vec2i(boundingBox.getSize());

        graphics.setColor(fill_color);
        graphics.originalGraphics().fillArc(p.x, p.y, s.x, s.y, 0, 360);
        graphics.setColor(border_color);
        graphics.originalGraphics().drawArc(p.x, p.y, s.x, s.y, 0, 360);
    }
}
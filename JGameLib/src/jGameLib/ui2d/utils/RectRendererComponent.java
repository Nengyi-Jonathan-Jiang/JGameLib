package jGameLib.ui2d.utils;

import jGameLib.ui2d.JGraphics;
import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ui2d.rendering.UIRendererComponent;

import java.awt.*;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RectRendererComponent extends UIRendererComponent {
    protected final static Color DEFAULT_BORDER_COLOR = Color.BLACK;
    protected final static Color DEFAULT_FILL_COLOR = new Color(0, 0, 0, 0);
    protected Color border_color;
    protected Color fill_color;

    public RectRendererComponent() {
        this(DEFAULT_BORDER_COLOR);
    }

    public RectRendererComponent(Color border_color) {
        this(border_color, DEFAULT_FILL_COLOR);
    }

    public RectRendererComponent(Color border_color, Color fill_color) {
        this.border_color = border_color;
        this.fill_color = fill_color;
    }

    @Override
    public void draw(JGraphics graphics) {
        graphics
            .setColor(fill_color)
            .fillRect(
                entity.getComponent(BoundingBoxComponent.class).getAbsoluteTopLeft(),
                entity.getComponent(BoundingBoxComponent.class).getSize()
            )
            .setColor(border_color)
            .drawRect(
                entity.getComponent(BoundingBoxComponent.class).getAbsoluteTopLeft(),
                entity.getComponent(BoundingBoxComponent.class).getSize()
            );
    }

    public RectRendererComponent setBorderColor(Color color) {
        border_color = color;
        return this;
    }

    public RectRendererComponent setFillColor(Color color) {
        fill_color = color;
        return this;
    }
}
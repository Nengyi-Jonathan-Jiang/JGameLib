package jGameLib.ecs.ui2d.utils;

import jGameLib.ecs.ui2d.JGraphics;
import jGameLib.ecs.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ecs.ui2d.rendering.UIRendererComponent;
import jGameLib.math.Vec2;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * @noinspection UnusedReturnValue, unused
 */
public class TextRendererBehavior extends UIRendererComponent {
    protected static final TextStyle DEFAULT_STYLE = new TextStyle();
    protected TextStyle style;
    protected String text;
    protected String[] lines;
    protected double[] lineY;
    protected TextLayout[] lineLayouts;
    private Vec2 textOffset;
    private Vec2 renderedSize;
    private boolean useUnderline = false;

    // Dummy graphics object we use for string size evaluation
    private static final Graphics2D _graphics = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();

    public TextRendererBehavior(String text) {
        this(text, DEFAULT_STYLE);
    }

    public TextRendererBehavior(String text, Font font) {
        this(text, new TextStyle(font));
    }

    public TextRendererBehavior(String text, TextStyle style) {
        setTextNoCalculateBounds(text);
        setStyleNoCalculateBounds(style);
        recalculateBounds();
    }

    public String getText() {
        return text;
    }

    public TextRendererBehavior setText(String text) {
        setTextNoCalculateBounds(text);
        recalculateBounds();
        return this;
    }

    private void setTextNoCalculateBounds(String text) {
        if (text.equals(this.text)) return;
        this.text = text;
        this.lines = text.isEmpty() ? new String[]{} : text.split("\n");
    }

    public void setStyle(TextStyle style) {
        setStyleNoCalculateBounds(style);
        recalculateBounds();
    }

    private void setStyleNoCalculateBounds(TextStyle style) {
        this.style = style;
    }

    public Vec2 getRenderedSize() {
        return renderedSize;
    }

    public TextRendererBehavior resizeToFit(double padding) {
        entity.getComponent(BoundingBoxComponent.class)
            .setSize(getRenderedSize().plus(new Vec2(padding * 2)));
        return this;
    }

    public TextRendererBehavior resizeWidthToFit(double padding) {
        entity.getComponent(BoundingBoxComponent.class)
            .setWidth(getRenderedSize().x + 2 * padding);
        return this;
    }

    public TextRendererBehavior resizeHeightToFit(double padding) {
        entity.getComponent(BoundingBoxComponent.class)
            .setHeight(getRenderedSize().y + 2 * padding);
        return this;
    }

    public Font getFont() {
        return style.font;
    }

    public TextStyle getStyle() {
        return style;
    }

    private void recalculateBounds() {

        FontRenderContext context = _graphics.getFontRenderContext();

        lineLayouts = Arrays.stream(lines).map(i -> new TextLayout(i, style.font, context)).toArray(TextLayout[]::new);

        double width = 0, height = 0;
        lineY = new double[lines.length];
        for (int i = 0; i < lines.length; i++) {
            TextLayout txt = new TextLayout(lines[i], style.font, context);
            Rectangle2D size = txt.getBounds();

            width = Math.max(width, size.getWidth());
            height += size.getHeight() * (i == 0 ? 1 : 1.2);
            lineY[i] = height;
        }
        renderedSize = new Vec2(width, height);

        textOffset = new Vec2(
            switch (style.getHorizontalAlignment()) {
                case START -> 0;
                case CENTER -> .5;
                case END -> 1;
            },
            switch (style.getVerticalAlignment()) {
                case START -> 0;
                case CENTER -> .5;
                case END -> 1;
            }
        ).times(getRenderedSize());
    }

    @Override
    public void draw(JGraphics graphics) {
        Vec2 rect_start = entity.getComponent(BoundingBoxComponent.class).getAbsoluteTopLeft();
        Vec2 text_start = rect_start.minus(textOffset).plus(new Vec2(
            switch (style.getHorizontalAlignment()) {
                case START -> 0;
                case CENTER -> entity.getComponent(BoundingBoxComponent.class).getSize().x() * 0.5;
                case END -> entity.getComponent(BoundingBoxComponent.class).getSize().x();
            },
            switch (style.getVerticalAlignment()) {
                case START -> 0;
                case CENTER -> entity.getComponent(BoundingBoxComponent.class).getSize().y() * 0.5;
                case END -> entity.getComponent(BoundingBoxComponent.class).getSize().y();
            })
        );

        graphics.setColor(style.fg_color).setDrawFont(style.font);
        for (int i = 0; i < lines.length; i++) {
            lineLayouts[i].draw(graphics.originalGraphics(), (float) text_start.x, (float) (text_start.y + lineY[i]));
            if (useUnderline) {
                graphics.drawRect(text_start.plus(new Vec2(0, lineY[i])), new Vec2(getRenderedSize().x, 0));
            }
        }
    }

    public void setUnderlined(boolean underline) {
        this.useUnderline = underline;
    }
}
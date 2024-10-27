package jGameLib.ui2d.utils;

import jGameLib.ui2d.JGraphics;
import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.util.math.Vec2;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class TextRendererComponent extends UIRendererComponent {
    protected static final TextStyle DEFAULT_STYLE = new TextStyleBuilder().get();
    protected TextStyle style;
    protected String text;
    protected String[] lines;
    protected Vec2[] lineOffset;
    protected TextLayout[] lineLayouts;
    private Vec2 textOffset;
    private Vec2 renderedSize;
    private boolean useUnderline = false;

    // Dummy graphics object we use for string size evaluation
    private static final Graphics2D _graphics = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();

    public TextRendererComponent(String text) {
        this(text, DEFAULT_STYLE);
    }

    public TextRendererComponent(String text, TextStyle style) {
        setTextNoCalculateBounds(text);
        setStyleNoCalculateBounds(style);
        recalculateBounds();
    }

    public String getText() {
        return text;
    }

    public TextRendererComponent setText(String text) {
        setTextNoCalculateBounds(text);
        recalculateBounds();
        return this;
    }

    private void setTextNoCalculateBounds(String text) {
        if (text.equals(this.text)) return;
        this.text = text;
        this.lines = text.isEmpty() ? new String[]{} : text.split("\n");
    }

    public TextRendererComponent setStyle(TextStyle style) {
        setStyleNoCalculateBounds(style);
        recalculateBounds();
        return this;
    }

    private void setStyleNoCalculateBounds(TextStyle style) {
        this.style = style;
    }

    public Vec2 getRenderedSize() {
        return renderedSize;
    }

    public TextRendererComponent resizeToFit(double padding) {
        entity.getComponent(BoundingBoxComponent.class)
            .setSize(getRenderedSize().plus(new Vec2(padding * 2)));
        return this;
    }

    public TextRendererComponent resizeWidthToFit(double padding) {
        entity.getComponent(BoundingBoxComponent.class)
            .setWidth(getRenderedSize().x + 2 * padding);
        return this;
    }

    public TextRendererComponent resizeHeightToFit(double padding) {
        entity.getComponent(BoundingBoxComponent.class)
            .setHeight(getRenderedSize().y + 2 * padding);
        return this;
    }

    public Font getFont() {
        return style.font();
    }

    public TextStyle getStyle() {
        return style;
    }

    private void recalculateBounds() {
        FontRenderContext context = _graphics.getFontRenderContext();

        lineLayouts = Arrays.stream(lines).map(
            i -> new TextLayout(i, style.font(), context)
        ).toArray(TextLayout[]::new);

        double width = 0, height = 0;
        lineOffset = new Vec2[lines.length];
        for (int i = 0; i < lines.length; i++) {
            TextLayout txt = lineLayouts[i];
            Rectangle2D size = txt.getBounds();

            width = Math.max(width, size.getWidth());
            height += size.getHeight() * (i == 0 ? 1 : 1.2);
            lineOffset[i] = new Vec2(-size.getX(), height - size.getMaxY());
        }

        renderedSize = new Vec2(width, height);
        textOffset = new Vec2(
            style.alignment().h_align.p,
            style.alignment().v_align.p
        ).times(renderedSize);
    }

    @Override
    public void draw(JGraphics graphics) {
        Vec2 rect_start = boundingBox.getAbsoluteTopLeft().plus(
            boundingBox.getSize().times(
                style.alignment().h_align.p,
                style.alignment().v_align.p
            )
        );
        Vec2 text_start = rect_start.minus(textOffset);

        graphics.setColor(style.color()).setDrawFont(style.font());
        for (int i = 0; i < lines.length; i++) {
            Vec2 line_start = text_start.plus(lineOffset[i]);

            lineLayouts[i].draw(
                graphics.originalGraphics(),
                (float) line_start.x,
                (float) line_start.y
            );
            if (useUnderline) {
                // Since the line height is comparable to the stroke width of 2px, we use fillRect to have precise
                // control over size
                graphics.fillRect(
                    // Add a bit of offset to make it look nicer
                    line_start.plus(0, 1),
                    new Vec2(getRenderedSize().x, 1)
                );
            }
        }
    }

    public void setUnderlined(boolean underline) {
        this.useUnderline = underline;
    }
}
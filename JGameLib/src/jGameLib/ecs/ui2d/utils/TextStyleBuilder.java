package jGameLib.ecs.ui2d.utils;

import java.awt.*;

public class TextStyleBuilder {

    private static final Font DEFAULT_FONT = new Font("Times New Roman", Font.PLAIN, 12);
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final TextStyle.TextAlign DEFAULT_ALIGNMENT = TextStyle.TextAlign.ALIGN_TOP_LEFT;

    public Font font = DEFAULT_FONT;
    public TextStyle.TextAlign alignment = DEFAULT_ALIGNMENT;
    public Color color = DEFAULT_FG_COLOR;

    public TextStyleBuilder() {

    }

    public TextStyleBuilder(TextStyle style) {
        this.font = style.font;
        this.alignment = style.alignment;
        this.color = style.color;
    }

    public TextStyleBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    public TextStyleBuilder setAlignment(TextStyle.TextAlign alignment) {
        this.alignment = alignment;
        return this;
    }

    public TextStyleBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public TextStyle get() {
        return new TextStyle(font, color, alignment);
    }
}

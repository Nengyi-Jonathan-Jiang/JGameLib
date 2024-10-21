package jGameLib.ecs.ui2d.utils;

import java.awt.*;

public class TextStyleBuilder {

    private static final Font DEFAULT_FONT = new Font("Times New Roman", Font.PLAIN, 12);
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 0);
    private static final TextStyle.TextAlign DEFAULT_ALIGNMENT = TextStyle.TextAlign.ALIGN_TOP_LEFT;

    public Font font = DEFAULT_FONT;
    public TextStyle.TextAlign alignment = DEFAULT_ALIGNMENT;
    public Color fg_color = DEFAULT_FG_COLOR;
    public Color bgColor = DEFAULT_BG_COLOR;

    public TextStyleBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    public TextStyleBuilder setAlignment(TextStyle.TextAlign alignment) {
        this.alignment = alignment;
        return this;
    }

    public TextStyleBuilder setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        return this;
    }

    public TextStyleBuilder setFgColor(Color fgColor) {
        this.fg_color = fgColor;
        return this;
    }

    public TextStyle get() {
        return new TextStyle(font, fg_color, bgColor, alignment);
    }
}

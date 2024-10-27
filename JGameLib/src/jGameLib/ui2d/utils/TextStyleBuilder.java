package jGameLib.ui2d.utils;

import jGameLib.util.files.FontLoader;

import java.awt.*;

@SuppressWarnings("unused")
public class TextStyleBuilder {

    private static final Font DEFAULT_FONT = new Font("Times New Roman", Font.PLAIN, 12);
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final TextStyle.TextAlign DEFAULT_ALIGNMENT = TextStyle.TextAlign.TOP_LEFT;

    public enum FontStyle {
        NORMAL, ITALIC, BOLD
    }

    private Font font = DEFAULT_FONT;
    private TextStyle.TextAlign alignment = DEFAULT_ALIGNMENT;
    private Color color = DEFAULT_FG_COLOR;
    private FontStyle fontStyle = null;
    private Double fontSize = null;

    public TextStyleBuilder() {

    }

    public TextStyleBuilder(TextStyle style) {
        this.font = style.font();
        this.alignment = style.alignment();
        this.color = style.color();
    }

    public TextStyleBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    public TextStyleBuilder setFont(String fontPath) {
        return setFont(FontLoader.load(fontPath));
    }

    public TextStyleBuilder setAlignment(TextStyle.TextAlign alignment) {
        this.alignment = alignment;
        return this;
    }

    public TextStyleBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public TextStyleBuilder setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public TextStyleBuilder setFontSize(double fontSize) {
        this.fontSize = fontSize;
        return this;
    }


    public TextStyle get() {
        Font modifiedFont = font;
        if (fontSize != null) {
            modifiedFont = modifiedFont.deriveFont((float) (double) fontSize);
        }
        if (fontStyle != null) {
            modifiedFont = modifiedFont.deriveFont(switch (fontStyle) {
                case NORMAL -> Font.PLAIN;
                case ITALIC -> Font.ITALIC;
                case BOLD -> Font.BOLD;
            });
        }
        return new TextStyle(modifiedFont, color, alignment);
    }
}

package jGameLib.ecs.ui2d.utils;

import java.awt.*;

/**
 * @noinspection unused
 */
public final class TextStyle {
    public enum TextAlign {
        ALIGN_TOP_LEFT(Alignment.START, Alignment.START),
        ALIGN_TOP_CENTER(Alignment.START, Alignment.CENTER),
        ALIGN_TOP_RIGHT(Alignment.START, Alignment.END),
        ALIGN_CENTER_LEFT(Alignment.CENTER, Alignment.START),
        ALIGN_CENTER(Alignment.CENTER, Alignment.CENTER),
        ALIGN_CENTER_RIGHT(Alignment.CENTER, Alignment.END),
        ALIGN_BOTTOM_LEFT(Alignment.END, Alignment.START),
        ALIGN_BOTTOM_CENTER(Alignment.END, Alignment.CENTER),
        ALIGN_BOTTOM_RIGHT(Alignment.END, Alignment.END);

        final Alignment v_align, h_align;

        TextAlign(Alignment v_align, Alignment h_align) {
            this.v_align = v_align;
            this.h_align = h_align;
        }
    }

    private static final Font DEFAULT_FONT = new Font("Times New Roman", Font.PLAIN, 12);
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 0);
    private static final TextAlign DEFAULT_ALIGNMENT = TextAlign.ALIGN_TOP_LEFT;

    public final Font font;
    public final TextAlign alignment;
    public final Color color;

    public TextStyle(Font font, Color color, TextAlign alignment) {
        this.font = font;
        this.color = color;
        this.alignment = alignment;
    }

    enum Alignment {
        START(0), CENTER(0.5), END(1);

        public final double p;

        Alignment(double p) {
            this.p = p;
        }
    }
}

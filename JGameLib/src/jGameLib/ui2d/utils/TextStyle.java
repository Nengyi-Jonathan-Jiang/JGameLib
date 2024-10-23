package jGameLib.ui2d.utils;

import java.awt.*;

@SuppressWarnings("unused")
public record TextStyle(Font font, Color color, TextStyle.TextAlign alignment) {
    public enum TextAlign {
        TOP_LEFT(Alignment.START, Alignment.START),
        TOP(Alignment.START, Alignment.CENTER),
        TOP_RIGHT(Alignment.START, Alignment.END),
        LEFT(Alignment.CENTER, Alignment.START),
        CENTER(Alignment.CENTER, Alignment.CENTER),
        RIGHT(Alignment.CENTER, Alignment.END),
        BOTTOM_LEFT(Alignment.END, Alignment.START),
        BOTTOM(Alignment.END, Alignment.CENTER),
        BOTTOM_RIGHT(Alignment.END, Alignment.END);

        final Alignment v_align, h_align;

        TextAlign(Alignment v_align, Alignment h_align) {
            this.v_align = v_align;
            this.h_align = h_align;
        }
    }

    private static final Font DEFAULT_FONT = new Font("Times New Roman", Font.PLAIN, 12);
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final Color DEFAULT_BG_COLOR = new Color(0, 0, 0, 0);
    private static final TextAlign DEFAULT_ALIGNMENT = TextAlign.TOP_LEFT;

    enum Alignment {
        START(0), CENTER(0.5), END(1);

        public final double p;

        Alignment(double p) {
            this.p = p;
        }
    }
}

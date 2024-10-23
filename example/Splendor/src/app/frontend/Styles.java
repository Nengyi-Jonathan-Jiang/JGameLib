package app.frontend;

import jGameLib.util.files.FontLoader;

import java.awt.*;

public class Styles {
    public static final Font FONT_FANCY = FontLoader.load("fonts/Nunito.ttf");
    public static final Font FONT_INFO = FontLoader.load("fonts/JetBrainsMono.ttf");

    public static class Colors {
        public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
        public static final Color T_DARKEN = new Color(0, 0, 0, 80);
        public static final Color T_LIGHTEN = new Color(255, 255, 255, 80);

        public static final Color DARKER = new Color(54, 32, 28);
        public static final Color DARK = new Color(69, 46, 34);
        public static final Color MEDIUM = new Color(196, 183, 154);
        public static final Color LIGHT = new Color(234, 217, 183);

        public static Color PHASE(Color color) {
            return setAlpha(color, (int) ((Math.sin(System.currentTimeMillis() / 300.) + 1) * 40));
        }

        public static Color PHASE(Color color, double amplitude) {
            return setAlpha(color, (int) ((Math.sin(System.currentTimeMillis() / 300.) + 1) * amplitude / 2));
        }

        public static Color setAlpha(Color color, int alpha) {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        }

        public static Color addAlpha(Color color, int alpha) {
            return setAlpha(color, Math.min(Math.max(color.getAlpha() + alpha, 0), 255));
        }
    }
}
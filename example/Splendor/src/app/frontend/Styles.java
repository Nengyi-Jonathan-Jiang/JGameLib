package app.frontend;

import jGameLib.math.Vec2;
import jGameLib.util.FontLoader;

import java.awt.*;

public class Styles {
    public static final Font FONT_FANCY = FontLoader.load("font/Redressed-Regular.ttf");
    public static final Font FONT_INFO = FontLoader.load("font/JetBrainsMono-Regular.ttf");

    public static final Font FONT_FANCY_BIG = FONT_FANCY.deriveFont(50f);
    public static final Font FONT_FANCY_NORMAL = FONT_FANCY.deriveFont(40f);
    public static final Font FONT_FANCY_MEDIUM = FONT_FANCY.deriveFont(30f);
    public static final Font FONT_FANCY_SMALL = FONT_FANCY.deriveFont(20f);

    public static final Vec2 HEX_SIZE = new Vec2(0.866025404, 1).times(50);
    public static final Vec2 HEX_SPACING = HEX_SIZE.times(1, 0.75);

    public static final Vec2 SECTOR_SPACING = HEX_SPACING.times(10);
    public static final Vec2 SECTOR_SIZE = HEX_SIZE.times(10.5, 7.75);

    public static final Vec2 BOARD_SIZE = HEX_SIZE.times(20.5, 15.25);


    public static final Vec2 HEX_TILE_SIZE = new Vec2(0.866025404, 1).times(40);

    public static final Vec2 CARD_SIZE = new Vec2(2, 3).times(55);

    public static final Vec2 SETTLEMENT_SIZE = new Vec2(30);
    public static final Vec2 UI_ICON_SIZE = new Vec2(50);
    public static final Vec2 ACTION_SLOT_SIZE = new Vec2(0.866025404, 1).times(80);
    public static final Vec2 OBJECTIVE_CARD_SIZE = new Vec2(2, 3).times(80);
    public static final Vec2 PRESET_CARD_SIZE = new Vec2(2, 3).times(90);

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
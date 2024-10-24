package app.backend;

import java.awt.*;

public enum Gem {
    BLACK,
    RED,
    GREEN,
    BLUE,
    WHITE;

    public Color getColor() {
        return switch (this) {
            case RED -> Color.RED;
            case GREEN -> Color.GREEN;
            case BLUE -> Color.BLUE;
            case BLACK -> Color.BLACK;
            case WHITE -> Color.WHITE;
        };
    }
}

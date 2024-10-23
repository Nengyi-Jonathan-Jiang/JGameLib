package jGameLib.ui2d.input;

import jGameLib.util.math.Vec2;

public record MouseEvent(Vec2 position, MouseButton button, MouseEventType mouseEventType) {
    public enum MouseButton {
        LEFT, RIGHT, MIDDLE, NONE
    }

    public enum MouseEventType {
        MOUSE_DOWN,
        MOUSE_UP
    }

    public String toString() {
        return "MouseEvent { position = " + position + ", button = " + button + ", type = " + mouseEventType + "}";
    }
}
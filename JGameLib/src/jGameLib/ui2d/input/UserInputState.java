package jGameLib.ui2d.input;

import jGameLib.util.math.Vec2;

import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("unused")
public class UserInputState {
    final Set<Character> keysDown = new TreeSet<>();
    Vec2 mousePosition = Vec2.zero;
    boolean mouseDownR = false, mouseDownL = false;

    UserInputState() {
    }

    public Vec2 getMousePosition() {
        return mousePosition;
    }

    public boolean isMouseLeftDown() {
        return mouseDownL;
    }

    public boolean isMouseRightDown() {
        return mouseDownR;
    }

    public boolean isKeyDown(char key) {
        return keysDown.contains(key);
    }

    public double getTimeInSeconds() {
        return System.currentTimeMillis() / 1000.;
    }

    void setMousePosition(Vec2 v) {
        if (v != null) mousePosition = v;
    }
}
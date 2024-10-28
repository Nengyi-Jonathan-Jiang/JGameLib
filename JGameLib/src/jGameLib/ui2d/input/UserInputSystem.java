package jGameLib.ui2d.input;

import jGameLib.core.Entity;
import jGameLib.core.GameState;
import jGameLib.core.JSystem;
import jGameLib.ui2d.GameCanvas;
import jGameLib.util.math.Vec2;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import static jGameLib.ui2d.input.KeyEvent.KeyEventType.KEY_DOWN;
import static jGameLib.ui2d.input.KeyEvent.KeyEventType.KEY_UP;
import static jGameLib.ui2d.input.MouseEvent.MouseButton.LEFT;
import static jGameLib.ui2d.input.MouseEvent.MouseButton.RIGHT;
import static jGameLib.ui2d.input.MouseEvent.MouseEventType.MOUSE_DOWN;
import static jGameLib.ui2d.input.MouseEvent.MouseEventType.MOUSE_UP;

public class UserInputSystem extends JSystem {
    private final GameCanvas canvas;
    private final Queue<MouseEvent> mouseEvents = new ArrayDeque<>();
    private int wheelMovement = 0;
    private final Queue<KeyEvent> keyEvents = new ArrayDeque<>();
    private final UserInputState state = new UserInputState();

    public UserInputSystem(GameCanvas canvas) {
        this.canvas = canvas;

        canvas.canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                Vec2 pos = new Vec2(e.getPoint().x, e.getPoint().y).minus(canvas.getSize().times(.5)).times(1 / canvas.getScale());

                if (SwingUtilities.isLeftMouseButton(e)) {
                    state.mouseDownL = true;
                    mouseEvents.add(new MouseEvent(pos, LEFT, MOUSE_DOWN));
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    state.mouseDownR = true;
                    mouseEvents.add(new MouseEvent(pos, RIGHT, MOUSE_DOWN));
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                Vec2 pos = new Vec2(e.getPoint().x, e.getPoint().y).minus(canvas.getSize().times(.5)).times(1 / canvas.getScale());

                if (SwingUtilities.isLeftMouseButton(e)) {
                    state.mouseDownL = false;
                    mouseEvents.add(new MouseEvent(pos, LEFT, MOUSE_UP));
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    state.mouseDownR = false;
                    mouseEvents.add(new MouseEvent(pos, RIGHT, MOUSE_UP));
                }
            }
        });

        SwingUtilities.getWindowAncestor(canvas.canvas).addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (state.keysDown.add(e.getKeyChar())) {
                    keyEvents.add(new KeyEvent(e, KEY_DOWN));
                }
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                if (state.keysDown.remove(e.getKeyChar())) {
                    keyEvents.add(new KeyEvent(e, KEY_UP));
                }
            }
        });

        SwingUtilities.getWindowAncestor(canvas.canvas).addMouseWheelListener(e -> wheelMovement += e.getWheelRotation());

        SwingUtilities.getWindowAncestor(canvas.canvas).addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                state.keysDown.clear();
                state.mouseDownL = false;
                state.mouseDownR = false;
            }
        });
    }

    @Override
    public boolean canActOn(Entity e) {
        return e.hasComponent(UserInputHandlerComponent.class);
    }

    @Override
    public void applyAction(Collection<Entity> e, GameState currentState) {
        state.setMousePosition(canvas.getMousePos());

        List<UserInputHandlerComponent> components = e.stream()
            .map(i -> i.getComponent(UserInputHandlerComponent.class))
            .toList();

        apply(components, state, UserInputHandlerComponent::update);

        for (MouseEvent me; (me = mouseEvents.poll()) != null; ) {
            switch (me.mouseEventType()) {
                case MOUSE_DOWN -> apply(components, me, UserInputHandlerComponent::onMouseDown);
                case MOUSE_UP -> apply(components, me, UserInputHandlerComponent::onMouseUp);
            }
        }

        for (KeyEvent ke; (ke = keyEvents.poll()) != null; ) {
            switch (ke.keyEventType()) {
                case KEY_DOWN -> apply(components, ke, UserInputHandlerComponent::onKeyDown);
                case KEY_UP -> apply(components, ke, UserInputHandlerComponent::onKeyUp);
            }
        }

        if (wheelMovement != 0) {
            apply(components, wheelMovement, UserInputHandlerComponent::onScroll);
        }

        mouseEvents.clear();
        keyEvents.clear();
        wheelMovement = 0;
    }

    private interface EventApplier<T> {
        void apply(UserInputHandlerComponent component, T event);
    }

    private static <T> void apply(
        Collection<UserInputHandlerComponent> components,
        T event,
        EventApplier<T> applier
    ) {
        components.forEach(component -> applier.apply(component, event));
    }
}

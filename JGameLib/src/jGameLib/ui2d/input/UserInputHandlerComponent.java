package jGameLib.ui2d.input;

import jGameLib.core.Component;
import jGameLib.core.Entity;

/**
 * A component to process user input events given by the {@link UserInputSystem}
 */
@SuppressWarnings("unused")
public abstract class UserInputHandlerComponent extends Component {
    /**
     * Can be overridden. Called whenever the state is active and a mouse button is pressed.
     *
     * @param me The mouse event
     */
    protected void onMouseDown(MouseEvent me) {
    }

    /**
     * Can be overridden. Called whenever the state is active and a mouse button is released.
     *
     * @param me The mouse event
     */
    protected void onMouseUp(MouseEvent me) {
    }

    /**
     * Can be overridden. Called whenever the state is active and a key is pressed.
     *
     * @param ke The key event
     */
    protected void onKeyDown(KeyEvent ke) {
    }

    /**
     * Can be overridden. Called whenever the state is active and a key is released.
     *
     * @param ke The key event
     */
    protected void onKeyUp(KeyEvent ke) {
    }

    /**
     * Can be overridden. Called whenever the state is active and the mouse wheel is moved.
     *
     * @param distance the distance the wheel is moved
     */
    protected void onScroll(int distance) {
    }

    /**
     * Can be overridden. Called every frame with the current user input state.
     */
    protected void update(UserInputState state) {

    }

    @Override
    protected final void onSetEntity(Entity entity) {
    }
}

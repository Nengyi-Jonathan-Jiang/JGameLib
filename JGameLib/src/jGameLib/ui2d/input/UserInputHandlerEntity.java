package jGameLib.ui2d.input;

import jGameLib.core.Entity;
import jGameLib.core.GameState;

/**
 * The {@link Entity} counterpart to {@link UserInputHandlerComponent}.
 */
public abstract class UserInputHandlerEntity extends Entity {
    public UserInputHandlerEntity(GameState state) {
        super(state);
        addComponent(new UserInputHandlerComponent() {
            @Override
            protected void onMouseDown(MouseEvent me) {
                UserInputHandlerEntity.this.onMouseDown(me);
            }

            @Override
            protected void onMouseUp(MouseEvent me) {
                UserInputHandlerEntity.this.onMouseUp(me);
            }

            @Override
            protected void onKeyDown(KeyEvent ke) {
                UserInputHandlerEntity.this.onKeyDown(ke);
            }

            @Override
            protected void onKeyUp(KeyEvent ke) {
                UserInputHandlerEntity.this.onKeyUp(ke);
            }

            @Override
            protected void onScroll(int distance) {
                UserInputHandlerEntity.this.onScroll(distance);
            }

            @Override
            protected void update(UserInputState state) {
                UserInputHandlerEntity.this.update(state);
            }
        });
    }

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
}

import app.states.TitleState;
import jGameLib.ecs.StateMachine;
import jGameLib.ecs.ui2d.GameWindow;
import jGameLib.ecs.ui2d.input.UserInputSystem;
import jGameLib.ecs.ui2d.rendering.UIRendererSystem;
import jGameLib.math.Vec2;

import static jGameLib.ecs.GameState.makeWhileLoopIterator;
import static jGameLib.ecs.GameState.toGameState;

public class Main {
    public static void main(String[] args) {
        // Make the app very much faster!
        GameWindow.enableHardwareAcceleration();

        GameWindow window = new GameWindow("Splendor");

        // Add the user input system before the renderer system to allow ui changes due to input to
        // be executed as soon as possible. Note that we can just use the global instance because we
        // aren't using more than one StateMachine
        StateMachine.globalInstance.addSystem(new UserInputSystem(window.canvas));
        StateMachine.globalInstance.addSystem(new UIRendererSystem(window.canvas));

        StateMachine.globalInstance.run(
            toGameState(
                () -> makeWhileLoopIterator(
                    TitleState::new,
                    () -> true // Keep looping always
                )
            )
        );
    }
}
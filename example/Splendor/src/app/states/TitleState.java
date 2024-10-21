package app.states;

import jGameLib.ecs.ui2d.input.KeyEvent;
import jGameLib.ecs.ui2d.input.UserInputHandlerComponent;
import jGameLib.ecs.ui2d.input.UserInputHandlerEntity;
import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.ecs.ui2d.rendering.UIRendererRootComponent;
import jGameLib.ecs.ui2d.utils.ButtonEntity;
import jGameLib.ecs.ui2d.utils.PositionAnimationComponent;
import jGameLib.ecs.ui2d.utils.RectRendererBehavior;
import jGameLib.math.Vec2;
import jGameLib.util.FontLoader;

import java.awt.*;

public class TitleState extends BasicState {
    public TitleState() {
        new UIEntity()
            .addComponents(
                new UIRendererRootComponent(),
                new RectRendererBehavior(null, Color.WHITE)
            )
            .cast(UIEntity.class)
            .withBoundingBox(b -> b.setSize(1920, 1080));

        PositionAnimationComponent positionAnimator = new PositionAnimationComponent();
        new ButtonEntity(
            "Doing things cuz I can",
            FontLoader.DefaultFont.deriveFont(20f),
            Color.RED,
            Color.BLACK,
            Color.BLUE
        )
            .addClickListener((a, b) -> {
                switch (a.textRendererComponent.getText()) {
                    case "Doing things cuz I can" -> {
                        a.textRendererComponent.setText("Wow, you did a thing!");
                        positionAnimator.moveTo(new Vec2(400, -200), 50);
                    }
                    case "Wow, you did a thing!" -> a.textRendererComponent.setText("Bye now!");
                    case "Bye now!" -> a.destroy();
                }
                a.textRendererComponent.resizeWidthToFit(10);
            })
            .addComponents(positionAnimator)
            .addComponent(new UIRendererRootComponent());

        new UserInputHandlerEntity(new UserInputHandlerComponent() {
            @Override
            protected void onKeyDown(KeyEvent ke) {
                if (ke.key() == KeyEvent.Key.K_R) {
                    setNextState(new TitleState());
                }
            }
        });
    }

    @Override
    protected void onSchedule() {
        System.out.println("onSchedule");
    }

    @Override
    protected void onExecutionEnd() {
        System.out.println("onExecutionEnd");
    }

    @Override
    protected void onExecutionStart() {
        System.out.println("onExecutionStart");
    }
}
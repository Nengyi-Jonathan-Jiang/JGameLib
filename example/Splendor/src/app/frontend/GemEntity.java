package app.frontend;

import app.backend.Gem;
import app.frontend.components.CircleHoverDetectionComponent;
import app.frontend.components.CircleRendererComponent;
import jGameLib.ecs.GameState;
import jGameLib.ecs.ui2d.rendering.UIEntity;
import jGameLib.ecs.ui2d.utils.PositionAnimationComponent;

import java.awt.*;

public class GemEntity extends UIEntity {
    public GemEntity(GameState state, Gem gem) {
        super(state);
        addComponent(new CircleRendererComponent(Color.BLACK, switch (gem) {
            case RED -> Color.RED;
            case GREEN -> Color.GREEN;
            case BLUE -> Color.BLUE;
            case BLACK -> Color.BLACK;
            case WHITE -> Color.WHITE;
        }));
        addComponent(new CircleHoverDetectionComponent());
        addComponent(new PositionAnimationComponent());
        getBoundingBox().setSize(100, 100);
    }
}
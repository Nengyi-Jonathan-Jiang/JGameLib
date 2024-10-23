package jGameLib.ui2d.rendering;

import jGameLib.core.GameState;
import jGameLib.util.math.Vec2;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class VerticalLayoutEntity extends UIEntity {
    private Vec2 padding = new Vec2(0);

    public VerticalLayoutEntity(GameState state) {
        super(state);
        addComponent(new UIRendererComponent() {
            @Override
            public void update() {
                applyLayout();
            }
        });
    }

    public VerticalLayoutEntity applyLayout() {
        var w = boundingBox.getChildren().stream()
            .map(BoundingBoxComponent::getSize)
            .mapToDouble(Vec2::x)
            .max()
            .orElse(0);
        var h = boundingBox.getChildren().stream()
            .map(BoundingBoxComponent::getSize)
            .mapToDouble(Vec2::y)
            .sum();

        boundingBox.setSize(padding.times(2).plus(w, h));

        double y = 0;
        for (BoundingBoxComponent child : boundingBox.getChildren()) {
            child.setTopLeft(boundingBox.getTopLeftOffset().plus(padding).plus(0, y));
            y += child.getSize().y;
        }
        return this;
    }

    public VerticalLayoutEntity setPadding(Vec2 padding) {
        this.padding = padding;
        return this;
    }
}

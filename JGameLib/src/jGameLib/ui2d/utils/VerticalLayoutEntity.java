package jGameLib.ui2d.utils;

import jGameLib.core.GameState;
import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ui2d.rendering.UIEntity;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.util.math.Vec2;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class VerticalLayoutEntity extends UIEntity {
    private Vec2 padding = new Vec2(0);
    private double spacing = 0;

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
        var children = boundingBox.getChildren();

        var w = children.stream()
            .map(BoundingBoxComponent::getSize)
            .mapToDouble(Vec2::x)
            .max()
            .orElse(0);
        var h = children.stream()
            .map(BoundingBoxComponent::getSize)
            .mapToDouble(Vec2::y)
            .sum()
            + Math.max(children.size() - 1, 0) * spacing;

        boundingBox.setSize(padding.times(2).plus(w, h));

        double y = 0;
        for (BoundingBoxComponent child : children) {
            child.setTopLeft(boundingBox.getTopLeftOffset().plus(padding).plus(0, y));
            y += child.getSize().y + spacing;
        }
        return this;
    }

    public VerticalLayoutEntity setPadding(Vec2 padding) {
        this.padding = padding;
        return this;
    }

    public VerticalLayoutEntity setSpacing(double spacing) {
        this.spacing = spacing;
        return this;
    }
}

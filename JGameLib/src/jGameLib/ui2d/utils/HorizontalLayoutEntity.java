package jGameLib.ui2d.utils;

import jGameLib.core.GameState;
import jGameLib.ui2d.rendering.BoundingBoxComponent;
import jGameLib.ui2d.rendering.UIEntity;
import jGameLib.ui2d.rendering.UIRendererComponent;
import jGameLib.util.math.Vec2;

import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class HorizontalLayoutEntity extends UIEntity {
    private Vec2 padding = new Vec2(0);
    private double spacing = 0;

    public HorizontalLayoutEntity(GameState state) {
        super(state);
        addComponent(new UIRendererComponent() {
            @Override
            public void update() {
                applyLayout();
            }
        });
    }

    public void applyLayout() {
        var children = boundingBox.getChildren();
        var w = children.stream()
            .map(BoundingBoxComponent::getSize)
            .mapToDouble(Vec2::x)
            .sum()
            + Math.max(children.size() - 1, 0) * spacing;
        var h = children.stream()
            .map(BoundingBoxComponent::getSize)
            .mapToDouble(Vec2::y)
            .max()
            .orElse(0);

        boundingBox.setSize(padding.times(2).plus(w, h));

        double x = 0;
        for (BoundingBoxComponent child : children) {
            child.setTopLeft(boundingBox.getTopLeftOffset().plus(padding).plus(x, 0));
            x += child.getSize().x + spacing;
        }
    }

    public HorizontalLayoutEntity setPadding(Vec2 padding) {
        this.padding = padding;
        return this;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }
}

package jGameLib.ui2d.rendering;

import java.util.function.Consumer;

public interface HasBoundingBox {
    BoundingBoxComponent getBoundingBox();

    default HasBoundingBox withBoundingBox(Consumer<BoundingBoxComponent> action) {
        action.accept(getBoundingBox());
        return this;
    }
}

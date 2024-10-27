package jGameLib.ui2d.rendering;

import jGameLib.core.Component;
import jGameLib.core.Entity;

/**
 * Marks an {@link Entity} as being an entity being directly processed by the {@link UIRendererSystem}.
 * This must be added after a {@link BoundingBoxComponent} has been added to the entity.
 * Without this component, entities will not be detected by the UIRendererSystem unless their
 * bounding box is a descendant of the bounding box of an entity with this component.
 */
public class UIRendererRootComponent extends Component {
    @Override
    protected void onSetEntity(Entity entity) {
        if (!entity.hasComponent(BoundingBoxComponent.class)) {
            throw new RuntimeException("An entity that is a UI Renderer Root must have a BoundingBoxComponent");
        }
    }
}

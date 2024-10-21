package jGameLib.ecs.ui2d.rendering;

import jGameLib.ecs.Component;
import jGameLib.ecs.Entity;

/**
 * Marks an {@link Entity} as being an entity being directly processed by the {@link UIRendererSystem}.
 * This must be added after a {@link BoundingBoxComponent} has been added to the entity.
 */
public class UIRendererRootComponent extends Component {
    @Override
    protected void onSetEntity(Entity entity) {
        if (!entity.hasComponent(BoundingBoxComponent.class)) {
            throw new RuntimeException("An entity that is a UI Renderer Root must have a BoundingBoxComponent");
        }
    }
}

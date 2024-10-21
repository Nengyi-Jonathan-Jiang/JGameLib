package jGameLib.ecs.ui2d.rendering;

import jGameLib.ecs.Entity;
import jGameLib.ecs.GameState;
import jGameLib.ecs.JSystem;
import jGameLib.ecs.ui2d.GameCanvas;
import jGameLib.ecs.ui2d.JGraphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class UIRendererSystem extends JSystem {
    private final GameCanvas canvas;

    public UIRendererSystem(GameCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public boolean canActOn(Entity e) {
        return e.hasComponent(UIRendererRootComponent.class);
    }

    @Override
    public void applyAction(Collection<Entity> entities, GameState currentState) {
        List<BoundingBoxComponent> boundingBoxes = entities.stream()
            .map(i -> i.getComponent(BoundingBoxComponent.class))
            .toList();

        // Update the entities
        boundingBoxes.forEach(UIRendererSystem::update);

        // Compute the draw order
        List<UIRendererComponent> drawOrder = new ArrayList<>();
        addToDrawOrder(boundingBoxes, drawOrder);

        // Tell the canvas to redraw itself
        canvas.repaint((graphics) -> redraw(drawOrder, graphics));
    }

    private static void update(BoundingBoxComponent boundingBox) {
        if (boundingBox.getEntity().isEnabled() && boundingBox.isEnabled()) {
            // Update children first
            boundingBox.getChildren().forEach(UIRendererSystem::update);
            // Update enabled components on self
            boundingBox.getEntity().getAllComponents(UIRendererComponent.class).stream()
                .filter(UIRendererComponent::isEnabled)
                .forEach(UIRendererComponent::update);
        }
    }

    private static void addToDrawOrder(List<BoundingBoxComponent> boundingBoxes, List<UIRendererComponent> result) {
        List<BoundingBoxComponent> b = new ArrayList<>(boundingBoxes);
        // Sort by render order
        b.sort(Comparator.comparingDouble(BoundingBoxComponent::getRenderOrder));
        // Do stuff
        b.stream()
            .filter(BoundingBoxComponent::isEnabled)
            .filter(i -> i.getEntity().isEnabled())
            .forEachOrdered((boundingBox) -> {
                // Add own renderer components to draw order
                boundingBox.getEntity().getAllComponents(UIRendererComponent.class).stream()
                    .filter(UIRendererComponent::isEnabled)
                    .forEach(result::add);
                // Add children to draw order
                addToDrawOrder(boundingBox.getChildren(), result);
            });
    }

    private void redraw(List<UIRendererComponent> components, JGraphics graphics) {
        components.forEach(component -> component.draw(graphics));
    }
}

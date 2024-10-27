package jGameLib.ui2d.rendering;

import jGameLib.core.Entity;
import jGameLib.core.GameState;
import jGameLib.core.JSystem;
import jGameLib.ui2d.GameCanvas;
import jGameLib.ui2d.JGraphics;

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

    private List<UIRendererComponent> updateOrder = new ArrayList<>();
    private List<UIRendererComponent> drawOrder = new ArrayList<>();

    @Override
    public synchronized void applyAction(Collection<Entity> entities, GameState currentState) {
        recalculateProcessingOrders(entities);

        updateOrder.forEach(UIRendererComponent::update);

        // Tell the canvas to redraw itself
        canvas.repaint(this::redraw);
    }

    private synchronized void recalculateProcessingOrders(Collection<Entity> entities) {
        List<BoundingBoxComponent> boundingBoxes = new ArrayList<>();
        entities.stream()
            .map(i -> i.getComponent(BoundingBoxComponent.class))
            .forEach(boundingBoxes::add);

        // Update the entities
        updateOrder = new ArrayList<>();
        boundingBoxes.forEach(this::addToUpdateOrder);
        // Compute the draw order
        drawOrder = new ArrayList<>();
        addToDrawOrder(boundingBoxes);
        drawOrder.sort(Comparator.comparingDouble(i -> i.getBoundingBox().getRenderOrder()));
    }

    private synchronized void addToUpdateOrder(BoundingBoxComponent boundingBox) {
        if (boundingBox.getEntity().isEnabled() && boundingBox.isEnabled()) {
            // Update children first
            boundingBox.getChildren().forEach(this::addToUpdateOrder);
            // Update enabled components on self
            boundingBox.getEntity().getAllComponents(UIRendererComponent.class).stream()
                .filter(UIRendererComponent::isEnabled)
                .forEach(updateOrder::add);
        }
    }

    private synchronized void addToDrawOrder(Collection<BoundingBoxComponent> boundingBoxes) {
        List<BoundingBoxComponent> b = new ArrayList<>(boundingBoxes);
        // Do stuff
        b.stream()
            .filter(BoundingBoxComponent::isEnabled)
            .filter(i -> i.getEntity().isEnabled())
            .forEachOrdered((boundingBox) -> {
                // Add own renderer components to draw order
                boundingBox.getEntity().getAllComponents(UIRendererComponent.class).stream()
                    .filter(UIRendererComponent::isEnabled)
                    .forEach(drawOrder::add);
                // Add children to draw order
                addToDrawOrder(boundingBox.getChildren());
            });
    }

    private synchronized void redraw(JGraphics graphics) {
        drawOrder.forEach(component -> component.draw(graphics));
    }
}

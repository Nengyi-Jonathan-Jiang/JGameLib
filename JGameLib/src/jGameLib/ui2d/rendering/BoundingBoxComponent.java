package jGameLib.ui2d.rendering;

import jGameLib.core.Component;
import jGameLib.core.Entity;
import jGameLib.util.math.Vec2;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class BoundingBoxComponent extends Component {
    private Vec2 relativePosition, size;
    private double renderOrder = 0;
    private BoundingBoxComponent parent = null;
    private final LinkedHashSet<BoundingBoxComponent> children = new LinkedHashSet<>();

    private boolean isEnabled = true;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void enable() {
        this.isEnabled = true;
    }

    public void disable() {
        this.isEnabled = false;
    }

    public BoundingBoxComponent(Vec2 size) {
        this(Vec2.zero, size);
    }

    public BoundingBoxComponent(Vec2 relativePosition, Vec2 size) {
        this.relativePosition = relativePosition;
        this.size = size;
    }

    @Override
    protected void onEntityDestroyed() {
        // Remove self from parent
        if (parent != null) {
            parent.children.remove(this);
        }
        // Automatically destroy children
        children.stream().map(Component::getEntity).toList().forEach(Entity::destroy);
    }

    public void setRenderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
    }

    public double getRenderOrder() {
        double parentRenderOrder = parent == null ? 0 : parent.getRenderOrder();
        return renderOrder + parentRenderOrder;
    }

    public void removeFromParent() {
        parent.children.remove(this);
        setParent(null);
    }

    public BoundingBoxComponent removeChild(BoundingBoxComponent child) {
        if (children.contains(child)) {
            child.removeFromParent();
        }
        return this;
    }

    public BoundingBoxComponent addChild(BoundingBoxComponent object) {
        object.setParent(this);
        children.add(object);
        return this;
    }

    private void setParent(BoundingBoxComponent object) {
        if (parent != null) {
            object.children.remove(this);
        }
        parent = object;
    }

    public BoundingBoxComponent addChildren(BoundingBoxComponent... children) {
        Arrays.stream(children).forEach(this::addChild);
        return this;
    }

    public Collection<BoundingBoxComponent> getChildren() {
        return children;
    }

    public BoundingBoxComponent getParent() {
        return parent;
    }

    public boolean hasParent() {
        return getParent() != null;
    }

    private Vec2 getParentAbsolutePosition() {
        return parent == null ? Vec2.zero : parent.getAbsolutePosition();
    }

    /**
     * @return position relative to top left corner of screen
     */
    public Vec2 getAbsolutePosition() {
        return getParentAbsolutePosition().plus(relativePosition);
    }

    /**
     * Sets the absolute position of the ecs.GameObject
     */
    public BoundingBoxComponent setAbsolutePosition(Vec2 absolutePosition) {
        return setRelativePosition(absolutePosition.minus(getParentAbsolutePosition()));
    }

    /**
     * @return position of top left corner relative to the top left corner of the screen
     */
    public Vec2 getAbsoluteTopLeft() {
        return getAbsolutePosition().plus(getTopLeftOffset());
    }

    /**
     * @return position of top right corner relative to the top left corner of the screen
     */
    public Vec2 getAbsoluteTopRight() {
        return getAbsolutePosition().plus(getTopRightOffset());
    }

    /**
     * @return position of bottom left corner relative to the top left corner of the screen
     */
    public Vec2 getAbsoluteBottomLeft() {
        return getAbsolutePosition().plus(getBottomLeftOffset());
    }

    /**
     * @return position of bottom left corner relative to the top left corner of the screen
     */
    public Vec2 getAbsoluteBottomRight() {
        return getAbsolutePosition().plus(getBottomRightOffset());
    }

    /**
     * @return position relative to the parent gameObject
     */
    public Vec2 getRelativePosition() {
        return relativePosition;
    }

    /**
     * Set the position relative to the parent gameObject
     */
    public BoundingBoxComponent setRelativePosition(Vec2 relativePosition) {
        this.relativePosition = relativePosition;
        return this;
    }


    public BoundingBoxComponent setRelativePosition(double x, double y) {
        return setRelativePosition(new Vec2(x, y));
    }

    public BoundingBoxComponent setX(double x) {
        relativePosition = new Vec2(x, relativePosition.y);
        return this;
    }

    public BoundingBoxComponent setY(double y) {
        relativePosition = new Vec2(relativePosition.x, y);
        return this;
    }

    /**
     * Move the gameObject
     */
    public BoundingBoxComponent moveBy(Vec2 movement) {
        this.relativePosition = this.relativePosition.plus(movement);
        return this;
    }

    /**
     * @return position of top left corner relative to the parent gameObject
     */
    public Vec2 getTopLeft() {
        return relativePosition.plus(getTopLeftOffset());
    }

    /**
     * Set the top left corner relative to the parent gameObject
     */
    public BoundingBoxComponent setTopLeft(Vec2 position) {
        return setRelativePosition(position.minus(getTopLeftOffset()));
    }

    /**
     * @return position of top right corner relative to the parent gameObject
     */
    public Vec2 getTopRight() {
        return relativePosition.plus(getTopRightOffset());
    }

    /**
     * Set the top right corner relative to the parent gameObject
     */
    public BoundingBoxComponent setTopRight(Vec2 position) {
        return setRelativePosition(position.minus(getTopRightOffset()));
    }

    /**
     * @return position of bottom left corner relative to the parent gameObject
     */
    public Vec2 getBottomLeft() {
        return relativePosition.plus(getBottomLeftOffset());
    }

    /**
     * Set the bottom left corner relative to the parent gameObject
     */
    public BoundingBoxComponent setBottomLeft(Vec2 position) {
        return setRelativePosition(position.minus(getBottomLeftOffset()));
    }

    /**
     * @return position of bottom left corner relative to the parent gameObject
     */
    public Vec2 getBottomRight() {
        return relativePosition.plus(getBottomRightOffset());
    }

    /**
     * Set the bottom right corner relative to the parent gameObject
     */
    public BoundingBoxComponent setBottomRight(Vec2 position) {
        return setRelativePosition(position.minus(getBottomRightOffset()));
    }

    /**
     * Get offset of top left corner
     */
    public Vec2 getTopLeftOffset() {
        return size.times(-.5, -.5);
    }

    /**
     * Get offset of top right corner
     */
    public Vec2 getTopRightOffset() {
        return size.times(.5, -.5);
    }

    /**
     * Get offset of bottom left corner
     */
    public Vec2 getBottomLeftOffset() {
        return size.times(-.5, .5);
    }

    /**
     * Get offset of bottom right corner
     */
    public Vec2 getBottomRightOffset() {
        return size.times(.5, .5);
    }

    /**
     * @return the size of the ecs.GameObject in pixels
     */
    public Vec2 getSize() {
        return size;
    }

    public BoundingBoxComponent setWidth(double w) {
        return setSize(w, size.y);
    }

    public BoundingBoxComponent setHeight(double h) {
        return setSize(size.x, h);
    }

    /**
     * Set the size of the ecs.GameObject
     */
    public BoundingBoxComponent setSize(Vec2 size) {
        this.size = size;
        return this;
    }

    /**
     * Set the size of the ecs.GameObject
     */
    public BoundingBoxComponent setSize(double width, double height) {
        return setSize(new Vec2(width, height));
    }
}

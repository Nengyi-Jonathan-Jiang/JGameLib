package jGameLib.core;

import java.util.Objects;

/**
 * An abstract Component class.
 */
@SuppressWarnings("unused")
public abstract class Component {
    protected Entity entity;

    /**
     * A utility method for casting this entity to a more specific type of component,
     * for easier use when method chaining
     */
    public final <T extends Component> T cast() {
        //noinspection unchecked
        return (T) this;
    }

    /**
     * Sets the parent game object of this component
     */
    final void setEntity(Entity entity) {
        this.entity = entity;
        this.onSetEntity(entity);
    }

    /**
     * May be overridden to check conditions on the entity containing a component. Subclass implementations should
     * usually call their superclass implementations
     */
    protected void onSetEntity(Entity entity) {
    }

    /**
     * May be overridden to do cleanup when the containing {@link Entity} is {@link Entity#destroy() destroyed}.
     */
    protected void onEntityDestroyed() {}

    /**
     * Helper method to assert that the entity this component is attached to has an instance of the given component
     * type, and returns the component.
     */
    protected final <T extends Component> T assertEntityHasComponent(Class<T> cls) {
        if (!entity.hasComponent(cls)) {
            throw new RuntimeException("Entity must have component " + cls.getSimpleName());
        }
        return entity.getComponent(cls);
    }

    /**
     * Helper method to assert that the entity this component is attached to does not already have an instance of the
     * current component. You may pass in any class that is a superclass of the current component.
     */
    protected final void assertUniqueInEntity(Class<? extends Component> cls) {
        // Ensure that the current class is a subclass of the given class
        getClass().asSubclass(cls);
        if (entity.getAllComponents(cls).size() != 1) {
            throw new RuntimeException("Entity must have only one of " + cls.getSimpleName());
        }
    }

    /**
     * @return The {@link Entity} that the behavior is attached to
     */
    public final Entity getEntity() {
        return Objects.requireNonNull(entity);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
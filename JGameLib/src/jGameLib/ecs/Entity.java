package jGameLib.ecs;

import jGameLib.util.ToStringHelper;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Represents an object that has {@link Component Components} and can be acted upon by {@link JSystem JSystems}.
 * Entities stick around forever in a list in their containing state until they are explicitly
 * {@link Entity#destroy() destroyed}.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Entity {
    private final Map<Class<? extends Component>, List<Component>> componentMap;
    private final List<Component> componentList;
    private final GameState containingGameState;

    private boolean isEnabled = true;

    /**
     * Construct an {@link Entity}. Entities must be constructed when their containing
     * {@link GameState GameState} has control (see below) and is default constructed or explicitly constructed to use
     * the global state machine in order to be linked to the correct state. If you need to construct an entity
     * elsewhere, use the {@link Entity#Entity(GameState)} constructor instead.
     * <p> A game state is said to have control when: <ul>
     * <li> The constructor or instance initializers are running, and no other threads might construct game states </li>
     * <li>
     * {@link GameState#onSchedule()}, {@link GameState#onUpdate()}, {@link GameState#onExecutionStart()},
     * or {@link GameState#onExecutionEnd()} are running
     * </li>
     * <li>
     * A {@link JSystem} is running on the entities in the state.
     * </li>
     * <li>
     * {@link StateMachine#runMethodWithControl(GameState, Consumer)} is running
     * </li>
     * </ul> </p>
     */
    public Entity() {
        this(StateMachine.gameStateWithControl);
    }

    /**
     * Construct an {@link Entity} with its containing {@link GameState}. The containing state need not have control,
     * as opposed to the other constructor {@link Entity#Entity()}
     */
    public Entity(GameState state) {
        this.componentMap = new HashMap<>();
        this.componentList = new ArrayList<>();

        state.entitiesInState.add(this);
        containingGameState = state;
    }

    /**
     * Removes an {@link Entity} from its containing {@link GameState}. After this method is called, you should never
     * use this entity again, and the entity will not participate in any {@link JSystem} runs
     */
    public void destroy() {
        containingGameState.entitiesInState.remove(this);
    }

    /**
     * A utility method for casting to a subclass, for easier use when method chaining
     */
    public final <T extends Entity> T cast() {
        //noinspection unchecked
        return (T) this;
    }

    /**
     * A utility method for casting to a subclass, for easier use when method chaining
     */
    public final <T extends Entity> T cast(Class<T> cls) {
        return cast();
    }

    /**
     * Adds a {@link Component} to the {@link Entity}. You can later access the component through calling
     * {@link Entity#getComponent(Class)} or {@link Entity#getAllComponents(Class)} using the class of the component or
     * any of its superclasses up and including to the Component class
     */
    public final Entity addComponent(Component component) {
        componentList.add(component);
        addComponent(component, component.getClass());
        component.setEntity(this);
        return this;
    }

    private void addComponent(Component component, Class<? extends Component> cls) {
        if (cls == Component.class) return;
        componentMap.computeIfAbsent(cls, k -> new ArrayList<>()).add(component);
        //noinspection unchecked
        addComponent(component, (Class<? extends Component>) cls.getSuperclass());
    }

    /**
     * Adds multiple {@link Component Components} to the {@link Entity}. See {@link Entity#addComponent(Component)} for
     * more details
     */
    public final Entity addComponents(Component... components) {
        for (Component behavior : components) {
            addComponent(behavior);
        }
        return this;
    }

    /**
     * Returns a {@link Component} on this {@link Entity} that is an instance of the given class (including subclasses)
     * if one exists, otherwise throws an error. If multiple components exist, no guarantee is made on which is
     * returned; if you need to access all components of the given class, use {@link Entity#getAllComponents(Class)}
     */
    public final <Type extends Component> @NotNull Type getComponent(Class<Type> type) {
        Iterator<Type> it = getAllComponents(type).iterator();
        if (it.hasNext()) {
            return it.next();
        }
        throw new RuntimeException("No component of type " + type.getSimpleName() + " exists on entity");
    }

    /**
     * Returns a collection of all the {@link Component Components} on this entity that are instances of the given
     * class (including subclasses).
     */
    public final <Type extends Component> Collection<@NotNull Type> getAllComponents(Class<Type> type) {
        //noinspection unchecked
        return (Collection<Type>) componentMap.getOrDefault(type, Collections.emptyList());
    }

    /**
     * Checks whether an instance of the given {@link Component} class (including subclasses) exists on this entity
     */
    public final <Type extends Component> boolean hasComponent(Class<Type> type) {
        return componentMap.containsKey(type);
    }

    /**
     * Runs an action on the {@link Component} returned by {@link Entity#getComponent(Class)} and returns this entity.
     * Useful when method chaining.
     */
    public final <Type extends Component> Entity withComponent(Class<Type> type, Consumer<Type> action) {
        action.accept(getComponent(type));
        return this;
    }

    /**
     * Runs an action on each {@link Component} returned by {@link Entity#getAllComponents(Class)} and returns
     * this entity. Useful when method chaining.
     */
    public final <Type extends Component> Entity withAllComponents(Class<Type> type, Consumer<Type> action) {
        getAllComponents(type).forEach(action);
        return this;
    }

    /**
     * Checks whether this entity is enabled. If the entity is not enabled, it will not participate in any systems.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Enables the entity to participate in {@link JSystem JSystems}.
     */
    public void enable() {
        this.isEnabled = true;
    }

    /**
     * Disables the entity until {@link Entity#enable()} is called, preventing it from participating in any
     * {@link JSystem JSystems}
     */
    public void disable() {
        this.isEnabled = false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" + ToStringHelper.toMultiLineList(
            Stream.of(
                "Components: [" + ToStringHelper.toMultiLineList(componentList.stream()) + "]"
            )
        ) + "}";
    }
}
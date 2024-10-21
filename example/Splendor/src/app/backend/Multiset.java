package app.backend;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Multiset<T> implements Set<T> {
    private final Map<T, Integer> backingMap;
    private final Supplier<Map<T, Integer>> backingMapSupplier;

    public Multiset() {
        this(HashMap::new);
    }

    public Multiset(Collection<T> elements) {
        this(elements, HashMap::new);
    }

    public Multiset(Supplier<Map<T, Integer>> backingMapSupplier) {
        this.backingMapSupplier = backingMapSupplier;
        this.backingMap = backingMapSupplier.get();
    }

    public Multiset(Collection<T> elements, Supplier<Map<T, Integer>> backingMapSupplier) {
        this(backingMapSupplier);
        addAll(elements);
    }

    @Override
    public int size() {
        return backingMap.values().stream().reduce(Integer::sum).orElse(0);
    }

    @Override
    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        //noinspection SuspiciousMethodCalls
        return backingMap.containsKey(o);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return backingMap.entrySet().stream().flatMap(
            i -> Stream.iterate(i.getKey(), j -> j).limit(i.getValue())
        ).iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return toArray(new Object[0]);
    }

    @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1[] a) {
        if (size() <= a.length) {
            return toArray((i) -> a);
        }
        //noinspection unchecked
        return (T1[]) toArray(Object[]::new);
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        T1[] arr = generator.apply(size());
        int i = 0;
        for (T element : this) {
            //noinspection unchecked
            arr[i++] = (T1) element;
        }
        return arr;
    }

    @Override
    public boolean add(T t) {
        return updateCount(t, count(t) + 1);
    }

    private boolean updateCount(T o, int value) {
        boolean changed = count(o) != value;
        if (value <= 0) backingMap.remove(o);
        else backingMap.put(o, value);
        return changed;
    }

    public int count(T t) {
        return backingMap.getOrDefault(t, 0);
    }

    @Override
    public boolean remove(Object o) {
        //noinspection SuspiciousMethodCalls
        if (backingMap.containsKey(o)) {
            //noinspection unchecked
            return updateCount((T) o, count((T) o) - 1);
        }
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        if (!c.stream().allMatch(this::contains)) return false;
        // Check each key to ensure it has the right count
        Multiset<T> temp = new Multiset<>(this.backingMapSupplier);
        //noinspection unchecked
        temp.addAll((Collection<? extends T>) c);

        for (T key : temp) {
            if (temp.count(key) > count(key)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        Multiset<T> temp = new Multiset<>(this.backingMapSupplier);
        //noinspection unchecked
        c.stream().filter(this::contains).map(i -> (T) i).forEach(temp::add);

        boolean changed = false;
        for (T key : this.backingMap.keySet()) {
            changed |= updateCount(key, Math.min(temp.count(key), count(key)));
        }

        return changed;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override
    public void clear() {
        backingMap.clear();
    }
}

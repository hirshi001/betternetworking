package com.hirshi001.networking.network;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

public class ListenerHandler<L> implements Collection<L>{


    private final LinkedHashSet<L> listeners = new LinkedHashSet<>();

    public ListenerHandler() {
    }

    public ListenerHandler(Collection<L> listeners) {
        this.addAll(listeners);
    }

    protected void forEachListener(Consumer<L> consumer) {
        for (L listener : listeners) {
            consumer.accept(listener);
        }
    }

    @Override
    public int size() {
        return listeners.size();
    }

    @Override
    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return listeners.contains(o);
    }

    @Override
    public Iterator<L> iterator() {
        return listeners.iterator();
    }

    @Override
    public Object[] toArray() {
        return listeners.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return listeners.toArray(a);
    }

    @Override
    public boolean add(L l) {
        return listeners.add(l);
    }

    @Override
    public boolean remove(Object o) {
        return listeners.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return listeners.containsAll(c);
    }

    @Override
    public final boolean addAll(Collection<? extends L> c) {
        return listeners.addAll(c);
    }

    @SafeVarargs
    public final boolean addAll(L... listeners) {
        boolean modified = false;
        for (L listener : listeners)
            modified |= this.listeners.add(listener);
        return modified;
    }

    @Override
    public final boolean removeAll(Collection<?> c) {
        return listeners.removeAll(c);
    }

    @SafeVarargs
    public final boolean removeAll(L... listeners) {
       boolean modified = false;
       for (L listener : listeners)
           modified |= this.listeners.remove(listener);
       return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return listeners.retainAll(c);
    }

    @Override
    public void clear() {
        listeners.clear();
    }
}

/*
 * Copyright 2023 Hrishikesh Ingle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hirshi001.networking.network;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

/**
 * This class is used to manage listeners for events.
 * @param <L> the type of the listener
 *
 * @author Hrishikesh Ingle
 */
@SuppressWarnings("unused")
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
    public <T> T[] toArray(T @NotNull [] a) {
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
    public boolean containsAll(@NotNull Collection<?> c) {
        return listeners.containsAll(c);
    }

    @Override
    public final boolean addAll(@NotNull Collection<? extends L> c) {
        return listeners.addAll(c);
    }

    @SafeVarargs
    @SuppressWarnings("UnusedReturnValue")
    public final boolean addAll(L... listeners) {
        boolean modified = false;
        for (L listener : listeners)
            modified |= this.listeners.add(listener);
        return modified;
    }

    @Override
    public final boolean removeAll(@NotNull Collection<?> c) {
        return listeners.removeAll(c);
    }

    @SafeVarargs
    @SuppressWarnings("UnusedReturnValue")
    public final boolean removeAll(L... listeners) {
       boolean modified = false;
       for (L listener : listeners)
           modified |= this.listeners.remove(listener);
       return modified;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return listeners.retainAll(c);
    }

    @Override
    public void clear() {
        listeners.clear();
    }
}

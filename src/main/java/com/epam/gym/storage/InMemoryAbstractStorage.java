package com.epam.gym.storage;

import lombok.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryAbstractStorage<K, V> implements IStorage<K, V> {

    private final Map<K, V> storage = new ConcurrentHashMap<>();

    @Override
    public void put(@NonNull K key, @NonNull V value) {
        storage.put(key, value);
    }

    @Override
    public Optional<V> get(@NonNull K key) {
        return Optional.ofNullable(storage.get(key));
    }

    @Override
    public void remove(@NonNull K key) {
        storage.remove(key);
    }

    @Override
    public Collection<V> values() {
        return storage.values();
    }
}

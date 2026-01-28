package com.epam.gym.storage;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryAbstractStorage<K, V> {

    private final Map<K, V> storage = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        storage.put(key, value);
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(storage.get(key));
    }

    public void remove(K key) {
        storage.remove(key);
    }

    public Collection<V> values() {
        return storage.values();
    }
}

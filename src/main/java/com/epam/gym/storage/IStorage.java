package com.epam.gym.storage;

import java.util.Collection;
import java.util.Optional;

public interface IStorage<K, V> {

    void put(K key, V value);

    Optional<V> get(K key);

    void remove(K key);

    Collection<V> values();
}

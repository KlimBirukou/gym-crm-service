package com.epam.gym.storage;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

public interface IStorage<K, V> {

    void put(@NonNull K key,@NonNull V value);

    Optional<V> get(@NonNull K key);

    void remove(@NonNull K key);

    Collection<V> values();
}

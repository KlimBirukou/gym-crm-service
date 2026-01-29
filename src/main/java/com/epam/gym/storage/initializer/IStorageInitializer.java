package com.epam.gym.storage.initializer;

import org.springframework.core.io.Resource;

import java.util.List;

public interface IStorageInitializer<T> {

    List<T> load(Resource dataFile);
}

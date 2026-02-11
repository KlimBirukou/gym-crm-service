package com.epam.gym.storage.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.InMemoryAbstractStorage;
import com.epam.gym.storage.initializer.IStorageInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class InMemoryTrainingStorage extends InMemoryAbstractStorage<UUID, Training> {

    @Value("${storage.data.training.file:classpath:data/training.json}")
    private Resource trainingDataFile;

    @Autowired
    private IStorageInitializer<Training> storageInitializer;

    @PostConstruct
    void initialize() {
        storageInitializer.load(trainingDataFile)
            .forEach(training -> put(training.getUid(), training));
    }
}

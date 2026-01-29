package com.epam.gym.storage.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.InMemoryAbstractStorage;
import com.epam.gym.storage.initializer.IStorageInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class InMemoryTraineeStorage extends InMemoryAbstractStorage<UUID, Trainee> {

    @Value("${storage.data.trainee.file:classpath:data/trainee.json}")
    private Resource traineeDataFile;

    @Autowired
    private IStorageInitializer<Trainee> storageInitializer;

    @PostConstruct
    void initialize() {
        storageInitializer.load(traineeDataFile)
            .forEach(trainee -> put(trainee.getUid(), trainee));
    }
}

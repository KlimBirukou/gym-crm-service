package com.epam.gym.storage.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.storage.InMemoryAbstractStorage;
import com.epam.gym.storage.initializer.IStorageInitializer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class InMemoryTrainerStorage extends InMemoryAbstractStorage<UUID, Trainer> {

    @Value("${storage.data.trainer.file:classpath:data/trainer.json}")
    private Resource trainerDataFile;

    @Autowired
    private IStorageInitializer<Trainer> storageInitializer;

    @PostConstruct
    void initialize() {
        storageInitializer.load(trainerDataFile)
            .forEach(trainer -> put(trainer.getUid(), trainer));
    }
}

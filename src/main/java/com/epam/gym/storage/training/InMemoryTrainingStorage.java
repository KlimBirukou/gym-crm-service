package com.epam.gym.storage.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.storage.InMemoryAbstractStorage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class InMemoryTrainingStorage extends InMemoryAbstractStorage<UUID, Training> {
}

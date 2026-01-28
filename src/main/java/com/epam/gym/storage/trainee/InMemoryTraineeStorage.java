package com.epam.gym.storage.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.storage.InMemoryAbstractStorage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class InMemoryTraineeStorage extends InMemoryAbstractStorage<UUID, Trainee> {
}

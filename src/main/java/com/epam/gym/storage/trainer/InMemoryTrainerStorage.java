package com.epam.gym.storage.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.storage.InMemoryAbstractStorage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class InMemoryTrainerStorage extends InMemoryAbstractStorage<UUID, Trainer> {
}

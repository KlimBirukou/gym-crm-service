package com.epam.gym.storage;

import com.epam.gym.domain.Trainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TrainerStorage {

    private final Map<UUID, Trainer> storage = new HashMap<>();

    public Map<UUID, Trainer> getStorage() {
        return storage;
    }
}

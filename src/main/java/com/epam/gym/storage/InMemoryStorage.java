package com.epam.gym.storage;

import com.epam.gym.domain.user.Trainer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Component
public class InMemoryStorage {

    private final Map<UUID, Trainer> trainerStorage = new HashMap<>();
}

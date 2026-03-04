package com.epam.gym.mother;

import com.epam.gym.domain.user.Trainer;

import java.util.UUID;

public class TrainerMother {

    private static final String PASSWORD = "password";

    public static Trainer get(UUID uid, String firstName, String lastName, String username) {
        return Trainer.builder()
            .uid(uid)
            .firstName(firstName)
            .lastName(lastName)
            .username(username)
            .password(PASSWORD)
            .active(true)
            .build();
    }
}

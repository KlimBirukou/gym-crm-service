package com.epam.gym.mother;

import com.epam.gym.domain.user.Trainee;

import java.time.LocalDate;
import java.util.UUID;

public class TraineeMother {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "address";

    public static Trainee get(UUID uid, String firstName, String lastName, String username) {
        return Trainee.builder()
            .uid(uid)
            .firstName(firstName)
            .lastName(lastName)
            .address(ADDRESS)
            .username(username)
            .password(PASSWORD)
            .birthdate(DATE)
            .active(true)
            .build();
    }
}

package com.epam.gym.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public abstract class User {

    private UUID uid;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}

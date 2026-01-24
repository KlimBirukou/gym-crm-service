package com.epam.gym.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public abstract class User {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}

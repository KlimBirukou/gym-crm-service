package com.epam.gym.service.generator.name;

import lombok.NonNull;

public interface IUsernameGenerator {

    String generate(@NonNull String firstName, @NonNull String lastName);
}

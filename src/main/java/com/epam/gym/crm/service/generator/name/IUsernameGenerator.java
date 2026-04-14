package com.epam.gym.crm.service.generator.name;

import lombok.NonNull;

public interface IUsernameGenerator {

    String generate(@NonNull String firstName, @NonNull String lastName);
}

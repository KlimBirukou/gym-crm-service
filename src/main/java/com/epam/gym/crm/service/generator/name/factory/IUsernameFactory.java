package com.epam.gym.crm.service.generator.name.factory;

import lombok.NonNull;

public interface IUsernameFactory {

    String create(@NonNull String firstName, @NonNull String lastName);

    String create(@NonNull String firstName, @NonNull String lastName, int suffix);
}

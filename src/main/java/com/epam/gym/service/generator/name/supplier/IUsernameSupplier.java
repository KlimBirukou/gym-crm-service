package com.epam.gym.service.generator.name.supplier;

import lombok.NonNull;

import java.util.List;

public interface IUsernameSupplier {

    List<String> supply(@NonNull String firstName, @NonNull String lastName);
}

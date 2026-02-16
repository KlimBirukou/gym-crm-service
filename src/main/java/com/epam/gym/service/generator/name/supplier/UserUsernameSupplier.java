package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.UserProfile;
import lombok.NonNull;

import java.util.List;

public abstract class UserUsernameSupplier<T extends UserProfile> implements IUsernameSupplier {

    protected abstract List<T> provideUsers(@NonNull String firstName, @NonNull String lastName);

    @Override
    public final List<String> supply(@NonNull String firstName, @NonNull String lastName) {
        return provideUsers(firstName, lastName)
            .stream()
            .map(UserProfile::getUsername)
            .toList();
    }
}

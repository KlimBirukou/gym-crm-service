package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.User;
import lombok.NonNull;

import java.util.List;

public abstract class UserUsernameSupplier<T extends User> implements IUsernameSupplier {

    protected abstract List<T> provideUsers(String firstName, String lastName);

    @Override
    public final List<String> supply(@NonNull String firstName, @NonNull String lastName) {
        return provideUsers(firstName, lastName)
            .stream()
            .map(User::getUsername)
            .toList();
    }
}

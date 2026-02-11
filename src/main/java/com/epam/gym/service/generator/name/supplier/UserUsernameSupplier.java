package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.User;
import lombok.NonNull;

import java.util.List;

public abstract class UserUsernameSupplier<T> implements IUsernameSupplier {

    protected abstract List<T> provideUsers(@NonNull String firstName, @NonNull String lastName);

    protected abstract User extractUser(T entity);

    @Override
    public final List<String> supply(@NonNull String firstName, @NonNull String lastName) {
        return provideUsers(firstName, lastName)
            .stream()
            .map(this::extractUser)
            .map(User::getUsername)
            .toList();
    }
}

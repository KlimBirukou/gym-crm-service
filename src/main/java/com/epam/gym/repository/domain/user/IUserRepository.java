package com.epam.gym.repository.domain.user;

import com.epam.gym.domain.user.User;
import lombok.NonNull;

import java.util.Optional;

public interface IUserRepository {

    Optional<User> getByUsername(@NonNull String username);

    void save(@NonNull User user);

    void update(@NonNull User user);
}

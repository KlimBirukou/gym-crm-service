package com.epam.gym.repository.user.user;

import com.epam.gym.domain.user.User;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {

    boolean existsByUsername(@NonNull String username);

    Optional<User> findByUid(@NonNull UUID uid);

    Optional<User> findByUsername(@NonNull String username);

    void save(@NonNull User user);
}

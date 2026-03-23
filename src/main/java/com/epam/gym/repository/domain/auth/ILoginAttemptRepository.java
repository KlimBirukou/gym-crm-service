package com.epam.gym.repository.domain.auth;

import com.epam.gym.domain.auth.LoginAttempt;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface ILoginAttemptRepository {

    Optional<LoginAttempt> findByUserUid(@NonNull UUID userUid);

    void save(@NonNull LoginAttempt loginAttempt);

    void deleteByUserUid(@NonNull UUID userUid);
}

package com.epam.gym.repository.jpa.user;

import com.epam.gym.repository.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserEntityRepository extends JpaRepository<@NonNull UserEntity, @NonNull UUID> {

    Optional<UserEntity> findByUsername(@NonNull String username);
}

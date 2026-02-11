package com.epam.gym.repository.jpa.repository;

import com.epam.gym.repository.jpa.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserEntityRepository extends JpaRepository<@NonNull UserEntity, @NonNull UUID> {

    boolean existsByUsername(@NonNull String username);

    Optional<UserEntity> findByUsername(@NonNull String username);
}

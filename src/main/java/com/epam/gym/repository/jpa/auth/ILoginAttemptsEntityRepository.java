package com.epam.gym.repository.jpa.auth;

import com.epam.gym.repository.entity.LoginAttemptEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ILoginAttemptsEntityRepository extends JpaRepository<@NonNull LoginAttemptEntity, @NonNull UUID> {

}

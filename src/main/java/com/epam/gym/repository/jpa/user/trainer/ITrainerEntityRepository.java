package com.epam.gym.repository.jpa.user.trainer;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ITrainerEntityRepository extends JpaRepository<@NonNull TrainerEntity, @NonNull UUID> {

    List<TrainerEntity> findByUserFirstNameAndUserLastName(
        @NonNull String firstName,
        @NonNull String lastName
    );
}

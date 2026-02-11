package com.epam.gym.repository.jpa.repository;

import com.epam.gym.repository.jpa.entity.TraineeEntity;
import com.epam.gym.repository.jpa.entity.TrainerEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITraineeEntityRepository extends JpaRepository<@NonNull TraineeEntity, @NonNull UUID> {

    List<TraineeEntity> findByUserFirstNameAndUserLastName(
        @NonNull String firstName,
        @NonNull String lastName
    );

    Optional<TrainerEntity> findByUserUsername(@NonNull String username);

    void deleteById(@NonNull UUID uid);
}

package com.epam.gym.repository.jpa.user.trainee;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ITraineeEntityRepository extends JpaRepository<@NonNull TraineeEntity, @NonNull UUID> {

    List<TraineeEntity> findByUserFirstNameAndUserLastName(
        @NonNull String firstName,
        @NonNull String lastName
    );

    void deleteById(@NonNull UUID uid);
}

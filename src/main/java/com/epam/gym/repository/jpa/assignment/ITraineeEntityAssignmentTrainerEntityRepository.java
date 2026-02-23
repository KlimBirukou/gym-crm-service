package com.epam.gym.repository.jpa.assignment;

import com.epam.gym.repository.entity.TraineeTrainerEntity;
import com.epam.gym.repository.entity.TrainerEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ITraineeEntityAssignmentTrainerEntityRepository extends JpaRepository<@NonNull TraineeTrainerEntity, @NonNull UUID> {

    boolean existsByTraineeUserUsernameAndTrainerUserUsername(@NonNull String traineeUsername, @NonNull String trainerUsername);

    @Query("""
        SELECT tte.trainer FROM TraineeTrainerEntity tte
        JOIN FETCH tte.trainer.user
        WHERE tte.trainer.user.username = :username
        """)
    List<TrainerEntity> getAssignedTrainers(@Param("username") @NonNull String username);

    @Query("""
        SELECT tr FROM TrainerEntity tr
        JOIN FETCH tr.user
        WHERE tr.user.username NOT IN (
           SELECT tte.trainer.user.username FROM TraineeTrainerEntity tte
           WHERE tte.trainee.user.username = :username
        )
        """)
    List<TrainerEntity> getUnassignedTrainers(@Param("username") @NonNull String username);
}

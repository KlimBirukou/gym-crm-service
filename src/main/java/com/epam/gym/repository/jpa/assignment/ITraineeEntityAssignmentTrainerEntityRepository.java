package com.epam.gym.repository.jpa.assignment;

import com.epam.gym.repository.entity.TraineeEntity;
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
public interface ITraineeEntityAssignmentTrainerEntityRepository
    extends JpaRepository<@NonNull TraineeTrainerEntity, @NonNull UUID> {

    boolean existsByTraineeUserUsernameAndTrainerUserUsername(@NonNull String traineeUsername,
                                                              @NonNull String trainerUsername);

    @Query("""
        SELECT te
        FROM TraineeEntity te
        JOIN FETCH te.user u
        WHERE (
            (:assigned = TRUE
                AND EXISTS
                    (SELECT 1
                    FROM TraineeTrainerEntity tte
                    WHERE tte.trainee = te
                        AND tte.trainer.user.username = :trainerUsername))
            OR (:assigned = FALSE
                AND NOT EXISTS
                    (SELECT 1
                    FROM TraineeTrainerEntity tte
                    WHERE tte.trainer.user.username = :trainerUsername))
        )
        AND (:active = FALSE OR u.active = TRUE)
        """)
    List<TraineeEntity> getTrainees(
        @Param("trainerUsername") @NonNull String trainerUsername,
        @Param("assigned") @NonNull Boolean assigned,
        @Param("active") @NonNull Boolean active
    );

    @Query("""
        SELECT tr
        FROM TrainerEntity tr
        JOIN FETCH tr.user u
        JOIN FETCH tr.specialization
        WHERE (
            (:assigned = TRUE
                AND EXISTS
                    (SELECT 1
                    FROM TraineeTrainerEntity tte
                    WHERE tte.trainer = tr
                        AND tte.trainee.user.username = : traineeUsername))
            OR (:assigned = FALSE
                AND NOT EXISTS
                    (SELECT 1
                    FROM TraineeTrainerEntity tte
                    WHERE tte.trainer = tr
                        AND tte.trainee.user.username = :traineeUsername))
        )
        AND (:active = FALSE OR u.active = TRUE)
        """)
    List<TrainerEntity> getTrainers(
        @Param("traineeUsername") @NonNull String traineeUsername,
        @Param("assigned") @NonNull Boolean assigned,
        @Param("active") @NonNull Boolean active
    );
}

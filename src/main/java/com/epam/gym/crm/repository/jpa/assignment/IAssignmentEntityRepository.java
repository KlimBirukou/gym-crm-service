package com.epam.gym.crm.repository.jpa.assignment;

import com.epam.gym.crm.repository.entity.TraineeEntity;
import com.epam.gym.crm.repository.entity.TraineeTrainerEntity;
import com.epam.gym.crm.repository.entity.TrainerEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAssignmentEntityRepository
    extends JpaRepository<@NonNull TraineeTrainerEntity, @NonNull UUID> {

    boolean existsByTraineeUserUsernameAndTrainerUserUsername(@NonNull String traineeUsername,
                                                              @NonNull String trainerUsername);

    @Query("""
        SELECT te
        FROM TraineeEntity te
        JOIN FETCH te.user u
        LEFT JOIN TraineeTrainerEntity tte
            ON tte.trainee = te AND tte.trainer.user.username = :trainerUsername
        WHERE (
                (:assigned = TRUE AND tte.trainer IS NOT NULL)
                OR
                (:assigned = FALSE AND tte.trainer IS NULL)
            )
            AND
            u.active = :active
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
        LEFT JOIN TraineeTrainerEntity tte
            ON tte.trainer = tr AND tte.trainee.user.username = :traineeUsername
        WHERE (
                (:assigned = TRUE AND tte.trainee IS NOT NULL)
                OR
                (:assigned = FALSE AND tte.trainee IS NULL)
            )
            AND
            u.active = :active
        """)
    List<TrainerEntity> getTrainers(
        @Param("traineeUsername") @NonNull String traineeUsername,
        @Param("assigned") @NonNull Boolean assigned,
        @Param("active") @NonNull Boolean active
    );
}

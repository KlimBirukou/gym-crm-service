package com.epam.gym.repository.jpa.training;

import com.epam.gym.repository.entity.TrainingEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ITrainingEntityRepository extends JpaRepository<@NonNull TrainingEntity, @NonNull UUID> {

    @Query("""
        SELECT tg
        FROM TrainingEntity tg
            JOIN FETCH tg.trainingType
            JOIN FETCH tg.trainee
            JOIN FETCH tg.trainer tr
        WHERE tg.trainee.uid = :traineeUid
            AND (tg.date >= COALESCE(:from, tg.date))
            AND (tg.date <= COALESCE(:to, tg.date))
            AND (tr.user.username = COALESCE(:trainerUsername, tr.user.username))
            AND (tg.trainingType.name = COALESCE(:trainingTypeName, tg.trainingType.name))
        """)
    List<TrainingEntity> findTraineeTrainings(
        @Param("traineeUid") @NonNull UUID traineeUid,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to,
        @Param("trainerUsername") String trainerUsername,
        @Param("trainingTypeName") String trainingTypeName
    );

    @Query("""
        SELECT tg
        FROM TrainingEntity tg
            JOIN FETCH tg.trainingType
            JOIN FETCH tg.trainee te
            JOIN FETCH tg.trainer
        WHERE tg.trainer.uid = :trainerUid
            AND (tg.date >= COALESCE(:from, tg.date))
            AND (tg.date <= COALESCE(:to, tg.date))
            AND (te.user.username = COALESCE(:traineeUsername, te.user.username))
        """)
    List<TrainingEntity> findTrainerTrainings(
        @Param("trainerUid") @NonNull UUID trainerUid,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to,
        @Param("traineeUsername") String traineeUsername
    );

    @Query("""
        SELECT tg
        FROM TrainingEntity tg
            JOIN FETCH tg.trainingType
            JOIN FETCH tg.trainee
            JOIN FETCH tg.trainer
        WHERE tg.date = :date
        """)
    List<TrainingEntity> findByDate(@Param("date") @NonNull LocalDate date);
}

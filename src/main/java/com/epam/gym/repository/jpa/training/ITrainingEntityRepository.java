package com.epam.gym.repository.jpa.training;

import com.epam.gym.repository.entity.TrainingEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ITrainingEntityRepository extends JpaRepository<@NonNull TrainingEntity, @NonNull UUID> {

    @Query("""
        SELECT tg FROM TrainingEntity tg
        JOIN FETCH tg.trainingType
        JOIN FETCH tg.trainee
        JOIN FETCH tg.trainer
        WHERE tg.trainingType.uid = :trainingTypeUid
        AND tg.trainee.uid = :traineeUid
        """)
    List<TrainingEntity> findByTraineeUidAndTrainingTypeUid(
        @Param("traineeUid") @NonNull UUID traineeUid,
        @Param("trainingTypeUid") @NonNull UUID trainingTypeUid
    );

    @Query("""
        SELECT tg FROM TrainingEntity tg
        JOIN FETCH tg.trainingType
        JOIN FETCH tg.trainee
        JOIN FETCH tg.trainer
        WHERE tg.trainingType.uid = :trainingTypeUid
        AND tg.trainer.uid = :trainerUid
        """)
    List<TrainingEntity> findByTrainerUidAndTrainingTypeUid(
        @Param("trainerUid") @NonNull UUID trainerUid,
        @Param("trainingTypeUid") @NonNull UUID trainingTypeUid
    );
}

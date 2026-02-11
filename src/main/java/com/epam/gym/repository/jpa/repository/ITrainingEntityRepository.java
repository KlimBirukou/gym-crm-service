package com.epam.gym.repository.jpa.repository;

import com.epam.gym.repository.jpa.entity.TrainingEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ITrainingEntityRepository extends JpaRepository<@NonNull TrainingEntity, @NonNull UUID> {

    List<TrainingEntity> findByDate(@NonNull LocalDate date);

    List<TrainingEntity> findByTraineeUid(@NonNull UUID uid);
}

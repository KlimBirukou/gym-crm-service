package com.epam.gym.crm.repository.jpa.type;

import com.epam.gym.crm.repository.entity.TrainingTypeEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITrainingTypeEntityRepository extends JpaRepository<@NonNull TrainingTypeEntity, @NonNull UUID> {

    Optional<TrainingTypeEntity> getByName(@NonNull String name);
}

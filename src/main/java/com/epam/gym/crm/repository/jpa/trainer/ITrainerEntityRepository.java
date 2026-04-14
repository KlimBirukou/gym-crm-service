package com.epam.gym.crm.repository.jpa.trainer;

import com.epam.gym.crm.repository.entity.TrainerEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITrainerEntityRepository extends JpaRepository<@NonNull TrainerEntity, @NonNull UUID> {

    boolean existsByUserUsername(@NonNull String username);

    Optional<TrainerEntity> findByUserUsername(@NonNull String username);

    List<TrainerEntity> findByUserFirstNameAndUserLastName(@NonNull String firstName, @NonNull String lastName);

    List<TrainerEntity> findAllByUidIn(@NonNull List<UUID> uids);
}

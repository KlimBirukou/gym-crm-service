package com.epam.gym.crm.repository.jpa.trainee;

import com.epam.gym.crm.repository.entity.TraineeEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITraineeEntityRepository extends JpaRepository<@NonNull TraineeEntity, @NonNull UUID> {

    boolean existsByUserUsername(@NonNull String username);

    Optional<TraineeEntity> findByUserUsername(@NonNull String username);

    void deleteByUserUsername(@NonNull String username);

    List<TraineeEntity> findByUserFirstNameAndUserLastName(@NonNull String firstName, @NonNull String lastName);

    List<TraineeEntity> findAllByUidIn(@NonNull List<UUID> uids);
}

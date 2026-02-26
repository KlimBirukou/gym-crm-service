package com.epam.gym.repository.jpa.trainee;

import com.epam.gym.repository.entity.TraineeEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITraineeEntityRepository extends JpaRepository<@NonNull TraineeEntity, @NonNull UUID> {

    boolean existsByUserUsername(@NonNull String username);

    Optional<TraineeEntity> findByUserUsername(@NonNull String username);

    void deleteByUserUsername(@NonNull String username);

    @Query("""
            SELECT DISTINCT te FROM TraineeEntity te
            JOIN FETCH te.user
            JOIN te.trainers tr
            WHERE tr.trainer.uid = :trainerUid
        """)
    List<TraineeEntity> findByTrainerUid(@Param("trainerUid") @NonNull UUID trainerUid);

    List<TraineeEntity> findByUserFirstNameAndUserLastName(@NonNull String firstName, @NonNull String lastName);

    List<TraineeEntity> findAllByUidIn(@NonNull List<UUID> uids);
}

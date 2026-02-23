package com.epam.gym.repository.jpa.trainer;

import com.epam.gym.repository.entity.TrainerEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITrainerEntityRepository extends JpaRepository<@NonNull TrainerEntity, @NonNull UUID> {

    boolean existsByUserUsername(@NonNull String username);

    Optional<TrainerEntity> findByUserUsername(@NonNull String username);

    void deleteByUserUsername(@NonNull String username);

    @Query("""
        SELECT DISTINCT tr FROM TrainerEntity tr
        JOIN FETCH tr.user
        JOIN FETCH tr.specialization
        JOIN tr.trainees te
        WHERE te.trainee.uid = :traineeUid
        """)
    List<TrainerEntity> findByTraineeUid(@Param("traineeUid") @NonNull UUID trainerUid);

    List<TrainerEntity> findByUserFirstNameAndUserLastName(@NonNull String firstName, @NonNull String lastName);
}

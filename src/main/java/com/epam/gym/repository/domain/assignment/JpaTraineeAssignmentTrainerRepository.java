package com.epam.gym.repository.domain.assignment;

import com.epam.gym.repository.entity.TraineeTrainerEntity;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.jpa.assignment.ITraineeEntityAssignmentTrainerEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaTraineeAssignmentTrainerRepository implements ITraineeAssignmentTrainerRepository {

    private final ITraineeEntityAssignmentTrainerEntityRepository repository;
    private final ConversionService conversionService;

    @Override
    public void assign(@NonNull Trainee trainee, @NonNull Trainer trainer) {
        var entity = TraineeTrainerEntity.builder()
            .build();
    }

    @Override
    public boolean checkAssign(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        return repository.existsByTraineeUserUsernameAndTrainerUserUsername(traineeUsername, trainerUsername);
    }

    @Override
    public List<Trainer> getAssignedTrainers(@NonNull String username) {
        return repository.getAssignedTrainers(username).stream()
            .map(entity -> conversionService.convert(entity, Trainer.class))
            .toList();
    }

    @Override
    public List<Trainer> getUnassignedTrainers(@NonNull String username) {
        return repository.getUnassignedTrainers(username).stream()
            .map(entity -> conversionService.convert(entity, Trainer.class))
            .toList();
    }
}

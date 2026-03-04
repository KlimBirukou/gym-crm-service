package com.epam.gym.repository.domain.assignment;

import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.repository.entity.TraineeTrainerEntity;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.repository.jpa.assignment.IAssignmentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaAssignmentRepository implements com.epam.gym.repository.domain.assignment.IAssignmentRepository {

    private final IAssignmentRepository repository;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public void assign(@NonNull Trainee trainee, @NonNull Trainer trainer) {
        var entity = TraineeTrainerEntity.builder()
            .trainee(conversionService.convert(trainee, TraineeEntity.class))
            .trainer(conversionService.convert(trainer, TrainerEntity.class))
            .build();
        repository.save(entity);
    }

    @Override
    @Transactional
    public boolean checkAssign(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        return repository.existsByTraineeUserUsernameAndTrainerUserUsername(traineeUsername, trainerUsername);
    }

    @Override
    @Transactional
    public List<Trainee> getTrainees(@NonNull String trainerUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        return repository.getTrainees(trainerUsername, assigned, active)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainee.class))
            .toList();
    }

    @Override
    @Transactional
    public List<Trainer> getTrainers(@NonNull String traineeUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        return repository.getTrainers(traineeUsername, assigned, active)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainer.class))
            .toList();
    }
}

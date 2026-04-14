package com.epam.gym.crm.repository.domain.assignment;

import com.epam.gym.crm.repository.entity.TraineeEntity;
import com.epam.gym.crm.repository.entity.TraineeTrainerEntity;
import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.repository.entity.TrainerEntity;
import com.epam.gym.crm.repository.jpa.assignment.IAssignmentEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaAssignmentRepository implements IAssignmentRepository {

    private final IAssignmentEntityRepository repository;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public void assign(@NonNull Trainee trainee, @NonNull Trainer trainer) {
        repository.save(TraineeTrainerEntity.builder()
            .trainee(conversionService.convert(trainee, TraineeEntity.class))
            .trainer(conversionService.convert(trainer, TrainerEntity.class))
            .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAssign(@NonNull String traineeUsername, @NonNull String trainerUsername) {
        return repository.existsByTraineeUserUsernameAndTrainerUserUsername(traineeUsername, trainerUsername);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getTrainees(@NonNull String trainerUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        return repository.getTrainees(trainerUsername, assigned, active)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainee.class))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getTrainers(@NonNull String traineeUsername,
                                     @NonNull Boolean assigned,
                                     @NonNull Boolean active) {
        return repository.getTrainers(traineeUsername, assigned, active)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainer.class))
            .toList();
    }
}

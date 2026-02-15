package com.epam.gym.repository.domain.training;

import com.epam.gym.repository.entity.TrainingEntity;
import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.jpa.training.ITrainingEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaTrainingRepository implements ITrainingRepository {

    private final ITrainingEntityRepository repository;
    private final ConversionService conversionService;

    @Override
    public void save(Training training) {
        var entity = conversionService.convert(training, TrainingEntity.class);
        repository.save(entity);
    }

    @Override
    public List<Training> getTraineeTrainings(@NonNull UUID traineeUid, @NonNull UUID trainingTypeUid) {
        return  repository.findByTraineeUidAndTrainingTypeUid(traineeUid, trainingTypeUid).stream()
            .map(entity -> conversionService.convert(entity, Training.class))
            .toList();
    }

    @Override
    public List<Training> getTrainerTrainings(@NonNull UUID trainerUid, @NonNull UUID trainingTypeUid) {
        return  repository.findByTrainerUidAndTrainingTypeUid(trainerUid, trainingTypeUid).stream()
            .map(entity -> conversionService.convert(entity, Training.class))
            .toList();
    }

    @Override
    public List<Training> getTrainingsOnDate(@NonNull LocalDate date) {
        return List.of();
    }
}

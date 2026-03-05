package com.epam.gym.repository.domain.training;

import com.epam.gym.repository.entity.TrainingEntity;
import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.jpa.training.ITrainingEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void save(@NonNull Training training) {
        var entity = conversionService.convert(training, TrainingEntity.class);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(@NonNull UUID traineeUid,
                                              LocalDate from,
                                              LocalDate to,
                                              String traineeUsername,
                                              String trainingTypeName) {
        return repository.findTraineeTrainings(traineeUid, from, to, traineeUsername, trainingTypeName)
            .stream()
            .map(entity -> conversionService.convert(entity, Training.class))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(@NonNull UUID trainerUid,
                                              LocalDate from,
                                              LocalDate to,
                                              String traineeUsername) {
        return repository.findTrainerTrainings(trainerUid, from, to, traineeUsername)
            .stream()
            .map(entity -> conversionService.convert(entity, Training.class))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsOnDate(@NonNull LocalDate date) {
        return repository.findByDate(date)
            .stream()
            .map(entity -> conversionService.convert(entity, Training.class))
            .toList();
    }
}

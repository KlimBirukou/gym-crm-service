package com.epam.gym.repository.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.jpa.training.ITrainingEntityRepository;
import com.epam.gym.repository.jpa.training.TrainingEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaTrainingRepository implements ITrainingRepository {

    private final ITrainingEntityRepository trainingEntityRepository;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public void save(@NonNull Training training) {
        var entity = conversionService.convert(training, TrainingEntity.class);
        if (Objects.isNull(entity)) {
            throw new IllegalStateException("Failed conversion Trainee to TraineeEntity");
        }
        trainingEntityRepository.save(entity);
    }

    @Override
    @Transactional
    public List<Training> findByDate(@NonNull LocalDate date) {
        return trainingEntityRepository.findByDate(date)
            .stream()
            .map(entity -> conversionService.convert(entity, Training.class))
            .toList();
    }

    @Override
    @Transactional
    public List<Training> findByTraineeUid(@NonNull UUID uid) {
        return trainingEntityRepository.findByTraineeUid(uid)
            .stream()
            .map(entity -> conversionService.convert(entity, Training.class))
            .toList();
    }
}

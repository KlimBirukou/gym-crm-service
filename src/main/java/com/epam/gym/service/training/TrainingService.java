package com.epam.gym.service.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.training.ITrainingRepository;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.validator.IValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainingService
    implements ITrainingService {

    private final ITrainingRepository trainingRepository;
    @Qualifier("createTrainingValidator")
    private final IValidator<CreateTrainingDto> createTrainingValidator;
    @Qualifier("trainingDateValidator")
    private final IValidator<LocalDate> trainingDateValidator;

    @Override
    public Training create(@NonNull CreateTrainingDto dto) {
        createTrainingValidator.validate(dto);
        var training = Training.builder()
            .uid(UUID.randomUUID())
            .trainerUid(dto.trainerUid())
            .traineeUid(dto.traineeUid())
            .date(dto.date())
            .name(dto.name())
            .type(dto.type())
            .duration(dto.duration())
            .build();
        trainingRepository.save(training);
        return training;
    }

    @Override
    public List<Training> findAllByDate(@NonNull LocalDate date) {
        trainingDateValidator.validate(date);
        return trainingRepository.findByLocalDate(date);
    }
}

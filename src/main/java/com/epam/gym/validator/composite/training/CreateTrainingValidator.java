package com.epam.gym.validator.composite.training;

import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.validator.IValidator;
import com.epam.gym.validator.base.AbstractCompositeValidator;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service("createTrainingValidator")
@RequiredArgsConstructor
public class CreateTrainingValidator
    extends AbstractCompositeValidator<CreateTrainingDto>
    implements IValidator<CreateTrainingDto> {

    @Qualifier("trainingDateValidator")
    private final IValidator<LocalDate> trainingDateValidator;
    @Qualifier("traineePersistenceValidator")
    private final IValidator<UUID> traineePersistenceValidator;
    @Qualifier("trainerPersistenceValidator")
    private final IValidator<UUID> trainerPersistenceValidator;
    @Qualifier("userAvailabilityOnDateValidator")
    private final IValidator<CreateTrainingDto> userAvailabilityOnDateValidator;

    @Getter(AccessLevel.PROTECTED)
    private List<IValidator<CreateTrainingDto>> validators;

    @PostConstruct
    void setUpValidator() {
        validators = List.of(
            dto -> trainingDateValidator.validate(dto.date()),
            dto -> traineePersistenceValidator.validate(dto.traineeUid()),
            dto -> trainerPersistenceValidator.validate(dto.trainerUid()),
            userAvailabilityOnDateValidator

        );
    }
}

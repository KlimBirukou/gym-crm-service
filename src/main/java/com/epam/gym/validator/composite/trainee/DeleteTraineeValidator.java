package com.epam.gym.validator.composite.trainee;

import com.epam.gym.validator.IValidator;
import com.epam.gym.validator.base.AbstractCompositeValidator;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("deleteTraineeValidator")
@RequiredArgsConstructor
public class DeleteTraineeValidator
    extends AbstractCompositeValidator<UUID>
    implements IValidator<UUID> {

    @Qualifier("traineePersistenceValidator")
    private final IValidator<UUID> traineePersistenceValidator;
    @Qualifier("traineeHasTrainingValidator")
    private final IValidator<UUID> traineeHasTrainingValidator;

    @Getter(AccessLevel.PROTECTED)
    private List<IValidator<UUID>> validators;

    @PostConstruct
    void setUpValidator() {
        validators = List.of(
            traineePersistenceValidator,
            traineeHasTrainingValidator
        );
    }
}

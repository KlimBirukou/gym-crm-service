package com.epam.gym.validator.single.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.EntityBusyOnDateException;
import com.epam.gym.repository.domain.training.ITrainingRepository;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.validator.IValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/*@Service("userAvailabilityOnDateValidator")
@RequiredArgsConstructor
public class UserAvailabilityOnDateValidator
    implements IValidator<CreateTrainingDto> {

    private final ITrainingRepository trainingRepository;

    @Override
    public void validate(@NonNull CreateTrainingDto target) {
        trainingRepository.findByDate(target.date())
            .forEach(training -> validateTraining(target, training));
    }

    private static void validateTraining(CreateTrainingDto target, Training training) {
        validateUid(training.getTraineeUid(), target.traineeUid(), Trainee.class.getSimpleName(), target);
        validateUid(training.getTrainerUid(), target.trainerUid(), Trainer.class.getSimpleName(), target);
    }

    private static void validateUid(UUID userUid, UUID targetUid, String simpleName, CreateTrainingDto dto) {
        if (Objects.equals(userUid, targetUid)) {
            throw new EntityBusyOnDateException(simpleName, dto.trainerUid(), dto.date());
        }
    }
}*/

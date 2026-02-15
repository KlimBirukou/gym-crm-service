package com.epam.gym.validator.single.training;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.domain.training.ITrainingRepository;
import com.epam.gym.validator.IValidator;
import com.epam.gym.validator.base.AbstractDomainPersistenceValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;

/*@Service("traineeHasTrainingValidator")
public class TraineeHasTrainingValidator
    extends AbstractDomainPersistenceValidator
    implements IValidator<UUID> {

    public TraineeHasTrainingValidator(ITrainingRepository trainingRepository) {
        super(
            uid -> !trainingRepository.findByTraineeUid(uid).isEmpty(),
            Trainee.class.getSimpleName()
        );
    }
}*/

package com.epam.gym.validator.single.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.domain.trainer.ITrainerRepository;
import com.epam.gym.validator.IValidator;
import com.epam.gym.validator.base.AbstractDomainPersistenceValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;

/*@Service("trainerPersistenceValidator")
public class TrainerPersistenceValidator
    extends AbstractDomainPersistenceValidator
    implements IValidator<UUID> {

    public TrainerPersistenceValidator(ITrainerRepository trainerRepository) {
        super(
            trainerRepository::existByUid,
            Trainer.class.getSimpleName()
        );
    }
}*/

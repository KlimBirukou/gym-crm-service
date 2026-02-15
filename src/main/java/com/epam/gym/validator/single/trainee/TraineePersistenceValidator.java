package com.epam.gym.validator.single.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.domain.trainee.ITraineeRepository;
import com.epam.gym.validator.IValidator;
import com.epam.gym.validator.base.AbstractDomainPersistenceValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;

/*@Service("traineePersistenceValidator")
public class TraineePersistenceValidator
    extends AbstractDomainPersistenceValidator
    implements IValidator<UUID> {

    public TraineePersistenceValidator(ITraineeRepository traineeRepository) {
        super(
            traineeRepository::existByUid,
            Trainee.class.getSimpleName()
        );
    }
}*/

package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.trainee.ITraineeRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class TraineeUsernameSupplier extends UserUsernameSupplier<Trainee> {

    private ITraineeRepository traineeRepository;

    @Override
    protected List<Trainee> provideUsers(@NonNull String firstName, @NonNull String lastName) {
        return traineeRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Autowired
    public void setTraineeRepository(ITraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }
}

package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.trainee.ITraineeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class TraineeUsernameSupplier extends UserUsernameSupplier<Trainee> {

    private final ITraineeRepository traineeRepository;

    @Override
    protected List<Trainee> provideUsers(@NonNull String firstName, @NonNull String lastName) {
        return traineeRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}

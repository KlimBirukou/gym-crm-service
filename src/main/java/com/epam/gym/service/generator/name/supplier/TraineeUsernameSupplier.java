package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.User;
import com.epam.gym.repository.user.trainee.ITraineeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraineeUsernameSupplier extends UserUsernameSupplier<Trainee> {

    private final ITraineeRepository traineeRepository;

    @Override
    @Transactional
    protected List<Trainee> provideUsers(@NonNull String firstName, @NonNull String lastName) {
        return traineeRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    protected User extractUser(Trainee trainee) {
        return trainee.getUser();
    }
}

package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.domain.user.User;
import com.epam.gym.repository.user.trainer.ITrainerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class TrainerUsernameSupplier extends UserUsernameSupplier<Trainer> {

    private final ITrainerRepository trainerRepository;

    @Override
    @Transactional
    protected List<Trainer> provideUsers(@NonNull String firstName, @NonNull String lastName) {
        return trainerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    protected User extractUser(Trainer trainer) {
        return trainer.getUser();
    }
}

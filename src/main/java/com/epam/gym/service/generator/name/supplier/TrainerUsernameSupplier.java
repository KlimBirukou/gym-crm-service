package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class TrainerUsernameSupplier extends UserUsernameSupplier<Trainer> {

    private final ITrainerRepository trainerRepository;

    @Override
    protected List<Trainer> provideUsers(@NonNull String firstName, @NonNull String lastName) {
        return trainerRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}

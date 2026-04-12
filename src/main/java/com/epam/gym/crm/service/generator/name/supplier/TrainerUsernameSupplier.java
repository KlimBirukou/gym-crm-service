package com.epam.gym.crm.service.generator.name.supplier;

import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.repository.domain.trainer.ITrainerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerUsernameSupplier extends UserUsernameSupplier<Trainer> {

    private final ITrainerRepository trainerRepository;

    @Override
    @Transactional
    protected List<Trainer> provideUsers(@NonNull String firstName, @NonNull String lastName) {
        return trainerRepository.getByFirstNameAndLastName(firstName, lastName);
    }
}

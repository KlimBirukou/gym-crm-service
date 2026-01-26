package com.epam.gym.service.generator.name.supplier;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class TrainerUsernameSupplier extends UserUsernameSupplier<Trainer> {

    private ITrainerRepository trainerRepository;

    @Override
    protected List<Trainer> provideUsers(@NonNull String firstName, @NonNull String lastName) {
        return trainerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Autowired
    public void setTrainerRepository(ITrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }
}

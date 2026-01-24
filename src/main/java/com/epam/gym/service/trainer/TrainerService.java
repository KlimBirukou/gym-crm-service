package com.epam.gym.service.trainer;

import com.epam.gym.domain.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.service.name.IUsernameGenerator;
import com.epam.gym.service.password.IPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class TrainerService implements ITrainerService {

    private IUsernameGenerator usernameGenerator;
    private IPasswordGenerator passwordGenerator;
    private ITrainerRepository trainerRepository;

    @Override
    public Trainer create(CreateTrainerDto dto) {
        var trainer = Trainer.builder()
            .uid(UUID.randomUUID())
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .specialization(dto.specialization())
            .username(usernameGenerator.generate(dto.firstName(), dto.lastName()))
            .password(passwordGenerator.generate())
            .isActive(true)
            .build();
        trainerRepository.save(trainer);
        return trainer;
    }

    @Override
    public void update(Trainer trainer) {

        //TODO need full realization
    }

    @Autowired
    public void setUsernameGenerator(IUsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setPasswordGenerator(IPasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Autowired
    public void setTrainerRepository(ITrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }
}

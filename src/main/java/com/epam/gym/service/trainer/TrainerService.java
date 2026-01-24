package com.epam.gym.service.trainer;

import com.epam.gym.domain.Trainer;
import com.epam.gym.service.name.IUsernameGenerator;
import com.epam.gym.service.password.IPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public final class TrainerService implements ITrainerService {

    private IUsernameGenerator usernameGenerator;
    private IPasswordGenerator passwordGenerator;

    @Override
    public UUID create(CreateTrainerDto dto) {
        var alreadyExistingTrainers = getByFirstLastName(dto.firstName(), dto.lastName());
        var baseUsername = usernameGenerator.generate(dto.firstName(), dto.lastName());
        var username = alreadyExistingTrainers.isEmpty()
            ? baseUsername
            : baseUsername + alreadyExistingTrainers.size();
        var candidate = Trainer.builder()
            .uid(UUID.randomUUID())
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .username(username)
            .password(passwordGenerator.generate())
            .isActive(false)
            .build();

        // TODO need impl saving logic
        return null;
    }

    @Override
    public void update(Trainer trainer) {

        //TODO need full realization
    }

    @Override
    public List<Trainer> getByFirstLastName(String firstName, String lastName) {

        //TODO ned full realization
        return List.of();
    }

    @Autowired
    public void setUsernameGenerator(IUsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
    }

    @Autowired
    public void setPasswordGenerator(IPasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }
}

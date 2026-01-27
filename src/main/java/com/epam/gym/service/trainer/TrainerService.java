package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public final class TrainerService implements ITrainerService {

    private IUsernameGenerator usernameGenerator;
    private IPasswordGenerator passwordGenerator;
    private ITrainerRepository trainerRepository;

    @Override
    public Trainer create(@NonNull CreateTrainerDto dto) {
        log.debug("Creating trainer with firstName = {}, lastName = {}", dto.firstName(), dto.lastName());
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
        log.debug("Trainer created with uid = {}", trainer.getUid());
        return trainer;
    }

    @Override
    public void update(@NonNull UpdateTrainerDto dto) {
        log.debug("Updating trainer uid = {}", dto.uid());
        var trainer = trainerRepository.findByUid(dto.uid())
            .orElseThrow(() -> new RuntimeException("Trainer not found"));
        trainer.setSpecialization(dto.specialization());
        trainerRepository.save(trainer);
        log.debug("Trainer with uid = {} updated", trainer.getUid());
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

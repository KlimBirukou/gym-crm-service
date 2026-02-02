package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class TrainerService
    implements ITrainerService {

    private final IUsernameGenerator usernameGenerator;
    private final IPasswordGenerator passwordGenerator;
    private final ITrainerRepository trainerRepository;

    @Override
    public Trainer create(@NonNull CreateTrainerDto dto) {
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
    public void update(@NonNull UpdateTrainerDto dto) {
        var trainer = trainerRepository.findByUid(dto.uid())
            .orElseThrow(() -> new DomainNotFoundException(Trainee.class.getSimpleName(), dto.uid()));
        trainer.setSpecialization(dto.specialization());
        trainerRepository.save(trainer);
    }
}

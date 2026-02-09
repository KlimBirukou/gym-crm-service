package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.user.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService implements ITrainerService {

    private final IUsernameGenerator usernameGenerator;
    private final IPasswordGenerator passwordGenerator;
    private final ITrainerRepository trainerRepository;

    @Override
    @Transactional
    public Trainer create(@NonNull CreateTrainerDto dto) {
        var uid = UUID.randomUUID();
        var username = usernameGenerator.generate(dto.firstName(), dto.lastName());
        var password = passwordGenerator.generate();
        var trainer = Trainer.builder()
            .uid(uid)
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .specialization(dto.specialization())
            .username(username)
            .password(password)
            .active(true)
            .build();
        trainerRepository.save(trainer);
        return trainer;
    }

    @Override
    @Transactional
    public void update(@NonNull UpdateTrainerDto dto) {
        var trainer = trainerRepository.findByUid(dto.uid())
            .orElseThrow(() -> new DomainNotFoundException(Trainer.class.getSimpleName(), dto.uid()))
            .setSpecialization(dto.specialization());
        trainerRepository.save(trainer);
    }
}

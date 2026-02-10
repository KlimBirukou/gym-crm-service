package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.user.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.ChangePasswordDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.ToggleStatusDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
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
            .orElseThrow(() -> new DomainNotFoundException(Trainer.class.getSimpleName(), dto.uid().toString()));
        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainer.setUsername(dto.username());
        trainer.setSpecialization(dto.specialization());
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public Trainer findByUsername(@NonNull String username) {
        return trainerRepository.findByUsername(username)
            .orElseThrow(() -> new DomainNotFoundException(Trainer.class.getSimpleName(), username));
    }

    @Override
    @Transactional
    public void changePassword(@NonNull ChangePasswordDto dto) {
        var trainer = trainerRepository.findByUsername(dto.username())
            .orElseThrow(() -> new DomainNotFoundException(Trainer.class.getSimpleName(), dto.username()));
        if (!Objects.equals(trainer.getPassword(), dto.oldPassword())) {
            throw new RuntimeException(); // TODO create normal exception
        }
        trainer.setPassword(dto.newPassword());
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public void toggleStatus(@NonNull ToggleStatusDto dto) {
        var trainer = trainerRepository.findByUid(dto.uid())
            .orElseThrow(() -> new DomainNotFoundException(Trainer.class.getSimpleName(), dto.uid().toString()));
        trainer.setActive(dto.status());
        trainerRepository.save(trainer);
    }
}

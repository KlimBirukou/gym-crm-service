package com.epam.gym.service.trainer;

import com.epam.gym.exception.AuthException;
import com.epam.gym.exception.not.found.TrainerNotFoundException;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.domain.trainer.ITrainerRepository;
import com.epam.gym.service.trainer.dto.ChangePasswordDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.type.ITrainingTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService implements ITrainerService {

    private final IPasswordGenerator passwordGenerator;
    private final IUsernameGenerator usernameGenerator;
    private final ITrainerRepository trainerRepository;
    private final ITrainingTypeService trainingTypeService;
    private final IPasswordService passwordService;

    @Override
    @Transactional
    public Trainer create(@NonNull CreateTrainerDto dto) {
        var trainer = Trainer.builder()
            .uid(UUID.randomUUID())
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .username(usernameGenerator.generate(dto.firstName(), dto.lastName()))
            .password(passwordService.hashPassword(passwordGenerator.generate()))
            .specialization(trainingTypeService.getByName(dto.specialization()))
            .active(true)
            .build();
        trainerRepository.save(trainer);
        return trainer;
    }

    @Override
    @Transactional
    public void update(@NonNull UpdateTrainerDto dto) {
        var trainer = getByUsername(dto.username());
        trainer.setFirstName(dto.firstName());
        trainer.setLastName(dto.lastName());
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public void changePassword(@NonNull ChangePasswordDto dto) {
        var trainer = getByUsername(dto.username());
        if (!passwordService.checkPassword(dto.oldPassword(), trainer.getPassword())) {
            throw new AuthException();
        }
        trainer.setPassword(passwordService.hashPassword(dto.newPassword()));
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public void toggleStatus(@NonNull String username) {
        var trainer = getByUsername(username);
        trainer.toggleActive();
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getByUsername(@NonNull String username) {
        return trainerRepository.getByUsername(username)
            .orElseThrow(() -> new TrainerNotFoundException(username));
    }
}

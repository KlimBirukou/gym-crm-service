package com.epam.gym.service.trainer;

import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.domain.trainer.ITrainerRepository;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.dto.ChangePasswordDto;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.type.ITrainingTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrainerService implements ITrainerService {

    private final IPasswordGenerator passwordGenerator;
    private final IUsernameGenerator usernameGenerator;
    private final ITrainerRepository trainerRepository;
    private final ITrainingTypeService trainingTypeService;

    @Override
    @Transactional
    public Trainer create(@NonNull CreateTrainerDto dto) {
        var uid = UUID.randomUUID();
        var password = passwordGenerator.generate();
        var username = usernameGenerator.generate(dto.firstName(), dto.lastName());
        var type = trainingTypeService.getByName(dto.specialization());
        var trainer = Trainer.builder()
            .uid(uid)
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .username(username)
            .password(password)
            .specialization(type)
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
        if (Objects.equals(trainer.getPassword(), dto.oldPassword())) {
            throw new RuntimeException("Wrong password");
        }
        trainer.setPassword(dto.newPassword());
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional
    public void toggleStatus(@NonNull String username) {
        var trainer = getByUsername(username);
        trainer.setActive(!trainer.isActive());
        trainerRepository.save(trainer);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getByUsername(String username) {
        return trainerRepository.getByUsername(username)
            .orElseThrow(() -> new RuntimeException("Trainer %s not found".formatted(username)));
    }
}

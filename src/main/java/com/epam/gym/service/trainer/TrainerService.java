package com.epam.gym.service.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.domain.user.User;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.user.trainer.ITrainerRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.user.IUserService;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import com.epam.gym.service.user.dto.ToggleStatusDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService implements ITrainerService {

    private final IUsernameGenerator usernameGenerator;
    private final IPasswordGenerator passwordGenerator;
    private final ITrainerRepository trainerRepository;
    private final IUserService userService;

    @Override
    @Transactional
    public Trainer create(@NonNull CreateTrainerDto dto) {
        var uid = UUID.randomUUID();
        var username = usernameGenerator.generate(dto.firstName(), dto.lastName());
        var password = passwordGenerator.generate();
        var user = User.builder()
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .username(username)
            .password(password)
            .active(true)
            .build();
        var trainer = Trainer.builder()
            .uid(uid)
            .user(user)
            .specialization(dto.specialization())
            .build();
        trainerRepository.save(trainer);
        return trainer;
    }

    @Override
    @Transactional
    public void update(@NonNull UpdateTrainerDto dto) {
        var trainer = trainerRepository.findByUid(dto.uid())
            .orElseThrow(() -> new DomainNotFoundException(Trainer.class.getSimpleName(), dto.uid().toString()));
        userService.updateUserData(
            trainer.getUser().getUid(),
            dto.firstName(),
            dto.lastName(),
            dto.username()
        );
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
        userService.changePassword(dto);
    }

    @Override
    @Transactional
    public void toggleStatus(@NonNull ToggleStatusDto dto) {
        userService.toggleStatus(dto);
    }
}

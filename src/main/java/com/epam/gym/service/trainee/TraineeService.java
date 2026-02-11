package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.User;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.user.trainee.ITraineeRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.service.user.IUserService;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import com.epam.gym.service.user.dto.ToggleStatusDto;
import com.epam.gym.validator.IValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraineeService implements ITraineeService {

    private final IUsernameGenerator usernameGenerator;
    private final IPasswordGenerator passwordGenerator;
    private final ITraineeRepository traineeRepository;
    private final IUserService userService;
    @Qualifier("createTraineeValidator")
    private final IValidator<CreateTraineeDto> createTraineeValidator;
    @Qualifier("deleteTraineeValidator")
    private final IValidator<UUID> deleteTraineeValidator;

    @Override
    @Transactional
    public Trainee create(@NonNull CreateTraineeDto dto) {
        createTraineeValidator.validate(dto);
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
        var trainee = Trainee.builder()
            .uid(uid)
            .user(user)
            .address(dto.address())
            .birthdate(dto.birthdate())
            .build();
        traineeRepository.save(trainee);
        return trainee;
    }

    @Override
    @Transactional
    public void update(@NonNull UpdateTraineeDto dto) {
        var trainee = traineeRepository.findByUid(dto.uid())
            .orElseThrow(() -> new DomainNotFoundException(Trainee.class.getSimpleName(), dto.uid().toString()));
        userService.updateUserData(
            trainee.getUser().getUid(),
            dto.firstName(),
            dto.lastName(),
            dto.username()
        );
        trainee.setAddress(dto.address());
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public Trainee findByUsername(@NonNull String username) {
        return traineeRepository.findByUsername(username)
            .orElseThrow(() -> new DomainNotFoundException(Trainee.class.getSimpleName(), username));
    }

    @Override
    @Transactional
    public void changePassword(@NonNull ChangePasswordDto dto) {
        userService.changePassword(dto);
    }

    @Transactional
    @Override
    public void toggleStatus(@NonNull ToggleStatusDto dto) {
        userService.toggleStatus(dto);
    }

    @Override
    @Transactional
    public void delete(@NonNull UUID uid) {
        deleteTraineeValidator.validate(uid);
        traineeRepository.deleteByUid(uid);
    }
}

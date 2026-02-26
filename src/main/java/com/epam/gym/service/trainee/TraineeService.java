package com.epam.gym.service.trainee;

import com.epam.gym.exception.AuthException;
import com.epam.gym.exception.not.found.TraineeNotFoundException;
import com.epam.gym.service.auth.IPasswordService;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.domain.trainee.ITraineeRepository;
import com.epam.gym.service.trainee.dto.ChangePasswordDto;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraineeService implements ITraineeService {

    private final IPasswordGenerator passwordGenerator;
    private final IUsernameGenerator usernameGenerator;
    private final ITraineeRepository traineeRepository;
    private final IPasswordService passwordService;

    @Override
    @Transactional
    public Trainee create(@NonNull CreateTraineeDto dto) {
        var trainee = Trainee.builder()
            .uid(UUID.randomUUID())
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .username(usernameGenerator.generate(dto.firstName(), dto.lastName()))
            .password(passwordService.hashPassword(passwordGenerator.generate()))
            .birthdate(dto.birthdate())
            .address(dto.address())
            .active(true)
            .build();
        traineeRepository.save(trainee);
        return trainee;
    }

    @Override
    @Transactional
    public void update(@NonNull UpdateTraineeDto dto) {
        var trainee = getByUsername(dto.username());
        trainee.setFirstName(dto.firstName());
        trainee.setLastName(dto.lastName());
        trainee.setAddress(dto.address());
        trainee.setBirthdate(dto.birthdate());
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void changePassword(@NonNull ChangePasswordDto dto) {
        var trainee = getByUsername(dto.username());
        if (!passwordService.checkPassword(dto.oldPassword(), trainee.getPassword())) {
            throw new AuthException();
        }
        trainee.setPassword(passwordService.hashPassword(dto.newPassword()));
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void toggleStatus(@NonNull String username) {
        var trainee = getByUsername(username);
        trainee.toggleActive();
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void delete(@NonNull String username) {
        traineeRepository.deleteByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee getByUsername(@NonNull String username) {
        return traineeRepository.getByUsername(username)
            .orElseThrow(() -> new TraineeNotFoundException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getByUids(@NonNull List<UUID> uids) {
        if (uids.isEmpty()) {
            return List.of();
        }
        return traineeRepository.findAllByUids(uids);
    }
}

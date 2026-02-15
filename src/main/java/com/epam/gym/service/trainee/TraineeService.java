package com.epam.gym.service.trainee;

import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.domain.trainee.ITraineeRepository;
import com.epam.gym.service.trainee.dto.ChangePasswordDto;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.service.trainer.ITrainerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TraineeService implements ITraineeService {

    private final IPasswordGenerator passwordGenerator;
    private final IUsernameGenerator usernameGenerator;
    private final ITraineeRepository traineeRepository;

    @Override
    @Transactional
    public Trainee create(@NonNull CreateTraineeDto dto) {
        var uid = UUID.randomUUID();
        var password = passwordGenerator.generate();
        var username = usernameGenerator.generate(dto.firstName(), dto.lastName());
        var trainee = Trainee.builder()
            .uid(uid)
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .username(username)
            .password(password)
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
        if (Objects.equals(trainee.getPassword(), dto.oldPassword())) {
            throw new RuntimeException("Wrong password");
        }
        trainee.setPassword(dto.newPassword());
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional(readOnly = true)
    public void toggleStatus(@NonNull String username) {
        var trainee = getByUsername(username);
        trainee.setActive(!trainee.isActive());
        traineeRepository.save(trainee);
    }

    @Override
    @Transactional
    public void delete(@NonNull String username) {
        traineeRepository.deleteByUsername(username);
    }


    @Override
    @Transactional(readOnly = true)
    public Trainee getByUsername(String username) {
        return traineeRepository.getByUsername(username)
            .orElseThrow(() -> new RuntimeException("Trainee %s not found".formatted(username)));
    }
}

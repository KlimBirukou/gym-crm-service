package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.trainee.ITraineeRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public final class TraineeService implements ITraineeService {

    private IUsernameGenerator usernameGenerator;

    private IPasswordGenerator passwordGenerator;

    private ITraineeRepository traineeRepository;

    @Override
    public Trainee create(@NonNull CreateTraineeDto dto) {
        log.debug("Creating trainee with firstName = {}, lastName = {}", dto.firstName(), dto.lastName());
        var trainee = Trainee.builder()
            .uid(UUID.randomUUID())
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .address(dto.address())
            .username(usernameGenerator.generate(dto.firstName(), dto.lastName()))
            .password(passwordGenerator.generate())
            .isActive(true)
            .build();
        traineeRepository.save(trainee);
        log.debug("Trainee created with uid = {}", trainee.getUid());
        return trainee;
    }

    @Override
    public void update(@NonNull UpdateTraineeDto dto) {
        log.debug("Updating trainee uid = {}", dto.uid());
        var trainee = traineeRepository.findByUid(dto.uid())
            .orElseThrow(() -> new RuntimeException("Trainee not found"));
        trainee.setAddress(dto.address());
        traineeRepository.save(trainee);
        log.debug("Trainee with uid = {} updated", trainee.getUid());
    }

    @Override
    public void delete(@NonNull UUID uid) {
        log.debug("Deleting trainee with uid = {}", uid);
        traineeRepository.findByUid(uid)
            .orElseThrow(() -> new RuntimeException("Trainee not found"));
        traineeRepository.deleteByUid(uid);
        log.debug("Deleted trainee with uid = {}", uid);
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
    public void setTrainerRepository(ITraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }
}

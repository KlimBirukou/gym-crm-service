package com.epam.gym.service.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.repository.user.trainee.ITraineeRepository;
import com.epam.gym.service.generator.name.IUsernameGenerator;
import com.epam.gym.service.generator.password.IPasswordGenerator;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.service.trainee.dto.UpdateTraineeDto;
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
        var trainee = Trainee.builder()
            .uid(uid)
            .firstName(dto.firstName())
            .lastName(dto.lastName())
            .address(dto.address())
            .username(username)
            .password(password)
            .birthdate(dto.birthdate())
            .active(true)
            .build();
        traineeRepository.save(trainee);
        return trainee;
    }

    @Override
    @Transactional
    public void update(@NonNull UpdateTraineeDto dto) {
        var trainee = traineeRepository.findByUid(dto.uid())
            .orElseThrow(() -> new DomainNotFoundException(Trainee.class.getSimpleName(), dto.uid()))
            .setAddress(dto.address());
        traineeRepository.save(trainee);
    }

    @Override
    public void delete(@NonNull UUID uid) {
        deleteTraineeValidator.validate(uid);
        traineeRepository.deleteByUid(uid);
    }
}

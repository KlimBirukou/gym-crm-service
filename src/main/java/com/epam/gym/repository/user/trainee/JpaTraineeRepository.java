package com.epam.gym.repository.user.trainee;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.jpa.repository.ITraineeEntityRepository;
import com.epam.gym.repository.jpa.entity.TraineeEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaTraineeRepository implements ITraineeRepository {

    private final ITraineeEntityRepository traineeEntityRepository;
    private final ConversionService conversionService;

    @Transactional
    public boolean existByUid(@NonNull UUID uid) {
        return traineeEntityRepository.existsById(uid);
    }

    @Override
    @Transactional
    public void save(@NonNull Trainee trainee) {
        var entity = conversionService.convert(trainee, TraineeEntity.class);
        if (Objects.isNull(entity)) {
            throw new IllegalStateException("Failed conversion Trainee to TraineeEntity");
        }
        traineeEntityRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findByUid(@NonNull UUID uid) {
        return traineeEntityRepository.findById(uid)
            .map(entity -> conversionService.convert(entity, Trainee.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> findByUsername(@NonNull String username) {
        return traineeEntityRepository.findByUserUsername(username)
            .map(entity -> conversionService.convert(entity, Trainee.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findByFirstNameAndLastName(@NonNull String firstName, @NonNull String lastName) {
        return traineeEntityRepository.findByUserFirstNameAndUserLastName(firstName, lastName)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainee.class))
            .toList();
    }

    @Override
    @Transactional
    public void deleteByUid(@NonNull UUID uid) {
        traineeEntityRepository.deleteById(uid);
    }
}

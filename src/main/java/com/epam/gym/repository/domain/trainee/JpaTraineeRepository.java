package com.epam.gym.repository.domain.trainee;

import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.jpa.trainee.ITraineeEntityRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaTraineeRepository implements ITraineeRepository {

    private final ITraineeEntityRepository repository;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(@NonNull String username) {
        return repository.existsByUserUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainee> getByUsername(@NonNull String username) {
        return repository.findByUserUsername(username)
            .map(entity -> conversionService.convert(entity, Trainee.class));
    }

    @Override
    @Transactional
    public void save(@NonNull Trainee trainee) {
        var entity = conversionService.convert(trainee, TraineeEntity.class);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByUsername(@NonNull String username) {
        repository.deleteByUserUsername(username);
    }


    @Override
    public List<Trainee> getByFirstAndNameLastName(@NonNull String firstname, @NonNull String lastName) {
        return repository.findByUserFirstNameAndUserLastName(firstname, lastName).stream()
            .map(entity -> conversionService.convert(entity, Trainee.class))
            .toList();
    }
}

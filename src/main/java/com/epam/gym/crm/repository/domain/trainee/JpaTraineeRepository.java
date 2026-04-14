package com.epam.gym.crm.repository.domain.trainee;

import com.epam.gym.crm.exception.not.found.TraineeNotFoundException;
import com.epam.gym.crm.repository.entity.TraineeEntity;
import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.repository.jpa.trainee.ITraineeEntityRepository;
import com.epam.gym.crm.repository.mapper.ITraineeEntityToTraineeMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
@RequiredArgsConstructor
public class JpaTraineeRepository implements ITraineeRepository {

    private final ITraineeEntityRepository repository;
    private final ConversionService conversionService;
    private final ITraineeEntityToTraineeMapper mapper;

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
    public void update(@NonNull Trainee trainee) {
        var entity = repository.findByUserUsername(trainee.getUsername())
            .orElseThrow(() -> new TraineeNotFoundException(trainee.getUsername()));
        mapper.updateEntity(trainee, entity);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByUsername(@NonNull String username) {
        repository.deleteByUserUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getByFirstNameAndLastName(@NonNull String firstname, @NonNull String lastName) {
        return repository.findByUserFirstNameAndUserLastName(firstname, lastName)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainee.class))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> findAllByUids(@NonNull List<UUID> uids) {
        return repository.findAllByUidIn(uids)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainee.class))
            .toList();
    }
}

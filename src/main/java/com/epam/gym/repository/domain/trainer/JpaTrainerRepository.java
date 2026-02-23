package com.epam.gym.repository.domain.trainer;

import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.jpa.trainer.ITrainerEntityRepository;
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
public class JpaTrainerRepository implements ITrainerRepository {

    private final ITrainerEntityRepository repository;
    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(@NonNull String username) {
        return repository.existsByUserUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> getByUsername(@NonNull String username) {
        return repository.findByUserUsername(username)
            .map(entity -> conversionService.convert(entity, Trainer.class));
    }

    @Override
    @Transactional
    public void save(@NonNull Trainer trainer) {
        var entity = conversionService.convert(trainer, TrainerEntity.class);
        repository.save(entity);
    }

    @Override
    public List<Trainer> getByFirstNameAndLastName(@NonNull String firstname, @NonNull String lastName) {
        return repository.findByUserFirstNameAndUserLastName(firstname, lastName).stream()
            .map(entity -> conversionService.convert(entity, Trainer.class))
            .toList();
    }
}

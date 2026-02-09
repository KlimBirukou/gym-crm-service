package com.epam.gym.repository.user.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.jpa.user.trainer.ITrainerEntityRepository;
import com.epam.gym.repository.jpa.user.trainer.TrainerEntity;
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
public class JpaTrainerRepository implements ITrainerRepository {

    private final ITrainerEntityRepository trainerEntityRepository;
    private final ConversionService conversionService;

    @Override
    @Transactional
    public boolean existByUid(@NonNull UUID uid) {
        return trainerEntityRepository.existsById(uid);
    }

    @Override
    @Transactional
    public void save(@NonNull Trainer trainer) {
        var entity = conversionService.convert(trainer, TrainerEntity.class);
        if (Objects.isNull(entity)) {
            throw new IllegalStateException("Failed conversion Trainer to TrainerEntity");
        }
        trainerEntityRepository.save(entity);
    }

    @Override
    @Transactional
    public List<Trainer> findByFirstNameAndLastName(@NonNull String firstName, @NonNull String lastName) {
        return trainerEntityRepository.findByUserFirstNameAndUserLastName(firstName, lastName)
            .stream()
            .map(entity -> conversionService.convert(entity, Trainer.class))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Trainer> findByUid(@NonNull UUID uid) {
        return trainerEntityRepository.findById(uid)
            .map(entity -> conversionService.convert(entity, Trainer.class));
    }
}
